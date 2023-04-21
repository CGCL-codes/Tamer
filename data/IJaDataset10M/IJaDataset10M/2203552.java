package org.spice.servlet.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 *
 * @author Karthik Rajkumar
 * @version (included from RED-CHILLIES version)
 */
public abstract class Framework extends HttpServlet {

    private static final long serialVersionUID = -1773161479474731652L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Framework.class);

    /**
     * Called by the server (via the <code>service</code> method) to
     * allow a servlet to handle a GET request.
     * <p/>
     * <p>Overriding this method to support a GET request also
     * automatically supports an HTTP HEAD request. A HEAD
     * request is a GET request that returns no body in the
     * response, only the request header fields.
     * <p/>
     * <p>When overriding this method, read the request data,
     * write the response headers, get the response's writer or
     * output stream object, and finally, write the response data.
     * It's best to include content type and encoding. When using
     * a <code>PrintWriter</code> object to return the response,
     * set the content type before accessing the
     * <code>PrintWriter</code> object.
     * <p/>
     * <p>The servlet container must write the headers before
     * committing the response, because in HTTP the headers must be sent
     * before the response body.
     * <p/>
     * <p>Where possible, set the Content-Length header (with the
     * {@link javax.servlet.ServletResponse#setContentLength} method),
     * to allow the servlet container to use a persistent connection
     * to return its response to the client, improving performance.
     * The content length is automatically set if the entire response fits
     * inside the response buffer.
     * <p/>
     * <p>When using HTTP 1.1 chunked encoding (which means that the response
     * has a Transfer-Encoding header), do not set the Content-Length header.
     * <p/>
     * <p>The GET method should be safe, that is, without
     * any side effects for which users are held responsible.
     * For example, most form queries have no side effects.
     * If a client request is intended to change stored data,
     * the request should use some other HTTP method.
     * <p/>
     * <p>The GET method should also be idempotent, meaning
     * that it can be safely repeated. Sometimes making a
     * method safe also makes it idempotent. For example,
     * repeating queries is both safe and idempotent, but
     * buying a product online or modifying data is neither
     * safe nor idempotent.
     * <p/>
     * <p>If the request is incorrectly formatted, <code>doGet</code>
     * returns an HTTP "Bad Request" message.
     *
     * @param req  an {@link javax.servlet.http.HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link javax.servlet.http.HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     * @throws java.io.IOException            if an input or output error is
     *                                        detected when the servlet handles
     *                                        the GET request
     * @throws javax.servlet.ServletException if the request for the GET
     *                                        could not be handled
     * @see javax.servlet.ServletResponse#setContentType
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    /**
     * <p>Receives an HTTP HEAD request from the protected
     * <code>service</code> method and handles the
     * request.
     * The client sends a HEAD request when it wants
     * to see only the headers of a response, such as
     * Content-Type or Content-Length. The HTTP HEAD
     * method counts the output bytes in the response
     * to set the Content-Length header accurately.
     * <p/>
     * <p>If you override this method, you can avoid computing
     * the response body and just set the response headers
     * directly to improve performance. Make sure that the
     * <code>doHead</code> method you write is both safe
     * and idempotent (that is, protects itself from being
     * called multiple times for one HTTP HEAD request).
     * <p/>
     * <p>If the HTTP HEAD request is incorrectly formatted,
     * <code>doHead</code> returns an HTTP "Bad Request"
     * message.
     *
     * @param req  the request object that is passed
     *             to the servlet
     * @param resp the response object that the servlet
     *             uses to return the headers to the clien
     * @throws java.io.IOException            if an input or output error occurs
     * @throws javax.servlet.ServletException if the request for the HEAD
     *                                        could not be handled
     */
    @Override
    protected void doHead(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a POST request.
     * <p/>
     * The HTTP POST method allows the client to send
     * data of unlimited length to the Web server a single time
     * and is useful when posting information such as
     * credit card numbers.
     * <p/>
     * <p>When overriding this method, read the request data,
     * write the response headers, get the response's writer or output
     * stream object, and finally, write the response data. It's best
     * to include content type and encoding. When using a
     * <code>PrintWriter</code> object to return the response, set the
     * content type before accessing the <code>PrintWriter</code> object.
     * <p/>
     * <p>The servlet container must write the headers before committing the
     * response, because in HTTP the headers must be sent before the
     * response body.
     * <p/>
     * <p>Where possible, set the Content-Length header (with the
     * {@link javax.servlet.ServletResponse#setContentLength} method),
     * to allow the servlet container to use a persistent connection
     * to return its response to the client, improving performance.
     * The content length is automatically set if the entire response fits
     * inside the response buffer.
     * <p/>
     * <p>When using HTTP 1.1 chunked encoding (which means that the response
     * has a Transfer-Encoding header), do not set the Content-Length header.
     * <p/>
     * <p>This method does not need to be either safe or idempotent.
     * Operations requested through POST can have side effects for
     * which the user can be held accountable, for example,
     * updating stored data or buying items online.
     * <p/>
     * <p>If the HTTP POST request is incorrectly formatted,
     * <code>doPost</code> returns an HTTP "Bad Request" message.
     *
     * @param req  an {@link javax.servlet.http.HttpServletRequest} object that
     *             contains the request the client has made
     *             of the servlet
     * @param resp an {@link javax.servlet.http.HttpServletResponse} object that
     *             contains the response the servlet sends
     *             to the client
     * @throws java.io.IOException            if an input or output error is
     *                                        detected when the servlet handles
     *                                        the request
     * @throws javax.servlet.ServletException if the request for the POST
     *                                        could not be handled
     * @see javax.servlet.ServletOutputStream
     * @see javax.servlet.ServletResponse#setContentType
     */
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a PUT request.
     * <p/>
     * The PUT operation allows a client to
     * place a file on the server and is similar to
     * sending a file by FTP.
     * <p/>
     * <p>When overriding this method, leave intact
     * any content headers sent with the request (including
     * Content-Length, Content-Type, Content-Transfer-Encoding,
     * Content-Encoding, Content-Base, Content-Language, Content-Location,
     * Content-MD5, and Content-Range). If your method cannot
     * handle a content header, it must issue an error message
     * (HTTP 501 - Not Implemented) and discard the request.
     * For more information on HTTP 1.1, see RFC 2616
     * <a href="http://www.ietf.org/rfc/rfc2616.txt"></a>.
     * <p/>
     * <p>This method does not need to be either safe or idempotent.
     * Operations that <code>doPut</code> performs can have side
     * effects for which the user can be held accountable. When using
     * this method, it may be useful to save a copy of the
     * affected URL in temporary storage.
     * <p/>
     * <p>If the HTTP PUT request is incorrectly formatted,
     * <code>doPut</code> returns an HTTP "Bad Request" message.
     *
     * @param req  the {@link javax.servlet.http.HttpServletRequest} object that
     *             contains the request the client made of
     *             the servlet
     * @param resp the {@link javax.servlet.http.HttpServletResponse} object that
     *             contains the response the servlet returns
     *             to the client
     * @throws java.io.IOException            if an input or output error occurs
     *                                        while the servlet is handling the
     *                                        PUT request
     * @throws javax.servlet.ServletException if the request for the PUT
     *                                        cannot be handled
     */
    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a DELETE request.
     * <p/>
     * The DELETE operation allows a client to remove a document
     * or Web page from the server.
     * <p/>
     * <p>This method does not need to be either safe
     * or idempotent. Operations requested through
     * DELETE can have side effects for which users
     * can be held accountable. When using
     * this method, it may be useful to save a copy of the
     * affected URL in temporary storage.
     * <p/>
     * <p>If the HTTP DELETE request is incorrectly formatted,
     * <code>doDelete</code> returns an HTTP "Bad Request"
     * message.
     *
     * @param req  the {@link javax.servlet.http.HttpServletRequest} object that
     *             contains the request the client made of
     *             the servlet
     * @param resp the {@link javax.servlet.http.HttpServletResponse} object that
     *             contains the response the servlet returns
     *             to the client
     * @throws java.io.IOException            if an input or output error occurs
     *                                        while the servlet is handling the
     *                                        DELETE request
     * @throws javax.servlet.ServletException if the request for the
     *                                        DELETE cannot be handled
     */
    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a OPTIONS request.
     * <p/>
     * The OPTIONS request determines which HTTP methods
     * the server supports and
     * returns an appropriate header. For example, if a servlet
     * overrides <code>doGet</code>, this method returns the
     * following header:
     * <p/>
     * <p><code>Allow: GET, HEAD, TRACE, OPTIONS</code>
     * <p/>
     * <p>There's no need to override this method unless the
     * servlet implements new HTTP methods, beyond those
     * implemented by HTTP 1.1.
     *
     * @param req  the {@link javax.servlet.http.HttpServletRequest} object that
     *             contains the request the client made of
     *             the servlet
     * @param resp the {@link javax.servlet.http.HttpServletResponse} object that
     *             contains the response the servlet returns
     *             to the client
     * @throws java.io.IOException            if an input or output error occurs
     *                                        while the servlet is handling the
     *                                        OPTIONS request
     * @throws javax.servlet.ServletException if the request for the
     *                                        OPTIONS cannot be handled
     */
    @Override
    protected void doOptions(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    /**
     * Called by the server (via the <code>service</code> method)
     * to allow a servlet to handle a TRACE request.
     * <p/>
     * A TRACE returns the headers sent with the TRACE
     * request to the client, so that they can be used in
     * debugging. There's no need to override this method.
     *
     * @param req  the {@link javax.servlet.http.HttpServletRequest} object that
     *             contains the request the client made of
     *             the servlet
     * @param resp the {@link javax.servlet.http.HttpServletResponse} object that
     *             contains the response the servlet returns
     *             to the client
     * @throws java.io.IOException            if an input or output error occurs
     *                                        while the servlet is handling the
     *                                        TRACE request
     * @throws javax.servlet.ServletException if the request for the
     *                                        TRACE cannot be handled
     */
    @Override
    protected void doTrace(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    /**
     * Receives standard HTTP requests from the public
     * <code>service</code> method and dispatches
     * them to the <code>do</code><i>XXX</i> methods defined in
     * this class. This method is an HTTP-specific version of the
     * {@link javax.servlet.Servlet#service} method. There's no
     * need to override this method.
     *
     * @param req  the {@link javax.servlet.http.HttpServletRequest} object that
     *             contains the request the client made of
     *             the servlet
     * @param resp the {@link javax.servlet.http.HttpServletResponse} object that
     *             contains the response the servlet returns
     *             to the client
     * @throws java.io.IOException            if an input or output error occurs
     *                                        while the servlet is handling the
     *                                        HTTP request
     * @throws javax.servlet.ServletException if the HTTP request
     *                                        cannot be handled
     * @see javax.servlet.Servlet#service
     */
    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    /**
     *
     *  All the inherited methods are calls the method in order to make sure that all the callbacks are handled.
     *
     * @param request  the {@link javax.servlet.http.HttpServletRequest} object that
     *             contains the request the client made of
     *             the servlet
     * @param response the {@link javax.servlet.http.HttpServletResponse} object that
     *             contains the response the servlet returns
     *             to the client
     * @throws java.io.IOException            if an input or output error occurs
     *                                        while the servlet is handling the
     *                                        HTTP request
     * @throws javax.servlet.ServletException if the HTTP request
     *                                        cannot be handled
     *
     * @see SpiceServlet #dispatch
     *
     *
     */
    protected abstract void dispatch(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException;

    /**
     * Handles all the request from the client.
     *
     *
     * @param req  the {@link javax.servlet.http.HttpServletRequest} object that
     *             contains the request the client made of
     *             the servlet
     * @param resp the {@link javax.servlet.http.HttpServletResponse} object that
     *             contains the response the servlet returns
     *             to the client
     * @throws java.io.IOException            if an input or output error occurs
     *                                        while the servlet is handling the
     *                                        HTTP request
     * @throws javax.servlet.ServletException if the HTTP request
     *                                        cannot be handled
     */
    private void handleRequest(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        dispatch(req, resp);
    }

    /**
     * The method initializes the framework and set it to go.
     * @param servletConfig
     */
    protected abstract void initializeFramework(ServletConfig servletConfig) throws ServletException;

    /**
     * A convenience method which can be overridden so that there's no need
     * to call <code>super.init(config)</code>.
     * <p/>
     * <p>Instead of overriding {@link #init(javax.servlet.ServletConfig)}, simply override
     * this method and it will be called by
     * <code>GenericServlet.init(ServletConfig config)</code>.
     * The <code>ServletConfig</code> object can still be retrieved via {@link
     * #getServletConfig}.
     *
     * @throws javax.servlet.ServletException if an exception occurs that
     *                                        interrupts the servlet's
     *                                        normal operation
     */
    @Override
    public void init(final ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        LOGGER.info("Initializing the Framework Engine and the " + getServletName() + " is starting");
        initializeFramework(servletConfig);
    }
}
