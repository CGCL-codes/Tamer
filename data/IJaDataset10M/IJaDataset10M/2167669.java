package org.apache.jasper.servlet;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jasper.Constants;
import org.apache.jasper.EmbeddedServletOptions;
import org.apache.jasper.Options;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.apache.jasper.compiler.Localizer;

/**
 * The JSP engine (a.k.a Jasper).
 *
 * The servlet container is responsible for providing a
 * URLClassLoader for the web application context Jasper
 * is being used in. Jasper will try get the Tomcat
 * ServletContext attribute for its ServletContext class
 * loader, if that fails, it uses the parent class loader.
 * In either case, it must be a URLClassLoader.
 *
 * @author Anil K. Vijendran
 * @author Harish Prabandham
 * @author Remy Maucherat
 * @author Kin-man Chung
 * @author Glenn Nielsen
 */
public class JspServlet extends HttpServlet {

    private Log log = LogFactory.getLog(JspServlet.class);

    private ServletContext context;

    private ServletConfig config;

    private Options options;

    private JspRuntimeContext rctxt;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.config = config;
        this.context = config.getServletContext();
        String engineOptionsName = config.getInitParameter("engineOptionsClass");
        if (engineOptionsName != null) {
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                Class engineOptionsClass = loader.loadClass(engineOptionsName);
                Class[] ctorSig = { ServletConfig.class, ServletContext.class };
                Constructor ctor = engineOptionsClass.getConstructor(ctorSig);
                Object[] args = { config, context };
                options = (Options) ctor.newInstance(args);
            } catch (Throwable e) {
                log.warn("Failed to load engineOptionsClass", e);
                options = new EmbeddedServletOptions(config, context);
            }
        } else {
            options = new EmbeddedServletOptions(config, context);
        }
        rctxt = new JspRuntimeContext(context, options);
        if (log.isDebugEnabled()) {
            log.debug(Localizer.getMessage("jsp.message.scratch.dir.is", options.getScratchDir().toString()));
            log.debug(Localizer.getMessage("jsp.message.dont.modify.servlets"));
        }
    }

    /**
     * Returns the number of JSPs for which JspServletWrappers exist, i.e.,
     * the number of JSPs that have been loaded into the webapp with which
     * this JspServlet is associated.
     *
     * <p>This info may be used for monitoring purposes.
     *
     * @return The number of JSPs that have been loaded into the webapp with
     * which this JspServlet is associated
     */
    public int getJspCount() {
        return this.rctxt.getJspCount();
    }

    /**
     * Resets the JSP reload counter.
     *
     * @param count Value to which to reset the JSP reload counter
     */
    public void setJspReloadCount(int count) {
        this.rctxt.setJspReloadCount(count);
    }

    /**
     * Gets the number of JSPs that have been reloaded.
     *
     * <p>This info may be used for monitoring purposes.
     *
     * @return The number of JSPs (in the webapp with which this JspServlet is
     * associated) that have been reloaded
     */
    public int getJspReloadCount() {
        return this.rctxt.getJspReloadCount();
    }

    /**
     * <p>Look for a <em>precompilation request</em> as described in
     * Section 8.4.2 of the JSP 1.2 Specification.  <strong>WARNING</strong> -
     * we cannot use <code>request.getParameter()</code> for this, because
     * that will trigger parsing all of the request parameters, and not give
     * a servlet the opportunity to call
     * <code>request.setCharacterEncoding()</code> first.</p>
     *
     * @param request The servlet requset we are processing
     *
     * @exception ServletException if an invalid parameter value for the
     *  <code>jsp_precompile</code> parameter name is specified
     */
    boolean preCompile(HttpServletRequest request) throws ServletException {
        String queryString = request.getQueryString();
        if (queryString == null) {
            return (false);
        }
        int start = queryString.indexOf(Constants.PRECOMPILE);
        if (start < 0) {
            return (false);
        }
        queryString = queryString.substring(start + Constants.PRECOMPILE.length());
        if (queryString.length() == 0) {
            return (true);
        }
        if (queryString.startsWith("&")) {
            return (true);
        }
        if (!queryString.startsWith("=")) {
            return (false);
        }
        int limit = queryString.length();
        int ampersand = queryString.indexOf("&");
        if (ampersand > 0) {
            limit = ampersand;
        }
        String value = queryString.substring(1, limit);
        if (value.equals("true")) {
            return (true);
        } else if (value.equals("false")) {
            return (true);
        } else {
            throw new ServletException("Cannot have request parameter " + Constants.PRECOMPILE + " set to " + value);
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jspUri = null;
        String jspFile = (String) request.getAttribute(Constants.JSP_FILE);
        if (jspFile != null) {
            jspUri = jspFile;
        } else {
            jspUri = (String) request.getAttribute(Constants.INC_SERVLET_PATH);
            if (jspUri != null) {
                String pathInfo = (String) request.getAttribute("javax.servlet.include.path_info");
                if (pathInfo != null) {
                    jspUri += pathInfo;
                }
            } else {
                jspUri = request.getServletPath();
                String pathInfo = request.getPathInfo();
                if (pathInfo != null) {
                    jspUri += pathInfo;
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("JspEngine --> " + jspUri);
            log.debug("\t     ServletPath: " + request.getServletPath());
            log.debug("\t        PathInfo: " + request.getPathInfo());
            log.debug("\t        RealPath: " + context.getRealPath(jspUri));
            log.debug("\t      RequestURI: " + request.getRequestURI());
            log.debug("\t     QueryString: " + request.getQueryString());
            log.debug("\t  Request Params: ");
            Enumeration e = request.getParameterNames();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                log.debug("\t\t " + name + " = " + request.getParameter(name));
            }
        }
        try {
            boolean precompile = preCompile(request);
            serviceJspFile(request, response, jspUri, null, precompile);
        } catch (RuntimeException e) {
            throw e;
        } catch (ServletException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            throw new ServletException(e);
        }
    }

    public void destroy() {
        if (log.isDebugEnabled()) {
            log.debug("JspServlet.destroy()");
        }
        rctxt.destroy();
    }

    private void serviceJspFile(HttpServletRequest request, HttpServletResponse response, String jspUri, Throwable exception, boolean precompile) throws ServletException, IOException {
        JspServletWrapper wrapper = (JspServletWrapper) rctxt.getWrapper(jspUri);
        if (wrapper == null) {
            synchronized (this) {
                wrapper = (JspServletWrapper) rctxt.getWrapper(jspUri);
                if (wrapper == null) {
                    if (null == context.getResource(jspUri)) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, jspUri);
                        return;
                    }
                    boolean isErrorPage = exception != null;
                    wrapper = new JspServletWrapper(config, options, jspUri, isErrorPage, rctxt);
                    rctxt.addWrapper(jspUri, wrapper);
                }
            }
        }
        wrapper.service(request, response, precompile);
    }
}
