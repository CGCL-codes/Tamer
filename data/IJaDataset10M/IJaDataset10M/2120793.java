package com.reserveamerica.elastica.appserver.jboss;

import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.naming.NamingException;
import org.jboss.annotation.ejb.Clustered;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.aop.Advisor;
import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AdviceStack;
import org.jboss.aspects.remoting.FamilyWrapper;
import org.jboss.ejb3.JBossProxy;
import org.jboss.ejb3.ProxyFactoryHelper;
import org.jboss.ejb3.remoting.RemoteProxyFactory;
import org.jboss.ejb3.stateless.BaseStatelessProxyFactory;
import org.jboss.ejb3.stateless.StatelessContainer;
import org.jboss.ejb3.stateless.StatelessHandleImpl;
import org.jboss.ha.framework.interfaces.ClusteringTargetsRepository;
import org.jboss.ha.framework.interfaces.DistributedReplicantManager;
import org.jboss.ha.framework.interfaces.HAPartition;
import org.jboss.ha.framework.interfaces.LoadBalancePolicy;
import org.jboss.ha.framework.interfaces.RandomRobin;
import org.jboss.ha.framework.server.HATarget;
import org.jboss.logging.Logger;
import org.jboss.remoting.InvokerLocator;
import com.reserveamerica.commons.Invalidatable;
import com.reserveamerica.elastica.appserver.NodeRolesView;
import com.reserveamerica.elastica.appserver.NodeStatesManager;
import com.reserveamerica.elastica.appserver.NodeStatesManagerFactory;
import com.reserveamerica.elastica.appserver.NodeStatesView;
import com.reserveamerica.elastica.appserver.ServiceControl;
import com.reserveamerica.elastica.cluster.NodeKey;
import com.reserveamerica.elastica.server.ServerEndpoint;

/**
 * This is the proxy factory used to create Dynamic Clustering-aware EJB3 proxies.
 * 
 * @author BStasyszyn
 */
public class DynamicClusterProxyFactory extends BaseStatelessProxyFactory implements RemoteProxyFactory, DistributedReplicantManager.ReplicantListener, Invalidatable {

    private static final Logger log = Logger.getLogger(DynamicClusterProxyFactory.class);

    private static final String SERVICE_NAME = "EJB3";

    private static final String cluster = Util.getClusterName();

    private RemoteBinding binding;

    private InvokerLocator locator;

    private DistributedReplicantManager drm;

    private HATarget hatarget;

    private String proxyFamilyName;

    private LoadBalancePolicy lbPolicy;

    private FamilyWrapper wrapper;

    private NodeStatesView nodeStatesView;

    private NodeRolesView nodeRolesView;

    private ProxyWrapper proxyWrapper;

    public synchronized void setRemoteBinding(RemoteBinding binding) {
        this.binding = binding;
    }

    public String getServiceName() {
        return SERVICE_NAME;
    }

    public synchronized String getProxyFamilyName() {
        return proxyFamilyName;
    }

    public String getPartitionName() {
        return ((StatelessContainer) getContainer()).getPartitionName();
    }

    public String getCluster() {
        return cluster;
    }

    protected synchronized FamilyWrapper getWrapper() {
        return wrapper;
    }

    protected synchronized NodeRolesView getNodeRolesView() {
        return nodeRolesView;
    }

    protected synchronized NodeStatesView getNodeStatesView() {
        return nodeStatesView;
    }

    protected synchronized LoadBalancePolicy getLoadBalancePolicy() {
        return lbPolicy;
    }

    protected Class<?>[] getInterfaces() {
        Class<?>[] remoteInterfaces = ProxyFactoryHelper.getRemoteInterfaces(getContainer());
        Class<?>[] interfaces = new Class[remoteInterfaces.length + 2];
        System.arraycopy(remoteInterfaces, 0, interfaces, 0, remoteInterfaces.length);
        interfaces[remoteInterfaces.length] = JBossProxy.class;
        interfaces[remoteInterfaces.length + 1] = ServiceControl.class;
        return interfaces;
    }

