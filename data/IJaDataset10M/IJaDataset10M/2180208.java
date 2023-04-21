package melcoe.fedora.pep.rest.objectshandlers;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import melcoe.fedora.pep.PEPException;
import melcoe.fedora.pep.rest.filters.AbstractFilter;
import melcoe.fedora.util.LogUtil;
import org.apache.log4j.Logger;
import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.RequestCtx;
import fedora.common.Constants;

/**
 * Handles the SetDatastreamVersionable operation.
 * 
 * @author nish.naidoo@gmail.com
 */
public class SetDatastreamVersionable extends AbstractFilter {

    private static Logger log = Logger.getLogger(SetDatastreamVersionable.class.getName());

    /**
     * Default constructor.
     * 
     * @throws PEPException
     */
    public SetDatastreamVersionable() throws PEPException {
        super();
    }

    public RequestCtx handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug(this.getClass().getName() + "/handleRequest!");
        }
        String path = request.getPathInfo();
        String[] parts = path.split("/");
        String pid = parts[1];
        String dsID = parts[3];
        String versionable = request.getParameter("versionable");
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
            if (versionable != null && !"".equals(versionable)) {
                resAttr.put(Constants.DATASTREAM.NEW_VERSIONABLE.getURI(), new StringAttribute(versionable));
            }
            actions.put(Constants.ACTION.ID.getURI(), new StringAttribute(Constants.ACTION.SET_DATASTREAM_VERSIONABLE.getURI().toASCIIString()));
            actions.put(Constants.ACTION.API.getURI(), new StringAttribute(Constants.ACTION.APIM.getURI().toASCIIString()));
            req = getContextHandler().buildRequest(getSubjects(request), actions, resAttr, getEnvironment(request));
            LogUtil.statLog(request.getRemoteUser(), Constants.ACTION.SET_DATASTREAM_VERSIONABLE.getURI().toASCIIString(), pid, dsID);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
        return req;
    }

    public RequestCtx handleResponse(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return null;
    }
}
