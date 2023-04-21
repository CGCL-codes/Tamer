package org.dcm4chex.wado.web;

import javax.servlet.http.HttpServletRequest;
import org.dcm4chex.wado.common.BasicRequestObject;

/**
 * @author franz.willer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RequestObjectFactory {

    /**
	 * Returns an request object for given hjttp request.
	 * <p>
	 * Returns null, if the request is not either a WADO or IHE RID request.
	 * 
	 * @param request The http request.
	 * 
	 * @return
	 */
    public static BasicRequestObject getRequestObject(HttpServletRequest request) {
        BasicRequestObject reqObj = null;
        String reqType = request.getParameter("requestType");
        if (reqType == null) {
            reqType = request.getParameter("RT");
        }
        if (reqType == null) return null;
        if ("WADO".equalsIgnoreCase(reqType)) {
            return new WADORequestObjectImpl(request);
        } else {
            return null;
        }
    }
}
