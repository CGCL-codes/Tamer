package net.sf.tacos.seam;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.jboss.seam.servlet.ContextualHttpServletRequest;
import org.jboss.seam.web.AbstractFilter;

/**
 *  Filter responsible for setup and teardown of Seam contexts.
 *
 * @author Igor Drobiazko
 */
public class TapestrySeamFilter extends AbstractFilter {

    /**
	 * {@inheritDoc}
	 */
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String pathInfo = httpRequest.getPathInfo();
        if (pathInfo != null && pathInfo.startsWith("/remoting")) {
            chain.doFilter(request, response);
        } else {
            new ContextualHttpServletRequest((HttpServletRequest) request) {

                @Override
                public void process() throws ServletException, IOException {
                    chain.doFilter(request, response);
                }
            }.run();
        }
    }
}
