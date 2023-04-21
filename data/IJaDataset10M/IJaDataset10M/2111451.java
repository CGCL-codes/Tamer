package org.fcrepo.server.security.xacml.pep.ws.operations;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fcrepo.server.security.xacml.pep.PEPException;
import com.sun.xacml.ctx.RequestCtx;

/**
 * @author nishen@melcoe.mq.edu.au
 */
public class DescribeUserHandler extends AbstractOperationHandler {

    private static final Logger logger = LoggerFactory.getLogger(DescribeUserHandler.class);

    public DescribeUserHandler() throws PEPException {
        super();
    }

    public RequestCtx handleResponse(MessageContext context) throws OperationHandlerException {
        try {
            String[] fedoraRoles = getUserRoles(context);
            if (fedoraRoles == null || fedoraRoles.length == 0) {
                return null;
            }
            Message message = context.getCurrentMessage();
            SOAPEnvelope envelope = message.getSOAPEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPHeaderElement roles = header.addHeaderElement(envelope.createName("roles", "drama", "http://drama.ramp.org.au/"));
            for (String fedoraRole : fedoraRoles) {
                SOAPElement role = roles.addChildElement(envelope.createName("role", "drama", "http://drama.ramp.org.au/"));
                role.addTextNode(fedoraRole);
            }
        } catch (Exception e) {
            logger.error("Error setting roles for user: " + e.getMessage(), e);
        }
        return null;
    }

    public RequestCtx handleRequest(MessageContext context) throws OperationHandlerException {
        return null;
    }
}
