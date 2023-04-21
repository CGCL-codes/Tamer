package be.fedict.eid.dss.ws;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.util.Set;

/**
 * Logging JAX-WS SOAP handler.
 * 
 * @author Frank Cornelis
 * 
 */
public class LoggingSoapHandler implements SOAPHandler<SOAPMessageContext> {

    private static final Log LOG = LogFactory.getLog(LoggingSoapHandler.class);

    public Set<QName> getHeaders() {
        return null;
    }

    public void close(MessageContext context) {
        LOG.debug("close");
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    public boolean handleMessage(SOAPMessageContext context) {
        LOG.debug("handle message");
        Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        LOG.debug("outbound message: " + outboundProperty);
        SOAPMessage soapMessage = context.getMessage();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            soapMessage.writeTo(output);
        } catch (Exception e) {
            LOG.error("SOAP error: " + e.getMessage());
        }
        LOG.debug("SOAP message: " + output.toString());
        return true;
    }
}
