package edu.hawaii.myisern.action;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.util.Log;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple security check that ensures that the user is logged in
 * before allowing access to any secured pages.
 * Adapted from Bugzooky application.
 *
 * @author Tim Fennell
 * @author Lisa Chen
 */
public class SecurityFilter implements Filter {

    protected static Log log = Log.getInstance(IsernActionBean.class);

    private static Set<String> publicUrls = new HashSet<String>();

    static {
        System.out.println("setting up security filter");
        publicUrls.add("/login.jsp");
        publicUrls.add("/Login.action");
        publicUrls.add("/Reload.action");
        publicUrls.add("/reload.jsp");
    }

    /**
   * do nothing.
   * @param filterConfig FilterConfig object
   * @throws ServletException on servlet error.  
   */
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
   * Allows authenticated users access to internal Urls.
   * 
   * @param servletRequest HTTP Servlet Request object.
   * @param servletResponse HTTP Servlet Response object.
   * @param filterChain filterChain object.
   * @throws IOException on io error.
   * @throws ServletException on servlet error. 
   */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("doFilter was called ");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getSession().getAttribute("user") == null && !isPublicResource(request)) {
            log.debug("doFilter REJECTED!");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            log.debug("doFilter Accepted!");
            filterChain.doFilter(request, response);
        }
    }

    /**
   * Method that checks the request to see if it is for a publicly accessible resource
   *
   * @param request User requested HTTP Servlet Request object.
   * @return True if the request is a publicly accessible resource.
   */
    protected boolean isPublicResource(HttpServletRequest request) {
        return publicUrls.contains(request.getServletPath());
    }

    /** Does nothing. */
    public void destroy() {
    }
}
