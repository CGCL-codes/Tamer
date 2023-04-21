package com.reserveamerica.elastica.appserver.jboss;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aspects.remoting.ClusterConstants;
import org.jboss.ha.framework.interfaces.GenericClusteringException;
import org.jboss.ha.framework.server.HATarget;
import org.jboss.logging.Logger;
import org.jboss.mx.util.JMXExceptionDecoder;
import com.reserveamerica.commons.ChangeTrackingHashMap;
import com.reserveamerica.commons.concurrent.Throttle;
import com.reserveamerica.elastica.appserver.AppServerConstants;
import com.reserveamerica.elastica.appserver.NodeRolesView;
import com.reserveamerica.elastica.appserver.NodeStatesManager;
import com.reserveamerica.elastica.appserver.NodeStatesManagerFactory;
import com.reserveamerica.elastica.appserver.NodeStatesView;
import com.reserveamerica.elastica.appserver.ThrottleUtils;
import com.reserveamerica.elastica.cluster.ActiveClusterConfigCache;
import com.reserveamerica.elastica.cluster.ClusterConfigException;
import com.reserveamerica.elastica.cluster.GlobalContext;
import com.reserveamerica.elastica.cluster.NodeKey;
import com.reserveamerica.elastica.cluster.ServerContext;
import com.reserveamerica.elastica.common.ShouldInvoke;
import com.reserveamerica.elastica.server.Server;
import com.reserveamerica.elastica.server.ServerState;

/**
 * The main server-side EJB3 interceptor for dynamic-clustering.
 * 
 * @author bstasyszyn
 */
public class DynamicClusterInterceptor implements Interceptor {

    private static final Logger log = Logger.getLogger(DynamicClusterInterceptor.class);

    private final Map families;

    private final String cluster = Util.getClusterName();

    public DynamicClusterInterceptor(Map families) {
        this.families = families;
    }

    public String getName() {
        return getClass().getName();
    }

