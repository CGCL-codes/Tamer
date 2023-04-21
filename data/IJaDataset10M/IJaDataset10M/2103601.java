package org.xactor.ws.server;

import java.lang.reflect.Method;
import java.security.Principal;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.transaction.Transaction;
import org.jboss.ejb.EjbModule;
import org.jboss.ejb.Interceptor;
import org.jboss.ejb.StatelessSessionContainer;
import org.jboss.invocation.Invocation;
import org.jboss.invocation.InvocationKey;
import org.jboss.invocation.InvocationType;
import org.jboss.invocation.PayloadKey;
import org.jboss.logging.Logger;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.security.SecurityAssociation;
import org.jboss.ws.WSException;
import org.jboss.ws.core.CommonMessageContext;
import org.jboss.ws.core.EndpointInvocation;
import org.jboss.ws.core.jaxrpc.handler.SOAPMessageContextJAXRPC;
import org.jboss.ws.core.server.AbstractServiceEndpointInvoker;
import org.jboss.ws.core.server.ServiceEndpointInfo;
import org.jboss.ws.core.server.ServiceEndpointInvoker;
import org.jboss.ws.core.soap.MessageContextAssociation;
import org.jboss.ws.core.utils.ObjectNameFactory;
import org.jboss.ws.metadata.j2ee.UnifiedApplicationMetaData;
import org.jboss.ws.metadata.j2ee.UnifiedBeanMetaData;
import org.jboss.ws.metadata.umdm.ServerEndpointMetaData;
import org.jboss.ws.metadata.umdm.EndpointMetaData.Type;
import org.jboss.ws.metadata.umdm.HandlerMetaData.HandlerType;
import org.xactor.ws.atomictx.TxServerHandler;

/**
 * Handles invocations on EJB2.1 endpoints.
 *
 * @author Thomas.Diesler@jboss.org
 * @since 19-Jan-2005
 */
public class ServiceEndpointInvokerEJB21 extends AbstractServiceEndpointInvoker implements ServiceEndpointInvoker {

    private Logger log = Logger.getLogger(ServiceEndpointInvokerEJB21.class);

    private String jndiName;

    private MBeanServer server;

    private ObjectName objectName;

    public ServiceEndpointInvokerEJB21() {
        server = MBeanServerLocator.locateJBoss();
    }

    /** Initialize the service endpoint */
    @Override
    public void init(ServiceEndpointInfo seInfo) {
        super.init(seInfo);
        ServerEndpointMetaData epMetaData = seInfo.getServerEndpointMetaData();
        String ejbName = epMetaData.getLinkName();
        if (ejbName == null) throw new WSException("Cannot obtain ejb-link from port component");
        UnifiedApplicationMetaData applMetaData = (UnifiedApplicationMetaData) seInfo.getUnifiedDeploymentInfo().metaData;
        UnifiedBeanMetaData beanMetaData = (UnifiedBeanMetaData) applMetaData.getBeanByEjbName(ejbName);
        if (beanMetaData == null) throw new WSException("Cannot obtain ejb meta data for: " + ejbName);
        String seiName = epMetaData.getServiceEndpointInterfaceName();
        if (epMetaData.getType() == Type.JAXRPC && seiName != null) {
            String bmdSEI = beanMetaData.getServiceEndpointInterface();
            if (seiName.equals(bmdSEI) == false) throw new WSException("Endpoint meta data defines SEI '" + seiName + "', <service-endpoint> in ejb-jar.xml defines '" + bmdSEI + "'");
        }
        jndiName = beanMetaData.getContainerObjectNameJndiName();
        if (jndiName == null) throw new WSException("Cannot obtain JNDI name for: " + ejbName);
        objectName = ObjectNameFactory.create("jboss.j2ee:jndiName=" + jndiName + ",service=EJB");
        try {
            EjbModule ejbModule = (EjbModule) server.getAttribute(objectName, "EjbModule");
            StatelessSessionContainer container = (StatelessSessionContainer) ejbModule.getContainer(ejbName);
            boolean injectionPointFound = false;
            Interceptor prev = container.getInterceptor();
            while (prev != null && prev.getNext() != null) {
                Interceptor next = prev.getNext();
                if (next.getNext() == null) {
                    log.debug("Inject service endpoint interceptor after: " + prev.getClass().getName());
                    ServiceEndpointInterceptor sepInterceptor = new ServiceEndpointInterceptor();
                    prev.setNext(sepInterceptor);
                    sepInterceptor.setNext(next);
                    injectionPointFound = true;
                }
                prev = next;
            }
            if (injectionPointFound == false) log.warn("Cannot service endpoint interceptor injection point");
        } catch (Exception ex) {
            log.warn("Cannot add service endpoint interceptor", ex);
        }
    }

