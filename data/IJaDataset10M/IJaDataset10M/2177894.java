package com.google.code.sapien.action;

import java.io.Serializable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.code.sapien.ActionConstants;
import com.google.code.sapien.service.UserService;
import com.google.inject.Inject;
import com.opensymphony.xwork2.Action;

/**
 * Logout Action, used to allow the user to log out.
 * @author Adam
 * @version $Id$
 * 
 * Created on Apr 30, 2009 at 8:52:49 PM 
 */
@Results({ @Result(name = "success", params = { "actionName", "index", "namespace", "/" }, type = "redirectAction") })
public class LogoutAction implements ServletRequestAware, ServletResponseAware, Serializable {

    /**
     * The <code>Logger</code> is used by the application to generate a log messages.
     */
    private static final Logger LOG = LoggerFactory.getLogger(LogoutAction.class);

    /**
	 * Serial Version UID.
	 */
    private static final long serialVersionUID = 2758458704905635256L;

    /**
     * Http Servlet Request, used to invalidate the session.
     */
    private HttpServletRequest request;

    /**
     * Http Servlet Reponse, used to remove the persistence cookie if it exists.
     */
    private HttpServletResponse response;

    /**
	 * The user service.
	 */
    private final UserService userService;

    /**
	 * Constructs a logout action.
	 * @param userService The user service.
	 */
    @Inject
    public LogoutAction(final UserService userService) {
        super();
        this.userService = userService;
    }

    /**
     * Invalidates the session so the user is logged out.
     * @return Action.SUCCESS.
     * @throws Exception If an error occurs invalidating the session.
     */
    public String execute() throws Exception {
        try {
            String encryptedMessage = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (ActionConstants.USER_PERSISTENT_LOGIN.equals(cookie.getName())) {
                        encryptedMessage = cookie.getValue();
                        break;
                    }
                }
            }
            if (StringUtils.isNotBlank(encryptedMessage)) {
                userService.removePersistentLogin(encryptedMessage);
                Cookie cookie = new Cookie(ActionConstants.USER_PERSISTENT_LOGIN, null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            LOG.debug("Invalidating Session");
            request.getSession().invalidate();
        } catch (IllegalStateException e) {
            LOG.warn("LogoutAction.execute() caught an IllegalStateException", e);
        }
        return Action.SUCCESS;
    }

    /**
	 * {@inheritDoc}
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }

    /**
	 * {@inheritDoc}
	 * @see org.apache.struts2.interceptor.ServletResponseAware#setServletResponse(
	 * javax.servlet.http.HttpServletResponse)
	 */
    public void setServletResponse(final HttpServletResponse response) {
        this.response = response;
    }
}