    @SuppressWarnings("unchecked")
    public Object invoke(Invocation invocation) throws Throwable {
        String clientFamily = (String) invocation.getMetaData(ClusterConstants.CLUSTERED_REMOTING, ClusterConstants.CLUSTER_FAMILY);
        final String serviceName = clientFamily;
        boolean restoreIsLocalCall = ServerContext.getInstance().isLocalCall();
        String restoreClientHost = ServerContext.getInstance().getClientHost();
        boolean isLocalCall = (restoreClientHost != null);
        try {
            ServerContext.getInstance().setLocalCall(isLocalCall);
            ServerContext.getInstance().setClientHost((String) invocation.getMetaData(AppServerConstants.GROUP_ID, AppServerConstants.CLIENT_ADDRESS));
            Map<String, Object> contextProperties;
            if (!isLocalCall) {
                contextProperties = new ChangeTrackingHashMap<String, Object>((Map<String, Object>) invocation.getMetaData(AppServerConstants.GROUP_ID, AppServerConstants.GLOBAL_CONTEXT));
                GlobalContext.getInstance().set(contextProperties);
            } else {
                contextProperties = null;
            }
            long clientViewId = ((Long) invocation.getMetaData(ClusterConstants.CLUSTERED_REMOTING, ClusterConstants.CLUSTER_VIEW_ID)).longValue();
            Long oClientStatesViewId = (Long) invocation.getMetaData(AppServerConstants.GROUP_ID, AppServerConstants.STATE_VIEW_ID);
            Long oClientRolesViewId = (Long) invocation.getMetaData(AppServerConstants.GROUP_ID, AppServerConstants.NODE_ROLES_VIEW_ID);
            long clientStatesViewId = (oClientStatesViewId != null) ? oClientStatesViewId.longValue() : 0L;
            long clientRolesViewId = (oClientRolesViewId != null) ? oClientRolesViewId.longValue() : 0L;
            HATarget target = (HATarget) families.get(clientFamily);
            if (target == null) {
                throw new GenericClusteringException(GenericClusteringException.COMPLETED_NO, "target is not/no more registered on this node");
            }
            boolean invoked = false;
            Object rtn;
            final ShouldInvoke shouldInvoke = shouldInvoke(invocation);
            if (shouldInvoke == ShouldInvoke.DONT_INVOKE) {
                MethodInvocation mi = (MethodInvocation) invocation;
                Method method = mi.getMethod();
                if (method.getName().equals("getServerInfo")) {
                    rtn = Server.getInstance().getServerInfo("EJB3");
                } else {
                    rtn = null;
                }
            } else {
                if (!target.invocationsAllowed()) {
                    throw new GenericClusteringException(GenericClusteringException.COMPLETED_NO, "invocations are currently not allowed on this target");
                }
                if (shouldInvoke == ShouldInvoke.THROTTLE) {
                    Throttle throttle = Server.getInstance().getRequestThrottle();
                    if (throttle.tryAcquire() == Throttle.AcquireResult.ACQUIRED) {
                        try {
                            invoked = true;
                            rtn = invocation.invokeNext();
                        } finally {
                            throttle.release();
                        }
                    } else {
                        rtn = null;
                        Server.getInstance().setState(ServerState.BUSY);
                    }
                } else {
                    invoked = true;
                    rtn = invocation.invokeNext();
                }
            }
            if (clientViewId != target.getCurrentViewId()) {
                synchronized (target.getReplicants()) {
                    invocation.addResponseAttachment(AppServerConstants.REPLICANTS, new ArrayList(target.getReplicants()));
                    invocation.addResponseAttachment(AppServerConstants.VIEW_ID, Long.valueOf(target.getCurrentViewId()));
                }
            }
            NodeStatesManager nodeStatesManager = NodeStatesManagerFactory.getInstance().get("EJB3");
            final NodeStatesView nodeStatesView = nodeStatesManager.getNodeStatesView();
            ServerState currentState = Server.getInstance().getState();
            if ((clientStatesViewId != nodeStatesView.getViewId()) || (currentState != ServerState.ONLINE)) {
                invocation.addResponseAttachment(AppServerConstants.SERVER_STATES, new HashMap<NodeKey, ServerState>(nodeStatesView.getStates()));
                invocation.addResponseAttachment(AppServerConstants.SERVER_STATES_VIEW_ID, Long.valueOf(nodeStatesView.getViewId()));
            }
            final NodeRolesView nodeRolesView = nodeStatesManager.getNodeRolesView();
            if (clientRolesViewId != nodeRolesView.getViewId()) {
                invocation.addResponseAttachment(AppServerConstants.SERVER_ROLES, new HashMap<NodeKey, Set<String>>(nodeRolesView.getNodeRoleIds()));
                invocation.addResponseAttachment(AppServerConstants.SERVER_ROLES_VIEW_ID, Long.valueOf(nodeRolesView.getViewId()));
            }
            invocation.addResponseAttachment(AppServerConstants.INVOKED, Boolean.valueOf(invoked));
            if (!isLocalCall) {
                invocation.addResponseAttachment(AppServerConstants.SERVER_DATA, contextProperties);
            }
            try {
                invocation.addResponseAttachment(AppServerConstants.CLUSTER_CONFIG_VERSION, ActiveClusterConfigCache.getInstance().getActiveConfigInfo(cluster).getVersion());
            } catch (ClusterConfigException ex) {
                log.error("invoke(" + serviceName + ") - An unexpected error occurred while attempting to retrieve active cluster configuration for cluster [" + cluster + "].", ex);
            }
            return rtn;
        } catch (RuntimeException e) {
            Throwable th = JMXExceptionDecoder.decode(e);
            if (log.isDebugEnabled()) {
                log.debug("invoke(" + serviceName + ") - Failed to invoke on bean", th);
            }
            if (th instanceof Exception) {
                throw (Exception) th;
            }
            throw e;
        } finally {
            if (!isLocalCall) {
                GlobalContext.getInstance().reset();
            }
            ServerContext.getInstance().setLocalCall(restoreIsLocalCall);
            ServerContext.getInstance().setClientHost(restoreClientHost);
        }
    }

    private ShouldInvoke shouldInvoke(Invocation invocation) {
        return ThrottleUtils.shouldInvokeBean((ServerState) invocation.getMetaData(AppServerConstants.GROUP_ID, AppServerConstants.OVERRIDE_OFFLINE_NODE), invocation.getMetaData(AppServerConstants.GROUP_ID, AppServerConstants.REFRESH_VIEW) != null);
    }
}
