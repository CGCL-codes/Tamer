package org.kablink.teaming.ssfs.wck;

import javax.servlet.http.HttpServletRequest;
import org.apache.slide.simple.authentication.SessionAuthenticationManager;
import org.kablink.teaming.asmodule.zonecontext.ZoneContextHolder;
import org.kablink.teaming.ssfs.CrossContextConstants;
import org.kablink.teaming.ssfs.web.crosscontext.DispatchClient;
import org.kablink.teaming.web.util.AttributesAndParamsOnlyServletRequest;
import org.kablink.teaming.web.util.NullServletResponse;

public class AuthenticationManager implements SessionAuthenticationManager {

    public Object getAuthenticationSession(String userName, String password) throws Exception {
        AttributesAndParamsOnlyServletRequest req = new AttributesAndParamsOnlyServletRequest(Util.getSsfContextPath());
        setAttributes(req, ZoneContextHolder.getServerName(), userName, password);
        NullServletResponse res = new NullServletResponse();
        DispatchClient.doDispatch(req, res);
        String errorCode = (String) req.getAttribute(CrossContextConstants.ERROR);
        if (errorCode != null) {
            String errorMessage = (String) req.getAttribute(CrossContextConstants.ERROR_MESSAGE);
            throw new Exception(errorMessage);
        } else {
            return userName;
        }
    }

    public Object getAuthenticationSession(String user) throws Exception {
        return user;
    }

    public void closeAuthenticationSession(Object session) throws Exception {
    }

    protected void setAttributes(HttpServletRequest req, String serverName, String userName, String password) {
        req.setAttribute(CrossContextConstants.OPERATION, CrossContextConstants.OPERATION_AUTHENTICATE);
        if (serverName != null) req.setAttribute(CrossContextConstants.SERVER_NAME, serverName);
        req.setAttribute(CrossContextConstants.USER_NAME, userName);
        req.setAttribute(CrossContextConstants.PASSWORD, password);
    }
}
