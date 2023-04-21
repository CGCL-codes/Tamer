package org.fcrepo.server.security.xacml.pep.ws.operations;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.StringAttribute;
import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.message.RPCElement;
import org.apache.axis.message.RPCParam;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.message.SOAPEnvelope;
import org.fcrepo.common.Constants;
import org.fcrepo.server.security.xacml.pep.ContextHandler;
import org.fcrepo.server.security.xacml.pep.ContextHandlerImpl;
import org.fcrepo.server.security.xacml.pep.PEPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the AbstractHandler class which provides generic functionality for
 * all operation handlers. All operation handlers should extend this class.
 *
 * @author nishen@melcoe.mq.edu.au
 */
public abstract class AbstractOperationHandler implements OperationHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractOperationHandler.class);

    protected static final String XACML_RESOURCE_ID = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";

    protected static final String SUBJECT_ID = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";

    protected static final String FEDORA_ROLE = "urn:fedora:names:fedora:2.1:subject:role";

    private static ContextHandler contextHandlerImpl;

    /**
     * Default constructor that obtains an instance of the ContextHandler.
     *
     * @throws PEPException
     */
    public AbstractOperationHandler() throws PEPException {
        contextHandlerImpl = ContextHandlerImpl.getInstance();
    }

    /**
     * Extracts the request parameters as objects from the context.
     *
     * @param context
     *        the message context.
     * @return list of Objects
     * @throws AxisFault
     */
    protected List<Object> getSOAPRequestObjects(MessageContext context) throws AxisFault {
        List<Object> result = new ArrayList<Object>();
        OperationDesc operation = context.getOperation();
        Message message = context.getRequestMessage();
        SOAPEnvelope envelope = message.getSOAPEnvelope();
        SOAPBodyElement body = envelope.getFirstBody();
        if (body instanceof RPCElement) {
            List params = null;
            try {
                params = ((RPCElement) body).getParams();
                logger.debug("Number of params: " + params.size());
            } catch (Exception e) {
                logger.error("Problem obtaining params", e);
                throw AxisFault.makeFault(e);
            }
            if (params.size() > 0) {
                logger.info("Operation returnType: " + operation.getReturnType().getNamespaceURI() + " " + operation.getReturnType().getLocalPart());
                for (int x = 0; x < params.size(); x++) {
                    RPCParam param = (RPCParam) params.get(x);
                    result.add(param.getObjectValue());
                    logger.info("Obtained object: (" + x + ") " + param.getQName().toString());
                }
            }
        }
        return result;
    }

    /**
     * Extracts the return object from the context.
     *
     * @param context
     *        the message context.
     * @return the return object for the message.
     * @throws AxisFault
     */
    protected Object getSOAPResponseObject(MessageContext context) throws AxisFault {
        Object result = null;
        OperationDesc operation = context.getOperation();
        Message message = context.getPastPivot() ? context.getResponseMessage() : context.getRequestMessage();
        SOAPEnvelope envelope = message.getSOAPEnvelope();
        SOAPBodyElement body = envelope.getFirstBody();
        if (body instanceof RPCElement) {
            List params = null;
            try {
                params = ((RPCElement) body).getParams();
                if (logger.isDebugEnabled()) {
                    logger.debug("Number of params: " + params.size());
                }
            } catch (Exception e) {
                logger.error("Problem obtaining params", e);
                throw AxisFault.makeFault(e);
            }
            if (params != null && params.size() > 0) {
                logger.info("Operation returnType: " + operation.getReturnType().getNamespaceURI() + " " + operation.getReturnType().getLocalPart());
                for (int x = 0; result == null && x < params.size(); x++) {
                    RPCParam param = (RPCParam) params.get(x);
                    if (param.getQName().equals(operation.getReturnQName())) {
                        logger.info("Obtained object: (" + x + ") " + param.getQName().toString());
                        result = param.getObjectValue();
                    }
                }
            }
        }
        if (result == null) {
            throw AxisFault.makeFault(new Exception("Could not obtain Object from SOAP Response"));
        }
        return result;
    }

    /**
     * Sets the request parameters for a request.
     *
     * @param context
     *        the message context
     * @param params
     *        list of parameters to set in order
     * @throws AxisFault
     */
    protected void setSOAPRequestObjects(MessageContext context, List<RPCParam> params) throws AxisFault {
        Message message = context.getPastPivot() ? context.getResponseMessage() : context.getRequestMessage();
        SOAPEnvelope envelope = message.getSOAPEnvelope();
        SOAPBodyElement body = envelope.getFirstBody();
        try {
            body.removeContents();
            for (RPCParam p : params) {
                body.addChild(p);
            }
        } catch (Exception e) {
            logger.error("Problem changing SOAP message contents", e);
            throw AxisFault.makeFault(e);
        }
    }

    /**
     * Sets the return object for a response as the param.
     *
     * @param context
     *        the message context
     * @param param
     *        the object to set as the return object
     * @throws AxisFault
     */
    protected void setSOAPResponseObject(MessageContext context, RPCParam param) throws AxisFault {
        Message message = context.getPastPivot() ? context.getResponseMessage() : context.getRequestMessage();
        SOAPEnvelope envelope = message.getSOAPEnvelope();
        SOAPBodyElement body = envelope.getFirstBody();
        try {
            body.removeContents();
            body.addChild(param);
        } catch (Exception e) {
            logger.error("Problem changing SOAP message contents", e);
            throw AxisFault.makeFault(e);
        }
    }

    /**
     * Sets the return object for a response as a sequence of params.
     *
     * @param context
     *        the message context
     * @param param
     *        the object to set as the return object
     * @throws AxisFault
     */
    protected void setSOAPResponseObject(MessageContext context, RPCParam[] params) throws AxisFault {
        Message message = context.getPastPivot() ? context.getResponseMessage() : context.getRequestMessage();
        SOAPEnvelope envelope = message.getSOAPEnvelope();
        SOAPBodyElement body = envelope.getFirstBody();
        try {
            body.removeContents();
            if (params != null) {
                for (RPCParam param : params) {
                    body.addChild(param);
                }
            }
        } catch (Exception e) {
            logger.error("Problem changing SOAP message contents", e);
            throw AxisFault.makeFault(e);
        }
    }

    /**
     * Extracts the list of Subjects from the given context.
     *
     * @param context
     *        the message context
     * @return a list of Subjects
     * @throws AxisFault
     */
    protected List<Map<URI, List<AttributeValue>>> getSubjects(MessageContext context) throws AxisFault {
        List<Map<URI, List<AttributeValue>>> subjects = new ArrayList<Map<URI, List<AttributeValue>>>();
        if (context.getUsername() == null || "".equals(context.getUsername().trim())) {
            return subjects;
        }
        String[] fedoraRole = getUserRoles(context);
        Map<URI, List<AttributeValue>> subAttr = null;
        List<AttributeValue> attrList = null;
        try {
            subAttr = new HashMap<URI, List<AttributeValue>>();
            attrList = new ArrayList<AttributeValue>();
            attrList.add(new StringAttribute(context.getUsername()));
            subAttr.put(Constants.SUBJECT.LOGIN_ID.getURI(), attrList);
            if (fedoraRole != null && fedoraRole.length > 0) {
                attrList = new ArrayList<AttributeValue>();
                for (String r : fedoraRole) {
                    attrList.add(new StringAttribute(r));
                }
                subAttr.put(new URI(FEDORA_ROLE), attrList);
            }
            subjects.add(subAttr);
            subAttr = new HashMap<URI, List<AttributeValue>>();
            attrList = new ArrayList<AttributeValue>();
            attrList.add(new StringAttribute(context.getUsername()));
            subAttr.put(Constants.SUBJECT.USER_REPRESENTED.getURI(), attrList);
            if (fedoraRole != null && fedoraRole.length > 0) {
                attrList = new ArrayList<AttributeValue>();
                for (String r : fedoraRole) {
                    attrList.add(new StringAttribute(r));
                }
                subAttr.put(new URI(FEDORA_ROLE), attrList);
            }
            subjects.add(subAttr);
            subAttr = new HashMap<URI, List<AttributeValue>>();
            attrList = new ArrayList<AttributeValue>();
            attrList.add(new StringAttribute(context.getUsername()));
            subAttr.put(new URI(SUBJECT_ID), attrList);
            if (fedoraRole != null && fedoraRole.length > 0) {
                attrList = new ArrayList<AttributeValue>();
                for (String r : fedoraRole) {
                    attrList.add(new StringAttribute(r));
                }
                subAttr.put(new URI(FEDORA_ROLE), attrList);
            }
            subjects.add(subAttr);
        } catch (URISyntaxException use) {
            logger.error(use.getMessage(), use);
            throw AxisFault.makeFault(use);
        }
        return subjects;
    }

    /**
     * Obtains a list of environment Attributes.
     *
     * @param context
     *        the message context
     * @return list of environment Attributes
     */
    protected Map<URI, AttributeValue> getEnvironment(MessageContext context) {
        Map<URI, AttributeValue> envAttr = new HashMap<URI, AttributeValue>();
        String ip = (String) context.getProperty("remoteaddr");
        if (ip != null && !"".equals(ip)) {
            envAttr.put(Constants.HTTP_REQUEST.CLIENT_IP_ADDRESS.getURI(), new StringAttribute(ip));
        }
        return envAttr;
    }

    /**
     * @return the Context Handler
     */
    protected ContextHandler getContextHandler() {
        return contextHandlerImpl;
    }

    /**
     * Returns the roles that the user has.
     *
     * @param context
     *        the message context
     * @return a String array of roles
     */
    @SuppressWarnings("unchecked")
    protected String[] getUserRoles(MessageContext context) {
        HttpServletRequest request = (HttpServletRequest) context.getProperty("transport.http.servletRequest");
        Map<String, Set<String>> reqAttr = null;
        reqAttr = (Map<String, Set<String>>) request.getAttribute("FEDORA_AUX_SUBJECT_ATTRIBUTES");
        if (reqAttr == null) {
            return null;
        }
        Set<String> fedoraRoles = reqAttr.get("fedoraRole");
        if (fedoraRoles == null || fedoraRoles.size() == 0) {
            return null;
        }
        String[] fedoraRole = fedoraRoles.toArray(new String[fedoraRoles.size()]);
        return fedoraRole;
    }
}
