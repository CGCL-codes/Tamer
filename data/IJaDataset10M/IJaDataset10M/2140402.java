package org.apache.axis.transport.http;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.axis.AxisEngine;
import org.apache.axis.AxisFault;
import org.apache.axis.AxisProperties;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.components.logger.LogFactory;
import org.apache.axis.configuration.EngineConfigurationFactoryFinder;
import org.apache.axis.server.AxisServer;
import org.apache.axis.utils.JavaUtils;
import org.apache.commons.logging.Log;

/**
 * Base class for servlets used in axis, has common methods
 * to get and save the engine to a common location, currently the
 * webapp's context, though some alternate persistence mechanism is always
 * possible. Also has a load counter shared by all servlets; tracks the
 * # of active http requests open to any of the subclasses.
 * @author Steve Loughran
 */
public class AxisServletBase extends HttpServlet {

    /**
     * per-instance cache of the axis server
     */
    protected AxisServer axisServer = null;

    private static Log log = LogFactory.getLog(AxisServlet.class.getName());

    private static boolean isDebug = false;

    /**
     *  count number of service requests in progress
     */
    private static int loadCounter = 0;

    /**
     *  and a lock
     */
    private static Object loadCounterLock = new Object();

    /**
     * name of the axis engine to use in the servlet context
     */
    protected static final String ATTR_AXIS_ENGINE = "AxisEngine";

    /**
     *  Cached path to our WEB-INF directory
     */
    private String webInfPath = null;

    /**
     * Cached path to our "root" dir
     */
    private String homeDir = null;

    /**
     * flag set to true for a 'production' server
     */
    private boolean isDevelopment;

    /**
     * property name for a production server
     */
    private static final String INIT_PROPERTY_DEVELOPMENT_SYSTEM = "axis.development.system";

    /**
     * our initialize routine; subclasses should call this if they override it
     */
    public void init() throws javax.servlet.ServletException {
        ServletContext context = getServletConfig().getServletContext();
        webInfPath = context.getRealPath("/WEB-INF");
        homeDir = context.getRealPath("/");
        isDebug = log.isDebugEnabled();
        if (log.isDebugEnabled()) log.debug("In AxisServletBase init");
        isDevelopment = JavaUtils.isTrueExplicitly(getOption(context, INIT_PROPERTY_DEVELOPMENT_SYSTEM, null));
    }

    /**
     * Destroy method is called when the servlet is going away.  Pass this
     * down to the AxisEngine to let it clean up...  But don't create the
     * engine if it hasn't already been created.
     * @todo Fixme for multiple servlets.
     * This has always been slightly broken
     * (the context's copy stayed around), but now we have extracted it into
     * a superclass it is blatantly broken.
     */
    public void destroy() {
        super.destroy();
        if (axisServer != null) {
            synchronized (axisServer) {
                if (axisServer != null) {
                    axisServer.cleanup();
                    axisServer = null;
                    storeEngine(this, null);
                }
            }
        }
    }

    /**
     * get the engine for this servlet from cache or context
     * @return
     * @throws AxisFault
     */
    public AxisServer getEngine() throws AxisFault {
        if (axisServer == null) axisServer = getEngine(this);
        return axisServer;
    }

    /**
     * This is a uniform method of initializing AxisServer in a servlet
     * context.
     * @todo add catch for not being able to cast the context attr to an
     * engine and reinit the engine if so.
     */
    public static AxisServer getEngine(HttpServlet servlet) throws AxisFault {
        AxisServer engine = null;
        if (isDebug) log.debug("Enter: getEngine()");
        ServletContext context = servlet.getServletContext();
        synchronized (context) {
            engine = retrieveEngine(servlet);
            if (engine == null) {
                Map environment = getEngineEnvironment(servlet);
                engine = AxisServer.getServer(environment);
                engine.setName(servlet.getServletName());
                storeEngine(servlet, engine);
            }
        }
        if (isDebug) log.debug("Exit: getEngine()");
        return engine;
    }

    /**
     * put the engine back in to the context.
     * @param context servlet context to use
     * @param engine reference to the engine. If null, the engine is removed
     */
    private static void storeEngine(HttpServlet servlet, AxisServer engine) {
        ServletContext context = servlet.getServletContext();
        String axisServletName = servlet.getServletName();
        if (engine == null) {
            context.removeAttribute(axisServletName + ATTR_AXIS_ENGINE);
            AxisServer server = (AxisServer) context.getAttribute(ATTR_AXIS_ENGINE);
            if (server != null && servlet.getServletName().equals(server.getName())) {
                context.removeAttribute(ATTR_AXIS_ENGINE);
            }
        } else {
            if (context.getAttribute(ATTR_AXIS_ENGINE) == null) {
                context.setAttribute(ATTR_AXIS_ENGINE, engine);
            }
            context.setAttribute(axisServletName + ATTR_AXIS_ENGINE, engine);
        }
    }

