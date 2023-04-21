package org.fcrepo.server.security.xacml.pep.ws.operations;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fcrepo.common.Constants;
import org.fcrepo.server.security.xacml.pep.PEPException;
import org.fcrepo.server.security.xacml.util.LogUtil;
import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.RequestCtx;

/**
 * @author nishen@melcoe.mq.edu.au
 */
public class AddRelationshipHandler extends AbstractOperationHandler {

    private static final Logger logger = LoggerFactory.getLogger(AddRelationshipHandler.class);

    public AddRelationshipHandler() throws PEPException {
        super();
    }

    public RequestCtx handleResponse(MessageContext context) throws OperationHandlerException {
        return null;
    }

    public RequestCtx handleRequest(MessageContext context) throws OperationHandlerException {
        logger.debug("AddRelationshipHandler/handleRequest!");
        RequestCtx req = null;
        List<Object> oMap = null;
        String pid = null;
        try {
            oMap = getSOAPRequestObjects(context);
            logger.debug("Retrieved SOAP Request Objects");
        } catch (AxisFault af) {
            logger.error("Error obtaining SOAP Request Objects", af);
            throw new OperationHandlerException("Error obtaining SOAP Request Objects", af);
        }
        try {
            pid = (String) oMap.get(0);
        } catch (Exception e) {
            logger.error("Error obtaining parameters", e);
            throw new OperationHandlerException("Error obtaining parameters.", e);
        }
        logger.debug("Extracted SOAP Request Objects");
        Map<URI, AttributeValue> actions = new HashMap<URI, AttributeValue>();
        Map<URI, AttributeValue> resAttr = new HashMap<URI, AttributeValue>();
        try {
            if (pid != null && !"".equals(pid)) {
                resAttr.put(Constants.OBJECT.PID.getURI(), new StringAttribute(pid));
            }
            if (pid != null && !"".equals(pid)) {
                resAttr.put(new URI(XACML_RESOURCE_ID), new AnyURIAttribute(new URI(pid)));
            }
            actions.put(Constants.ACTION.ID.getURI(), new StringAttribute(Constants.ACTION.ADD_RELATIONSHIP.getURI().toASCIIString()));
            actions.put(Constants.ACTION.API.getURI(), new StringAttribute(Constants.ACTION.APIM.getURI().toASCIIString()));
            req = getContextHandler().buildRequest(getSubjects(context), actions, resAttr, getEnvironment(context));
            LogUtil.statLog(context.getUsername(), Constants.ACTION.ADD_RELATIONSHIP.getURI().toASCIIString(), pid, null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new OperationHandlerException(e.getMessage(), e);
        }
        return req;
    }
}
