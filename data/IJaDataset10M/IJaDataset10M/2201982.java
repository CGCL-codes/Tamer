package org.fcrepo.server.security.xacml.pep.rest.objectshandlers;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.RequestCtx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fcrepo.common.Constants;
import org.fcrepo.server.security.xacml.pdp.data.FedoraPolicyStore;
import org.fcrepo.server.security.xacml.pep.PEPException;
import org.fcrepo.server.security.xacml.pep.rest.filters.AbstractFilter;
import org.fcrepo.server.security.xacml.util.LogUtil;

/**
 * Handles the AddDatastream operation.
 *
 * @author nish.naidoo@gmail.com
 */
public class AddDatastream extends AbstractFilter {

    private static final Logger logger = LoggerFactory.getLogger(AddDatastream.class);

    /**
     * Default constructor.
     *
     * @throws PEPException
     */
    public AddDatastream() throws PEPException {
        super();
    }

    public RequestCtx handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + "/handleRequest!");
        }
        String path = request.getPathInfo();
        String[] parts = path.split("/");
        String pid = parts[1];
        String dsID = parts[3];
        String mimeType = request.getParameter("mimeType");
        String formatURI = request.getParameter("formatURI");
        String dsLocation = request.getParameter("dsLocation");
        String controlGroup = request.getParameter("controlGroup");
        String dsState = request.getParameter("dsState");
        String checksumType = request.getParameter("checksumType");
        String checksum = request.getParameter("checksum");
        RequestCtx req = null;
        Map<URI, AttributeValue> actions = new HashMap<URI, AttributeValue>();
        Map<URI, AttributeValue> resAttr = new HashMap<URI, AttributeValue>();
        try {
            if (pid != null && !"".equals(pid)) {
                resAttr.put(Constants.OBJECT.PID.getURI(), new StringAttribute(pid));
            }
            if (pid != null && !"".equals(pid)) {
                resAttr.put(new URI(XACML_RESOURCE_ID), new AnyURIAttribute(new URI(pid)));
            }
            if (dsID != null && !"".equals(dsID)) {
                resAttr.put(Constants.DATASTREAM.ID.getURI(), new StringAttribute(dsID));
            }
            if (mimeType != null && !"".equals(mimeType)) {
                resAttr.put(Constants.DATASTREAM.NEW_MIME_TYPE.getURI(), new StringAttribute(mimeType));
            }
            if (formatURI != null && !"".equals(formatURI)) {
                resAttr.put(Constants.DATASTREAM.NEW_FORMAT_URI.getURI(), new AnyURIAttribute(new URI(formatURI)));
            }
            if (dsLocation != null && !"".equals(dsLocation)) {
                resAttr.put(Constants.DATASTREAM.NEW_LOCATION.getURI(), new AnyURIAttribute(new URI(dsLocation)));
            }
            if (controlGroup != null && !"".equals(controlGroup)) {
                resAttr.put(Constants.DATASTREAM.NEW_CONTROL_GROUP.getURI(), new StringAttribute(controlGroup));
            }
            if (dsState != null && !"".equals(dsState)) {
                resAttr.put(Constants.DATASTREAM.NEW_STATE.getURI(), new StringAttribute(dsState));
            }
            if (checksumType != null && !"".equals(checksumType)) {
                resAttr.put(Constants.DATASTREAM.NEW_CHECKSUM_TYPE.getURI(), new StringAttribute(checksumType));
            }
            if (checksum != null && !"".equals(checksum)) {
                resAttr.put(Constants.DATASTREAM.NEW_CHECKSUM.getURI(), new StringAttribute(checksum));
            }
            actions.put(Constants.ACTION.ID.getURI(), new StringAttribute(Constants.ACTION.ADD_DATASTREAM.getURI().toASCIIString()));
            actions.put(Constants.ACTION.API.getURI(), new StringAttribute(Constants.ACTION.APIM.getURI().toASCIIString()));
            if (dsID != null && dsID.equals(FedoraPolicyStore.POLICY_DATASTREAM)) {
                actions.put(Constants.ACTION.ID.getURI(), new StringAttribute(Constants.ACTION.MANAGE_POLICIES.getURI().toASCIIString()));
            }
            req = getContextHandler().buildRequest(getSubjects(request), actions, resAttr, getEnvironment(request));
            LogUtil.statLog(request.getRemoteUser(), Constants.ACTION.ADD_DATASTREAM.getURI().toASCIIString(), pid, dsID);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
        return req;
    }

    public RequestCtx handleResponse(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return null;
    }
}
