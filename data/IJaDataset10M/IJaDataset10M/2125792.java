package org.torweg.pulse.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;

/**
 * filters the version prefix from the request URI to allow far future expires
 * headers for static resources.
 * <p>
 * see also: <tt>xsl/globals.xsl ($version.number)</tt>.
 * </p>
 * <p>
 * If an URI is not rewritten, the rest of the {@code FilterChain} is processed.
 * Since {@code FilterChain}s are only constructed upon first request, filtering
 * for rewritten URIs is aborted.
 * </p>
 * <p>
 * If you need to post process rewritten URIs, consider overwriting
 * {@link #dispatch(ServletRequest, ServletResponse, RequestRewriter)}.
 * </p>
 * 
 * @author Thomas Weber
 * @version $Revision: 1825 $
 */
public class VersionRewriteFilter extends AbstractPulseFilter {

    /**
	 * @see javax.servlet.Filter#destroy()
	 */
    public void destroy() {
        return;
    }

    /**
	 * does the URL rewriting.
	 * 
	 * @param servletRequest
	 *            the current servlet request
	 * @param servletResponse
	 *            the current servlet response
	 * @param chain
	 *            the filter chain
	 * @throws ServletException
	 *             on errors while filtering
	 * @throws IOException
	 *             on errors while filtering
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain chain) throws IOException, ServletException {
        StringBuilder bs = new StringBuilder(((HttpServletRequest) servletRequest).getContextPath()).append('/');
        if (bs.charAt(0) != '/') {
            bs.insert(0, '/');
        }
        RequestRewriter requestRewriter = new RequestRewriter(servletRequest, bs.toString());
        if (requestRewriter.isRewritten()) {
            dispatch(servletRequest, servletResponse, requestRewriter);
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
	 * actually takes care of the dispatching to the rewritten URI.
	 * <p>
	 * This method can be overwritten by subclassing {@code Filter}s.
	 * </p>
	 * <p>
	 * The original code of this method is no more than:<br/>
	 * {@code requestRewriter.getRequestDispatcher().forward(servletRequest,
	 * servletResponse);}
	 * </p>
	 * 
	 * @param servletRequest
	 *            the current servlet request
	 * @param servletResponse
	 *            the current servlet response
	 * @param requestRewriter
	 *            the current rewrite request
	 * @throws ServletException
	 *             on errors while dispatching
	 * @throws IOException
	 *             on errors while dispatching
	 */
    protected void dispatch(final ServletRequest servletRequest, final ServletResponse servletResponse, final RequestRewriter requestRewriter) throws ServletException, IOException {
        requestRewriter.getRequestDispatcher().forward(servletRequest, servletResponse);
    }

    /**
	 * initialises the filter.
	 * 
	 * @param conf
	 *            the filter config (which is ignored)
	 */
    public void init(final FilterConfig conf) {
        return;
    }

    /**
	 * Utility to rewrite a request.
	 */
    public static final class RequestRewriter {

        /**
		 * the base URI.
		 */
        private final String baseURI;

        /**
		 * the rewritten flag.
		 */
        private final boolean rewritten;

        /**
		 * the dispatch URI.
		 */
        private String dispatchURI = null;

        /**
		 * the URI of the {@code RequestRewriter}.
		 */
        private URI uri;

        /**
		 * the original servlet request.
		 */
        private final ServletRequest request;

        /**
		 * creates a new rewritten request.
		 * 
		 * @param req
		 *            the wrapped request
		 * @param base
		 *            the base URI
		 */
        public RequestRewriter(final ServletRequest req, final String base) {
            super();
            this.baseURI = base;
            this.request = req;
            String originalRequestURI;
            try {
                originalRequestURI = ((HttpServletRequest) req).getRequestURI();
            } catch (Exception e) {
                originalRequestURI = (String) req.getAttribute("javax.servlet.include.request_uri");
            }
            try {
                URI requestURI = new URI(originalRequestURI);
                if (requestURI.getRawPath().startsWith(this.baseURI + Lifecycle.getVersioningPrefix())) {
                    this.rewritten = true;
                    rewrite(requestURI);
                } else {
                    this.rewritten = false;
                    this.uri = new URI(originalRequestURI);
                }
            } catch (URISyntaxException e) {
                throw new PulseException(e);
            }
        }

        /**
		 * does the actual rewriting.
		 * 
		 * @param sourceURI
		 *            the source URI to be rewritten
		 */
        private void rewrite(final URI sourceURI) {
            StringBuilder rewrittenURI = new StringBuilder();
            StringBuilder dispatchBuilder = new StringBuilder();
            if (sourceURI.getScheme() != null) {
                rewrittenURI.append(sourceURI.getScheme()).append(':');
            }
            if (sourceURI.getRawAuthority() != null) {
                rewrittenURI.append("//").append(sourceURI.getRawAuthority());
            }
            if (sourceURI.getRawPath() != null) {
                String rawPath = sourceURI.getRawPath().replaceFirst(this.baseURI + Lifecycle.getVersioningPrefix() + ".*?/", this.baseURI);
                rewrittenURI.append(rawPath);
                dispatchBuilder.append(rawPath.substring(this.baseURI.length() - 1));
            }
            if (sourceURI.getRawQuery() != null) {
                rewrittenURI.append('?').append(sourceURI.getRawQuery());
                dispatchBuilder.append('?').append(sourceURI.getRawQuery());
            }
            if (sourceURI.getRawFragment() != null) {
                rewrittenURI.append('#').append(sourceURI.getRawFragment());
                dispatchBuilder.append('#').append(sourceURI.getRawFragment());
            }
            this.dispatchURI = dispatchBuilder.toString();
            try {
                this.uri = new URI(rewrittenURI.toString());
            } catch (URISyntaxException e) {
                throw new PulseException(e);
            }
        }

        /**
		 * returns the request dispatcher for the rewritten URI.
		 * 
		 * @return the request dispatcher
		 */
        public RequestDispatcher getRequestDispatcher() {
            return this.request.getRequestDispatcher(this.dispatchURI);
        }

        /**
		 * returns whether the URI of the request has actually been rewritten.
		 * 
		 * @return {@code true}, if and only if the URI of the request has been
		 *         rewritten. Otherwise {@code false}.
		 */
        public boolean isRewritten() {
            return this.rewritten;
        }

        /**
		 * returns the rewritten URI.
		 * 
		 * @return the rewritten URI
		 */
        public URI getRewrittenURI() {
            return this.uri;
        }
    }
}
