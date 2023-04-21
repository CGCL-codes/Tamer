package net.rossillo.spring.web.servlet.http;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides a Facebook request filter to wrap the current servlet request in
 * a Facebook request.
 * 
 * @author Scott Rossillo
 *
 * @see FacebookHttpServletRequest
 */
public class FacebookFilter implements Filter {

    private static final Log log = LogFactory.getLog(FacebookFilter.class);

    /**
	 * This implementation is empty.
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
    public void destroy() {
    }

    /**
	 * Wraps the given request in a Facebook HTTP servlet request.
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("Dumping request params:");
            for (Object param : request.getParameterMap().keySet()) {
                log.debug("\t%" + param + " = " + request.getParameter((String) param));
            }
        }
        FacebookHttpServletRequest fbRequest = new FacebookHttpServletRequest((HttpServletRequest) request);
        chain.doFilter(fbRequest, response);
    }

    /**
	 * This implementation is empty.
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
    public void init(FilterConfig config) throws ServletException {
    }
}
