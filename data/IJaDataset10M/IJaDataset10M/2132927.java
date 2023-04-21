package org.tolven.shiro.web.servlet;

import java.io.IOException;
import javax.naming.InitialContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.tolven.naming.TolvenContext;
import org.tolven.naming.WebContext;

/**
 * This filter requires the user to have already been authenticated before passing through it
 * 
 * @author Joseph Isaac
 *
 */
public class WSAuthorizationFilter extends AuthorizationFilter {

    public WSAuthorizationFilter() {
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        try {
            InitialContext ictx = new InitialContext();
            TolvenContext tolvenContext = (TolvenContext) ictx.lookup("tolvenContext");
            String webContextId = (String) ictx.lookup("java:comp/env/webContextId");
            WebContext webContext = (WebContext) tolvenContext.getWebContext(webContextId);
            String loginPath = webContext.getWsLoginPath();
            if (loginPath == null) {
                throw new RuntimeException("No WebContext loginPath found for contextPath: " + webContextId);
            }
            setLoginUrl(loginPath);
            return super.onPreHandle(request, response, mappedValue);
        } catch (Exception ex) {
            throw new RuntimeException("Could not get loginUrl", ex);
        }
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        if (isLoginRequest(servletRequest, servletResponse)) {
            return true;
        } else {
            Subject subject = getSubject(servletRequest, servletResponse);
            return subject.isAuthenticated();
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        WebUtils.toHttp(servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
