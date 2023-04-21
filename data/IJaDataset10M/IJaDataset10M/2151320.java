package org.gomba;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Servlet forwards HTTP requests to other servlets. Target servlets are
 * mapped to HTTP request methods: GET, POST, PUT, DELETE. This servlet is
 * useful to build read/write/update/delete web services by compositing servlets
 * that manage only a single operation.
 * 
 * <p>
 * Init params:
 * <dl>
 * <dt>GET</dt>
 * <dd>The name of the servlet to handle GET requests. This method is normally
 * used for read (SELECT in SQL) operations. (Optional)</dd>
 * <dt>POST</dt>
 * <dd>The name of the servlet to handle POST requests. This method is normally
 * used for creation (INSERT in SQL) operations. (Optional)</dd>
 * <dt>PUT</dt>
 * <dd>The name of the servlet to handle PUT requests. This method is normally
 * used for update (UPDATE in SQL) operations. (Optional)</dd>
 * <dt>DELETE</dt>
 * <dd>The name of the servlet to handle DELETE requests. This method is
 * obviously used for deletion (DELETE in SQL) operations. (Optional)</dd>
 * </dl>
 * </p>
 * 
 * <p>
 * Design note. While this 'dispatcher' approach may seem to break the Servlet
 * contract by splitting the processing of HTTP methods among multiple servlets,
 * we designed this way with the explicit purpose of remaining inside the
 * Servlet API and not creating our own framework inside the Servlet framework.
 * The benefits of this design are:
 * <ul>
 * <li>All configuration is done through standard mechanisms (web.xml).</li>
 * <li>A web service can smoothly grow from a read-only service (which is
 * fairly easy to configure: 1 servlet, 1 servlet-mapping), to a more
 * sophisticated read/write/update/delete service (which involves the
 * configuration of 4 servlets and 1 servlet-mapping).</li>
 * </ul>
 * An alternative design would be having a single Servlet handling all HTTP
 * methods and invoking a "service" mapped to it. This would require:
 * <ul>
 * <li>Creating our own "service" abstraction.</li>
 * <li>Having our own XML configuration file besides the web.xml</li>
 * </ul>
 * Which we do not like at all.
 * </p>
 * 
 * @author Flavio Tordini
 * @version $Id: DispatcherServlet.java,v 1.3 2004/09/16 09:23:16 flaviotordini Exp $
 */
public final class DispatcherServlet extends HttpServlet {

    private static final String METHOD_GET = "GET";

    private static final String METHOD_HEAD = "HEAD";

    private static final String METHOD_POST = "POST";

    private static final String METHOD_PUT = "PUT";

    private static final String METHOD_DELETE = "DELETE";

    private String getResourceName;

    private String postResourceName;

    private String putResourceName;

    private String deleteResourceName;

    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.getResourceName = config.getInitParameter(METHOD_GET);
        this.postResourceName = config.getInitParameter(METHOD_POST);
        this.putResourceName = config.getInitParameter(METHOD_PUT);
        this.deleteResourceName = config.getInitParameter(METHOD_DELETE);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest,
     *           javax.servlet.http.HttpServletResponse)
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        forwardTo(this.deleteResourceName, request, response);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *           javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        forwardTo(this.getResourceName, request, response);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doHead(javax.servlet.http.HttpServletRequest,
     *           javax.servlet.http.HttpServletResponse)
     */
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        forwardTo(this.getResourceName, request, response);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *           javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        forwardTo(this.postResourceName, request, response);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest,
     *           javax.servlet.http.HttpServletResponse)
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        forwardTo(this.putResourceName, request, response);
    }

    /**
     * Forward the request to the specified named resource.
     */
    private void forwardTo(String resourceName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (resourceName == null) {
            methodNotAllowed(response);
            return;
        }
        RequestDispatcher dispatcher = getServletContext().getNamedDispatcher(resourceName);
        if (dispatcher == null) {
            throw new ServletException("Cannot get a RequestDispatcher for name: " + resourceName);
        }
        dispatcher.forward(request, response);
    }

    /**
     * @see <a
     *           href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.6">Method
     *           Not Allowed HTTP status </a>
     * @see <a
     *           href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.7>Allow
     *           HTTP header </a>
     */
    private void methodNotAllowed(HttpServletResponse response) throws IOException {
        final StringBuffer allowedMethods = new StringBuffer("OPTIONS, TRACE");
        if (this.getResourceName != null) {
            allowedMethods.append(", ").append(METHOD_GET);
            allowedMethods.append(", ").append(METHOD_HEAD);
        }
        if (this.postResourceName != null) {
            allowedMethods.append(", ").append(METHOD_POST);
        }
        if (this.putResourceName != null) {
            allowedMethods.append(", ").append(METHOD_PUT);
        }
        if (this.deleteResourceName != null) {
            allowedMethods.append(", ").append(METHOD_DELETE);
        }
        response.setHeader("Allow", allowedMethods.toString());
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
