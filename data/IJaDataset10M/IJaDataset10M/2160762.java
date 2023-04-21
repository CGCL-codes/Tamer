package net.winstone.filters.gzip;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import net.winstone.core.WinstoneRequest;

/**
 * A filter that checks if the request will accept a gzip encoded response, and
 * if so wraps the response in a gzip encoding response wrapper.
 * 
 * @author <a href="mailto:rick_knowles@hotmail.com">Rick Knowles</a>
 * @version $Id: GzipFilter.java,v 1.1 2005/08/24 06:43:34 rickknowles Exp $
 */
public class GzipFilter implements Filter {

    private static final String ACCEPT_ENCODING = "Accept-Encoding";

    private ServletContext context;

    @Override
    public void init(final FilterConfig config) throws ServletException {
        context = config.getServletContext();
    }

    @Override
    public void destroy() {
        context = null;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final Enumeration<String> headers = ((WinstoneRequest) request).getHeaders(GzipFilter.ACCEPT_ENCODING);
        boolean acceptsGzipEncoding = Boolean.FALSE;
        while (headers.hasMoreElements() && !acceptsGzipEncoding) {
            acceptsGzipEncoding = (headers.nextElement().indexOf("gzip") != -1);
        }
        if (acceptsGzipEncoding) {
            final GzipResponseWrapper encodedResponse = new GzipResponseWrapper((HttpServletResponse) response, context);
            chain.doFilter(request, encodedResponse);
            encodedResponse.close();
        } else {
            chain.doFilter(request, response);
        }
    }
}
