package org.apache.tiles.servlet.context;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tiles.context.TilesRequestContext;
import org.apache.tiles.util.TilesIOException;

/**
 * Servlet-based implementation of the TilesApplicationContext interface.
 *
 * @version $Rev: 632818 $ $Date: 2008-03-02 20:48:05 +0100 (Sun, 02 Mar 2008) $
 */
public class ServletTilesRequestContext extends ServletTilesApplicationContext implements TilesRequestContext {

    /**
     * The request object to use.
     */
    private HttpServletRequest request;

    /**
     * The response object to use.
     */
    private HttpServletResponse response;

    /**
     * <p>The lazily instantiated <code>Map</code> of header name-value
     * combinations (immutable).</p>
     */
    private Map<String, String> header = null;

    /**
     * <p>The lazily instantitated <code>Map</code> of header name-values
     * combinations (immutable).</p>
     */
    private Map<String, String[]> headerValues = null;

    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-value.</p>
     */
    private Map<String, String> param = null;

    /**
     * <p>The lazily instantiated <code>Map</code> of request
     * parameter name-values.</p>
     */
    private Map<String, String[]> paramValues = null;

    /**
     * <p>The lazily instantiated <code>Map</code> of request scope
     * attributes.</p>
     */
    private Map<String, Object> requestScope = null;

    /**
     * <p>The lazily instantiated <code>Map</code> of session scope
     * attributes.</p>
     */
    private Map<String, Object> sessionScope = null;

    /**
     * Creates a new instance of ServletTilesRequestContext.
     *
     * @param servletContext The servlet context.
     * @param request The request object.
     * @param response The response object.
     */
    public ServletTilesRequestContext(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) {
        super(servletContext);
        initialize(request, response);
    }

    /** {@inheritDoc} */
    public Map<String, String> getHeader() {
        if ((header == null) && (request != null)) {
            header = new ServletHeaderMap(request);
        }
        return (header);
    }

    /** {@inheritDoc} */
    public Map<String, String[]> getHeaderValues() {
        if ((headerValues == null) && (request != null)) {
            headerValues = new ServletHeaderValuesMap(request);
        }
        return (headerValues);
    }

    /** {@inheritDoc} */
    public Map<String, String> getParam() {
        if ((param == null) && (request != null)) {
            param = new ServletParamMap(request);
        }
        return (param);
    }

    /** {@inheritDoc} */
    public Map<String, String[]> getParamValues() {
        if ((paramValues == null) && (request != null)) {
            paramValues = new ServletParamValuesMap(request);
        }
        return (paramValues);
    }

    /** {@inheritDoc} */
    public Map<String, Object> getRequestScope() {
        if ((requestScope == null) && (request != null)) {
            requestScope = new ServletRequestScopeMap(request);
        }
        return (requestScope);
    }

    /** {@inheritDoc} */
    public Map<String, Object> getSessionScope() {
        if ((sessionScope == null) && (request != null)) {
            sessionScope = new ServletSessionScopeMap(request);
        }
        return (sessionScope);
    }

    /** {@inheritDoc} */
    public void dispatch(String path) throws IOException {
        if (response.isCommitted() || ServletUtil.isForceInclude(request)) {
            include(path);
        } else {
            forward(path);
        }
    }

    /**
     * Forwards to a path.
     *
     * @param path The path to forward to.
     * @throws IOException If something goes wrong during the operation.
     */
    protected void forward(String path) throws IOException {
        RequestDispatcher rd = request.getRequestDispatcher(path);
        if (rd == null) {
            throw new IOException("No request dispatcher returned for path '" + path + "'");
        }
        try {
            rd.forward(request, response);
        } catch (ServletException ex) {
            throw wrapServletException(ex, "ServletException including path '" + path + "'.");
        }
    }

    /** {@inheritDoc} */
    public void include(String path) throws IOException {
        ServletUtil.setForceInclude(request, true);
        RequestDispatcher rd = request.getRequestDispatcher(path);
        if (rd == null) {
            throw new IOException("No request dispatcher returned for path '" + path + "'");
        }
        try {
            rd.include(request, response);
        } catch (ServletException ex) {
            throw wrapServletException(ex, "ServletException including path '" + path + "'.");
        }
    }

    /** {@inheritDoc} */
    public Locale getRequestLocale() {
        return request.getLocale();
    }

    /** {@inheritDoc} */
    public HttpServletRequest getRequest() {
        return request;
    }

    /** {@inheritDoc} */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * <p>Initialize (or reinitialize) this {@link ServletTilesRequestContext} instance
     * for the specified Servlet API objects.</p>
     *
     * @param request  The <code>HttpServletRequest</code> for this request
     * @param response The <code>HttpServletResponse</code> for this request
     */
    public void initialize(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * <p>Release references to allocated resources acquired in
     * <code>initialize()</code> of via subsequent processing.  After this
     * method is called, subsequent calls to any other method than
     * <code>initialize()</code> will return undefined results.</p>
     */
    public void release() {
        header = null;
        headerValues = null;
        param = null;
        paramValues = null;
        requestScope = null;
        sessionScope = null;
        request = null;
        response = null;
        super.release();
    }

    /** {@inheritDoc} */
    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }

    /**
     * Wraps a ServletException to create an IOException with the root cause if present.
     *
     * @param ex The exception to wrap.
     * @param message The message of the exception.
     * @return The wrapped exception.
     * @since 2.0.6
     */
    protected IOException wrapServletException(ServletException ex, String message) {
        IOException retValue;
        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            retValue = new TilesIOException(message, rootCause);
        } else {
            retValue = new TilesIOException(message, ex);
        }
        return retValue;
    }
}