    /**
     * Get an engine from the servlet context; robust againt serialization
     * issues of hot-updated webapps. Remember than if a webapp is marked
     * as distributed, there is more than 1 servlet context, hence more than
     * one AxisEngine instance
     * @param servlet
     * @return the engine or null if either the engine couldnt be found or
     *         the attribute wasnt of the right type
     */
    private static AxisServer retrieveEngine(HttpServlet servlet) {
        Object contextObject = servlet.getServletContext().getAttribute(servlet.getServletName() + ATTR_AXIS_ENGINE);
        if (contextObject == null) {
            contextObject = servlet.getServletContext().getAttribute(ATTR_AXIS_ENGINE);
        }
        if (contextObject instanceof AxisServer) {
            AxisServer server = (AxisServer) contextObject;
            if (server != null && servlet.getServletName().equals(server.getName())) {
                return server;
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * extract information from the servlet configuration files
     * @param servlet
     * @return
     */
    protected static Map getEngineEnvironment(HttpServlet servlet) {
        Map environment = new HashMap();
        String attdir = servlet.getInitParameter(AxisEngine.ENV_ATTACHMENT_DIR);
        if (attdir != null) environment.put(AxisEngine.ENV_ATTACHMENT_DIR, attdir);
        ServletContext context = servlet.getServletContext();
        environment.put(AxisEngine.ENV_SERVLET_CONTEXT, context);
        String webInfPath = context.getRealPath("/WEB-INF");
        if (webInfPath != null) environment.put(AxisEngine.ENV_SERVLET_REALPATH, webInfPath + File.separator + "attachments");
        EngineConfiguration config = EngineConfigurationFactoryFinder.newFactory(servlet).getServerEngineConfig();
        if (config != null) {
            environment.put(EngineConfiguration.PROPERTY_NAME, config);
        }
        return environment;
    }

    /**
     *  get a count of the # of services running. This is only
     *  ever an approximate number in a busy system
     *
     * @return    The TotalServiceCount value
     */
    public static int getLoadCounter() {
        return loadCounter;
    }

    /**
     * thread safe lock counter increment
     */
    protected static void incLockCounter() {
        synchronized (loadCounterLock) {
            loadCounter++;
        }
    }

    /**
     * thread safe lock counter decrement
     */
    protected static void decLockCounter() {
        synchronized (loadCounterLock) {
            loadCounter--;
        }
    }

    /**
     * subclass of service method that tracks entry count; calls the
     * parent's implementation to have the http method cracked and delegated
     * to the doGet, doPost method.
     * @param req request
     * @param resp response
     * @throws ServletException something went wrong
     * @throws IOException something different went wrong
     */
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        incLockCounter();
        try {
            super.service(req, resp);
        } finally {
            decLockCounter();
        }
    }

    /**
     * extract the base of our webapp from an inbound request
     *
     * @param request request containing http://foobar/axis/services/something
     * @return some URL like http://foobar:8080/axis/
     */
    protected String getWebappBase(HttpServletRequest request) {
        StringBuffer baseURL = new StringBuffer(128);
        baseURL.append(request.getScheme());
        baseURL.append("://");
        baseURL.append(request.getServerName());
        if (request.getServerPort() != 80) {
            baseURL.append(":");
            baseURL.append(request.getServerPort());
        }
        baseURL.append(request.getContextPath());
        return baseURL.toString();
    }

    /**
     * what is the servlet context
     * @return get the context from the servlet config
     */
    public ServletContext getServletContext() {
        return getServletConfig().getServletContext();
    }

    /**
     * accessor to webinf
     * @return path to WEB-INF/ in the local filesystem
     */
    protected String getWebInfPath() {
        return webInfPath;
    }

    /**
     * what is the root dir of the applet?
     * @return path of root dir
     */
    protected String getHomeDir() {
        return homeDir;
    }

    /**
     * Retrieve option, in order of precedence:
     * (Managed) System property (see discovery.ManagedProperty),
     * servlet init param, context init param.
     * Use of system properties is discouraged in production environments,
     * as it overrides everything else.
     */
    protected String getOption(ServletContext context, String param, String dephault) {
        String value = AxisProperties.getProperty(param);
        if (value == null) value = getInitParameter(param);
        if (value == null) value = context.getInitParameter(param);
        try {
            AxisServer engine = getEngine(this);
            if (value == null && engine != null) value = (String) engine.getOption(param);
        } catch (AxisFault axisFault) {
        }
        return (value != null) ? value : dephault;
    }

    /**
     * probe for the system being 'production'
     * @return true for a dev system.
     */
    public boolean isDevelopment() {
        return isDevelopment;
    }
}