    protected synchronized void initializeJndiName() {
        jndiName = ProxyFactoryHelper.getRemoteJndiName(getContainer(), binding);
    }

    @SuppressWarnings("unchecked")
    public synchronized void start() throws Exception {
        String clientBindUrl = ProxyFactoryHelper.getClientBindUrl(binding);
        locator = new InvokerLocator(clientBindUrl);
        Clustered clustered = (Clustered) ((Advisor) getContainer()).resolveAnnotation(Clustered.class);
        if (clustered == null) {
            throw new RuntimeException("Could not find @Clustered annotation.  Cannot deploy.");
        }
        String partitionName = getPartitionName();
        proxyFamilyName = ((StatelessContainer) getContainer()).getDeploymentQualifiedName() + "." + locator.getProtocol() + "." + partitionName;
        HAPartition partition = (HAPartition) getContainer().getInitialContext().lookup("/HAPartition/" + partitionName);
        hatarget = new HATarget(partition, proxyFamilyName, locator, HATarget.ENABLE_INVOCATIONS);
        ClusteringTargetsRepository.initTarget(proxyFamilyName, hatarget.getReplicants());
        ((StatelessContainer) getContainer()).getClusterFamilies().put(proxyFamilyName, hatarget);
        if (clustered.loadBalancePolicy() == null || clustered.loadBalancePolicy().equals(LoadBalancePolicy.class)) {
            lbPolicy = new RandomRobin();
        } else {
            lbPolicy = (LoadBalancePolicy) clustered.loadBalancePolicy().newInstance();
        }
        wrapper = new FamilyWrapper(proxyFamilyName, hatarget.getReplicants());
        this.drm = partition.getDistributedReplicantManager();
        drm.registerListener(proxyFamilyName, this);
        JBossServer server = JBossServer.getInstance();
        ServerEndpoint endpoint = new ServerEndpoint(getServiceName(), locator.getHost(), locator.getHost(), locator.getPort());
        ServerEndpoint existingEndpoint = server.registerServerEndpoint(endpoint);
        if (existingEndpoint == null) {
            log.info("startService(" + getProxyFamilyName() + ") - Registered server endpoint " + endpoint + " for service " + getServiceName() + ". Starting node-states manager for service " + getServiceName());
            NodeStatesManager nodeStatesManager = new NodeStatesManager(getServiceName(), NodeKey.getKey(endpoint.getAddress(), endpoint.getPort()));
            NodeStatesManager existingNodeStatesManager = NodeStatesManagerFactory.getInstance().register(nodeStatesManager);
            if (existingNodeStatesManager == null) {
                nodeStatesManager.start();
            }
        } else if (log.isDebugEnabled()) {
            log.info("startService(" + getProxyFamilyName() + ") - Server endpoint " + existingEndpoint + " already registered for service " + getServiceName() + ". No action required.");
        }
        super.start();
        NodeStatesManagerFactory.getInstance().get(getServiceName()).registerListener(this);
    }

    public synchronized void stop() throws Exception {
        try {
            NodeStatesManagerFactory.getInstance().get(getServiceName()).stop();
        } catch (RuntimeException ex) {
            log.warn("stop(" + getProxyFamilyName() + ") - Caught exception while attempting to stop NodeStatesManagerFactory.", ex);
        }
        super.stop();
        hatarget.destroy();
        drm.unregisterListener(proxyFamilyName, this);
        ((StatelessContainer) getContainer()).getClusterFamilies().remove(proxyFamilyName);
    }

    public synchronized Object createProxy() {
        String stackName;
        if (binding.interceptorStack() != null && binding.interceptorStack().length() > 0) {
            stackName = binding.interceptorStack();
        } else {
            stackName = "DynamicClusterClientInterceptors";
        }
        AdviceStack stack = AspectManager.instance().getAdviceStack(stackName);
        NodeStatesManager nodeStatesManager = NodeStatesManagerFactory.getInstance().get(getServiceName());
        nodeStatesView = nodeStatesManager.getNodeStatesView();
        nodeRolesView = nodeStatesManager.getNodeRolesView();
        Object proxy = constructProxy(newProxy(stack));
        proxyWrapper = newProxyWrapper(jndiName + "Proxy", proxy);
        return constructProxy(proxyWrapper);
    }

