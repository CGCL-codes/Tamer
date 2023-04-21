package org.apache.axis2.jaxws.server.endpoint;

import org.apache.axis2.jaxws.ExceptionFactory;
import org.apache.axis2.jaxws.binding.BindingUtils;
import org.apache.axis2.jaxws.core.MessageContext;
import org.apache.axis2.jaxws.core.util.MessageContextUtils;
import org.apache.axis2.jaxws.description.EndpointDescription;
import org.apache.axis2.jaxws.description.EndpointInterfaceDescription;
import org.apache.axis2.jaxws.description.OperationDescription;
import org.apache.axis2.jaxws.description.ServiceDescription;
import org.apache.axis2.jaxws.i18n.Messages;
import org.apache.axis2.jaxws.message.Message;
import org.apache.axis2.jaxws.message.Protocol;
import org.apache.axis2.jaxws.message.XMLFault;
import org.apache.axis2.jaxws.message.XMLFaultCode;
import org.apache.axis2.jaxws.message.XMLFaultReason;
import org.apache.axis2.jaxws.message.factory.MessageFactory;
import org.apache.axis2.jaxws.registry.FactoryRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.http.HTTPBinding;
import java.util.Collection;
import java.util.Iterator;

public class Utils {

    public static final Log log = LogFactory.getLog(Utils.class);

    @Deprecated
    public static boolean bindingTypesMatch(MessageContext mc, ServiceDescription serviceDesc) {
        Collection<EndpointDescription> eds = serviceDesc.getEndpointDescriptions_AsCollection();
        if ((eds != null) && (eds.size() > 0)) {
            Iterator<EndpointDescription> i = eds.iterator();
            if (i.hasNext()) {
                EndpointDescription ed = eds.iterator().next();
                Protocol protocol = mc.getMessage().getProtocol();
                String bindingType = ed.getBindingType();
                if (log.isDebugEnabled()) {
                    log.debug("Checking for matching binding types.");
                    log.debug("    message protocol: " + protocol);
                    log.debug("        binding type: " + bindingType);
                }
                if (protocol.equals(Protocol.soap11)) {
                    return (BindingUtils.isSOAP11Binding(bindingType));
                } else if (protocol.equals(Protocol.soap12)) {
                    return (BindingUtils.isSOAP12Binding(bindingType));
                } else if (protocol.equals(Protocol.rest)) {
                    return HTTPBinding.HTTP_BINDING.equalsIgnoreCase(bindingType);
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("There were no endpoint descriptions found, thus the binding match failed.");
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Compares the version of the message in the MessageContext to what's expected
     * given the ServiceDescription.  The behavior is described in the SOAP 1.2
     * specification under Appendix 'A'.
     * 
     * @param mc
     * @param serviceDesc
     * @return
     */
    public static boolean bindingTypesMatch(MessageContext mc, EndpointDescription ed) {
        Protocol protocol = mc.getMessage().getProtocol();
        String bindingType = ed.getBindingType();
        if (log.isDebugEnabled()) {
            log.debug("Checking for matching binding types.");
            log.debug("    message protocol: " + protocol);
            log.debug("        binding type: " + bindingType);
        }
        if (protocol.equals(Protocol.soap11)) {
            return (BindingUtils.isSOAP11Binding(bindingType));
        } else if (protocol.equals(Protocol.soap12)) {
            return (BindingUtils.isSOAP12Binding(bindingType));
        } else if (protocol.equals(Protocol.rest)) {
            return HTTPBinding.HTTP_BINDING.equalsIgnoreCase(bindingType);
        }
        return true;
    }

    /**
     * Creates a fault message that reflects a version mismatch for the configured message protocol.
     * The returned message will always be a SOAP 1.1 message per the specification.
     * 
     * @param mc
     * @param msg
     * @return
     */
    public static MessageContext createVersionMismatchMessage(MessageContext mc, Protocol protocol) {
        if (protocol.equals(Protocol.soap12)) {
            String msg = "Incoming SOAP message protocol is version 1.2, but endpoint is configured for SOAP 1.1";
            return Utils.createFaultMessage(mc, msg);
        } else if (protocol.equals(Protocol.soap11)) {
            boolean canSupport = false;
            if (canSupport) {
                return null;
            } else {
                String msg = "Incoming SOAP message protocol is version 1.1, but endpoint is configured for SOAP 1.2.  This is not supported.";
                return Utils.createFaultMessage(mc, msg);
            }
        } else {
            String msg = "Incoming message protocol does not match endpoint protocol.";
            return Utils.createFaultMessage(mc, msg);
        }
    }

    public static MessageContext createFaultMessage(MessageContext mc, String msg) {
        try {
            XMLFault xmlfault = new XMLFault(XMLFaultCode.VERSIONMISMATCH, new XMLFaultReason(msg));
            MessageFactory mf = (MessageFactory) FactoryRegistry.getFactory(MessageFactory.class);
            Message message = mf.create(Protocol.soap11);
            message.setXMLFault(xmlfault);
            MessageContext responseMsgCtx = MessageContextUtils.createFaultMessageContext(mc);
            responseMsgCtx.setMessage(message);
            return responseMsgCtx;
        } catch (XMLStreamException e) {
            throw ExceptionFactory.makeWebServiceException(e);
        }
    }

    public static OperationDescription getOperationDescription(MessageContext mc) {
        OperationDescription op = null;
        op = mc.getOperationDescription();
        if (op == null) {
            if (log.isDebugEnabled()) {
                log.debug("No OperationDescription found on MessageContext, searching existing operations");
            }
            EndpointDescription ed = mc.getEndpointDescription();
            EndpointInterfaceDescription eid = ed.getEndpointInterfaceDescription();
            OperationDescription[] ops = eid.getDispatchableOperation(mc.getOperationName());
            if (ops == null || ops.length == 0) {
                throw ExceptionFactory.makeWebServiceException(Messages.getMessage("oprDescrErr", mc.getOperationName().toString()));
            }
            if (ops.length > 1) {
                throw ExceptionFactory.makeWebServiceException(Messages.getMessage("oprDescrErr1", mc.getOperationName().toString()));
            }
            op = ops[0];
            if (log.isDebugEnabled()) {
                log.debug("wsdl operation: " + op.getName());
                log.debug("   java method: " + op.getJavaMethodName());
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("An existing OperationDescription was found on the MessageContext.");
            }
        }
        return op;
    }
}
