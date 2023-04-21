package org.lightcommons.web.auth;

import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.lightcommons.web.auth.impl.PropertiesAuthConfig;

/**
 * SecurityFilter,通过Spring创建Configuration对象<br>
 * 需要在classpath中加入security.xml,构建一个完整的Configuration对象,Bean的Name必须时configuration
 * @author <a href="mailto:gl@kindsoft.cn">桂健雄</a>
 * @since 2007-4-26
 */
public class AuthFilter implements Filter {

    public final void destroy() {
    }

    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            processRequest((HttpServletRequest) request, (HttpServletResponse) response, chain);
        } finally {
            AuthManager.cleanThread();
        }
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        AuthManager.setCurrentSession(req.getSession());
        String servletPath = req.getServletPath();
        if (AuthManager.getAuthConfig().isIgnore(servletPath)) {
            chain.doFilter(req, res);
            return;
        }
        Authentication auth = AuthManager.getAuthentication();
        if (auth != null && auth.urlAccessible(servletPath)) {
            chain.doFilter(req, res);
        } else {
            if (auth == null || auth.getUser() == null) {
                String lasturl = servletPath;
                String q = req.getQueryString();
                if (q != null && !"".equals(q)) {
                    lasturl = lasturl + "?" + q;
                }
                res.sendRedirect(req.getContextPath() + AuthManager.getAuthConfig().getLoginPage() + "?lasturl=" + URLEncoder.encode(lasturl, "ISO-8859-1"));
            } else {
                res.sendRedirect(req.getContextPath() + AuthManager.getAuthConfig().getDeniedPage());
            }
        }
    }

    public final void init(FilterConfig config) throws ServletException {
        AuthManager.init(new PropertiesAuthConfig("auth.properties"));
    }
}