    protected ProxyWrapper newProxyWrapper(String jndiName, Object proxy) {
        return new DynamicClusterProxyWrapper(getCluster(), jndiName, proxy);
    }

    protected InvocationHandler newProxy(AdviceStack stack) {
        return new DynamicClusterProxy(getContainer(), stack.createInterceptors((Advisor) getContainer(), null), getWrapper(), getLoadBalancePolicy(), getPartitionName(), getCluster(), getNodeStatesView(), getNodeRolesView());
    }

    protected void bindProxy(Object proxy) throws NamingException {
        try {
            log.debug("Binding proxy for " + getContainer().getEjbName() + " in JNDI at " + jndiName);
            org.jboss.naming.Util.rebind(getContainer().getInitialContext(), jndiName, proxy);
            org.jboss.naming.Util.rebind(getContainer().getInitialContext(), proxyWrapper.getProxyJndiName(), proxyWrapper.getProxy());
        } catch (NamingException ex) {
            NamingException namingException = new NamingException("Could not bind stateless proxy with ejb name " + getContainer().getEjbName() + " into JNDI under jndiName: " + getContainer().getInitialContext().getNameInNamespace() + "/" + jndiName);
            namingException.setRootCause(ex);
            throw namingException;
        }
    }

    protected StatelessHandleImpl getHandle() {
        StatelessHandleImpl handle = new StatelessHandleImpl();
        RemoteBinding remoteBinding = (RemoteBinding) ((Advisor) getContainer()).resolveAnnotation(RemoteBinding.class);
        if (remoteBinding != null) handle.jndiName = remoteBinding.jndiBinding();
        return handle;
    }

    public synchronized void replicantsChanged(String key, List newReplicants, int newReplicantsViewId) {
        if (log.isInfoEnabled()) log.info("replicantsChanged(" + getProxyFamilyName() + ") - New Replicants view ID: " + newReplicantsViewId + ", New Replicants: " + newReplicants);
        try {
            ArrayList targets = new ArrayList(newReplicants);
            wrapper.get().updateClusterInfo(targets, newReplicantsViewId);
            bindProxy(createProxy());
        } catch (Exception ex) {
            log.error("replicantsChanged(" + getProxyFamilyName() + ") - Unable to bind new proxy.", ex);
        }
    }

    /**
   * Implementation of {@link Invalidatable}. Called when either the node states view or the node roles view changes.
   * Since the proxy is serialized into the JNDI server, we need to create a fresh proxy and rebind.
   */
    public synchronized void invalidate(Object key) {
        if (log.isDebugEnabled()) log.debug("invalidate(" + getProxyFamilyName() + ") - Received invalidation message [" + Arrays.toString((Object[]) key) + "].");
        if ((nodeStatesView != null) && (nodeRolesView != null)) {
            NodeStatesManager nodeStatesManager = NodeStatesManagerFactory.getInstance().get(getServiceName());
            if ((nodeStatesManager.getNodeStatesView().getViewId() == nodeStatesView.getViewId()) && (nodeStatesManager.getNodeRolesView().getViewId() == nodeRolesView.getViewId())) {
                if (log.isDebugEnabled()) log.debug("invalidate(" + getProxyFamilyName() + ") - No change in node states or node roles. Rebind of proxy unnecessary.");
                return;
            }
        }
        if (log.isDebugEnabled()) log.debug("invalidate(" + getProxyFamilyName() + ") - Node states or roles have changed. Binding new proxy.");
        try {
            bindProxy(createProxy());
        } catch (NamingException ex) {
            log.error("invalidate(" + getProxyFamilyName() + ") - Unable to bind new proxy.", ex);
        }
    }
}
