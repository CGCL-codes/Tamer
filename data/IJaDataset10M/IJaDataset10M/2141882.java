package org.kablink.teaming.servlet.portal;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.kablink.teaming.domain.EntityIdentifier;
import org.kablink.teaming.domain.User;
import org.kablink.teaming.portal.PortalLogin;
import org.kablink.teaming.portletadapter.AdaptedPortletURL;
import org.kablink.teaming.util.SPropsUtil;
import org.kablink.teaming.web.WebKeys;
import org.kablink.teaming.web.servlet.SAbstractController;
import org.kablink.teaming.web.util.PermaLinkUtil;
import org.kablink.teaming.web.util.WebHelper;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

public class PortalLoginController extends SAbstractController {

    private static final String PORTAL_LOGIN_SUPPORTED_METHOD = "portal.login.supported.method";

    private static final String PORTAL_LOGIN_FORCENEW_ALLOWED = "portal.login.forcenew.allowed";

    private PortalLogin portalLogin;

    protected PortalLogin getPortalLogin() {
        return portalLogin;
    }

    public void setPortalLogin(PortalLogin portalLogin) {
        this.portalLogin = portalLogin;
    }

    protected ModelAndView handleRequestAfterValidation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (SPropsUtil.getBoolean("form.login.auth.disallowed", false)) return null;
        Map<String, Object> model = new HashMap();
        String view = WebKeys.VIEW_LOGIN_RETURN;
        if (("/" + WebKeys.SERVLET_PORTAL_LOGIN).equalsIgnoreCase(request.getPathInfo())) {
            if (!isInteractiveLoginAllowed(request)) {
                model.put(WebKeys.LOGIN_ERROR, WebKeys.LOGIN_ERROR_LOGINS_NOT_ALLOWED);
                return new ModelAndView(view, model);
            }
            String username = ServletRequestUtils.getStringParameter(request, "j_username", "");
            String password = ServletRequestUtils.getStringParameter(request, "j_password", "");
            String remember = ServletRequestUtils.getStringParameter(request, "remember");
            String url = ServletRequestUtils.getStringParameter(request, "spring-security-redirect", "");
            boolean forceNew = ServletRequestUtils.getBooleanParameter(request, "_forcenew", false);
            model.put(WebKeys.URL, url);
            if (!(forceNew && SPropsUtil.getBoolean(PORTAL_LOGIN_FORCENEW_ALLOWED, false))) {
                if (WebHelper.isUserLoggedIn(request) && username.equalsIgnoreCase(WebHelper.getRequiredUserName(request))) {
                    logger.info("The user " + username + " is already logged in.");
                    model.put(WebKeys.LOGIN_ERROR, WebKeys.LOGIN_ERROR_USER_ALREADY_LOGGED_IN);
                    return new ModelAndView(view, model);
                }
            }
            try {
                getPortalLogin().loginPortal(request, response, username, password, (remember != null && (remember.equalsIgnoreCase("on") || remember.equalsIgnoreCase("true")) ? true : false));
            } catch (Exception e) {
                model.put(WebKeys.LOGIN_ERROR, WebKeys.LOGIN_ERROR_LOGIN_FAILED);
                view = WebKeys.VIEW_LOGIN_RETRY;
                Thread.sleep(1000);
                return new ModelAndView(view, model);
            }
            User user = getProfileModule().findUserByName(username);
            model.put(WebKeys.USER_PRINCIPAL, user);
            String redirectUrl;
            if (!url.equals("")) {
                redirectUrl = url;
                redirectUrl = redirectUrl.replace(WebKeys.URL_USER_ID_PLACE_HOLDER, user.getId().toString());
            } else {
                if (request.getQueryString() != null) redirectUrl = request.getRequestURL().append("?").append(request.getQueryString()).toString(); else redirectUrl = request.getRequestURL().toString();
            }
            response.sendRedirect(redirectUrl);
            return null;
        } else {
            Long userId = null;
            try {
                userId = WebHelper.getRequiredUserId(request);
            } catch (Exception e) {
            }
            getPortalLogin().logoutPortal(request, response);
            view = WebKeys.VIEW_LOGOUT_RETURN;
            String url = ServletRequestUtils.getStringParameter(request, "spring-security-redirect", "");
            if (url.equals("")) {
                if (userId != null) {
                    url = PermaLinkUtil.getUserPermalink(request, userId.toString());
                }
            }
            model.put(WebKeys.URL, url);
        }
        return new ModelAndView(view, model);
    }

    protected boolean isInteractiveLoginAllowed(HttpServletRequest request) {
        String allowedMethod = SPropsUtil.getString(PORTAL_LOGIN_SUPPORTED_METHOD, "post");
        if (allowedMethod.equalsIgnoreCase("post")) {
            if ("post".equalsIgnoreCase(request.getMethod())) return true; else return false;
        } else if (allowedMethod.equalsIgnoreCase("all")) {
            return true;
        } else if (allowedMethod.equalsIgnoreCase("none")) {
            return false;
        } else {
            logger.warn("Illegal value " + allowedMethod + " for " + PORTAL_LOGIN_SUPPORTED_METHOD + " property");
            return false;
        }
    }
}