    /** Load the SEI implementation bean if necessary 
    */
    public Class loadServiceEndpoint() {
        if (server.isRegistered(objectName) == false) throw new WSException("Cannot find service endpoint target: " + objectName);
        return null;
    }

    /** Create an instance of the SEI implementation bean if necessary */
    public Object createServiceEndpointInstance(Object endpointContext, Class seiImplClass) {
        return null;
    }

    /** Invoke an instance of the SEI implementation bean */
    public void invokeServiceEndpointInstance(Object seiImpl, EndpointInvocation epInv) throws Exception {
        log.debug("invokeServiceEndpoint: " + epInv.getJavaMethod().getName());
        Principal principal = SecurityAssociation.getPrincipal();
        Object credential = SecurityAssociation.getCredential();
        CommonMessageContext msgContext = MessageContextAssociation.peekMessageContext();
        try {
            Method method = epInv.getJavaMethod();
            Object[] args = epInv.getRequestPayload();
            Transaction tx = TxServerHandler.getCurrentTransaction();
            Invocation inv = new Invocation(null, method, args, tx, principal, credential);
            if ((msgContext instanceof javax.xml.rpc.handler.MessageContext) == false) msgContext = new SOAPMessageContextJAXRPC(msgContext);
            inv.setValue(InvocationKey.SOAP_MESSAGE_CONTEXT, msgContext);
            inv.setValue(InvocationKey.SOAP_MESSAGE, msgContext.getSOAPMessage());
            inv.setType(InvocationType.SERVICE_ENDPOINT);
            ServerEndpointMetaData sepMetaData = seInfo.getServerEndpointMetaData();
            inv.setValue(HandlerCallback.class.getName(), new HandlerCallback(sepMetaData), PayloadKey.TRANSIENT);
            inv.setValue(EndpointInvocation.class.getName(), epInv, PayloadKey.TRANSIENT);
            String[] sig = { Invocation.class.getName() };
            Object retObj = server.invoke(objectName, "invoke", new Object[] { inv }, sig);
            epInv.setReturnValue(retObj);
        } catch (Exception e) {
            handleInvocationException(e);
        }
    }

    /** Create an instance of the SEI implementation bean if necessary */
    public void destroyServiceEndpointInstance(Object seiImpl) {
    }

    /** Handlers are beeing called through the HandlerCallback from the EJB interceptor */
    @Override
    public boolean callRequestHandlerChain(ServerEndpointMetaData sepMetaData, HandlerType type) {
        if (type == HandlerType.PRE) return delegate.callRequestHandlerChain(sepMetaData, type); else return true;
    }

    /** Handlers are beeing called through the HandlerCallback from the EJB interceptor */
    public boolean callResponseHandlerChain(ServerEndpointMetaData sepMetaData, HandlerType type) {
        if (type == HandlerType.PRE) return delegate.callResponseHandlerChain(sepMetaData, type); else return true;
    }

    /** Handlers are beeing called through the HandlerCallback from the EJB interceptor */
    public boolean callFaultHandlerChain(ServerEndpointMetaData sepMetaData, HandlerType type, Exception ex) {
        if (type == HandlerType.PRE) return delegate.callFaultHandlerChain(sepMetaData, type, ex); else return true;
    }

    public class HandlerCallback {

        private ServerEndpointMetaData sepMetaData;

        public HandlerCallback(ServerEndpointMetaData sepMetaData) {
            this.sepMetaData = sepMetaData;
        }

        /** Handlers are beeing called through the HandlerCallback from the EJB interceptor */
        public boolean callRequestHandlerChain(HandlerType type) {
            if (type == HandlerType.PRE) return true; else return delegate.callRequestHandlerChain(sepMetaData, type);
        }

        /** Handlers are beeing called through the HandlerCallback from the EJB interceptor */
        public boolean callResponseHandlerChain(HandlerType type) {
            if (type == HandlerType.PRE) return true; else return delegate.callResponseHandlerChain(sepMetaData, type);
        }

        /** Handlers are beeing called through the HandlerCallback from the EJB interceptor */
        public boolean callFaultHandlerChain(HandlerType type, Exception ex) {
            if (type == HandlerType.PRE) return true; else return delegate.callFaultHandlerChain(sepMetaData, type, ex);
        }
    }
}
