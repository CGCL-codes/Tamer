package org.pojosoft.catalog.web.gwt.server.servlet;

import org.apache.log4j.Logger;
import org.pojosoft.core.security.SecurityContext;
import org.pojosoft.core.security.support.SecurityContextHolder;
import org.pojosoft.catalog.web.gwt.client.authentication.AuthenticationService;
import org.pojosoft.ria.gwt.client.service.UserProfile;
import org.pojosoft.ria.gwt.server.support.I18nMessageUtils;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

public class SecurityFilter implements Filter {

    private String loginPage;

    private static final String LOGIN_PAGE = "login_page";

    static Logger logger = Logger.getLogger(SecurityFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
        if (filterConfig != null) {
            loginPage = filterConfig.getInitParameter(LOGIN_PAGE);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session != null) {
            logger.debug("sessionId=" + session.getId());
        } else {
            logger.debug("session null **");
        }
        String url = ((HttpServletRequest) request).getRequestURL().toString();
        logger.debug("\t" + url);
        String path = url.substring(url.indexOf(((HttpServletRequest) request).getContextPath()), url.length());
        if (path.equals(((HttpServletRequest) request).getContextPath() + "/home.jsp") || path.equals(((HttpServletRequest) request).getContextPath() + "/home.html") || path.equals(((HttpServletRequest) request).getContextPath() + "/login.jsp") || path.equals(((HttpServletRequest) request).getContextPath() + "/login_redirect.jsp")) {
            chain.doFilter(request, response);
        } else {
            SecurityContext securityContext = null;
            SecurityContextHolder.clearContext();
            if (session != null) securityContext = (SecurityContext) session.getAttribute(org.pojosoft.catalog.web.gwt.client.authentication.AuthenticationService.SECURITY_CONTEXT);
            if (securityContext != null) {
                logger.debug("securityContext for userId " + securityContext.getAuthentication().getPrincipleId() + " found.");
                SecurityContextHolder.setContext(securityContext);
                UserProfile userProfile = (UserProfile) session.getAttribute(AuthenticationService.USER_PROFILE);
                I18nMessageUtils.getInstance().setLocale(new Locale(userProfile.isoLanguageCode, userProfile.isoCountryCode));
            } else {
                logger.debug("securityContext not found. session timed out?");
            }
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }
}
