package com.acv.webapp.filter;

import java.io.IOException;
import java.sql.Timestamp;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.log4j.Logger;
import com.acv.dao.common.Constants;
import com.acv.dao.security.UserDetailsImplementation;

/**
 * Content Date Setter Filter
 * @author arnaud
 *
 * This filter check if the content date (timestamp) is already set (by ContentViewerServlet for "simulation")
 * if it is not set it means we are in a normal situation so we can set it with the current date (timestamp)
 *
 */
public class ContentDateSetterFilter implements Filter {

    private static final Logger log = Logger.getLogger(ContentDateSetterFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();
        Timestamp existingTimestamp = (Timestamp) session.getAttribute(Constants.CONTENT_TIMESTAMP);
        SecurityContext sc = SecurityContextHolder.getContext();
        if (existingTimestamp != null) {
            if (sc != null && sc.getAuthentication() != null) {
                ((UserDetailsImplementation) sc.getAuthentication().getPrincipal()).setBrowsingDate(existingTimestamp);
                log.debug("ContentDateSetterFilter : The browsing date has been successfully set on the security context");
            } else {
                log.warn("ContentDateSetterFilter : The browsing date hasn't been set on the security context");
            }
            if (log.isDebugEnabled()) log.debug("Content timestamp already set with value : " + existingTimestamp.toString());
        } else {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (sc != null && sc.getAuthentication() != null) {
                ((UserDetailsImplementation) sc.getAuthentication().getPrincipal()).setBrowsingDate(now);
                log.debug("ContentDateSetterFilter : The browsing date has been successfully set on the security context");
            } else {
                log.warn("ContentDateSetterFilter : The browsing date hasn't been set on the security context");
            }
            session.setAttribute(Constants.CONTENT_TIMESTAMP, now);
            log.debug("Content timestamp was NOT already set.");
            if (log.isDebugEnabled()) log.debug("It is set now with value : " + now.toString());
        }
        filterChain.doFilter(request, response);
    }

    public void destroy() {
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}
