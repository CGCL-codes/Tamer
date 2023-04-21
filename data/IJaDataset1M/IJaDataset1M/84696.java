package edu.uiuc.ncsa.security.servlet;

import edu.uiuc.ncsa.security.core.Logable;
import edu.uiuc.ncsa.security.core.exceptions.GeneralException;
import edu.uiuc.ncsa.security.core.util.AbstractEnvironment;
import edu.uiuc.ncsa.security.core.util.ConfigurationLoader;
import edu.uiuc.ncsa.security.core.util.MyLogger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Very straightforward servlet wrapper. This sets up logging and debug. All posts and gets
 * are intercepted and routed to a single doIt method.<br><br></br></br>
 * 3/23/2012: Added simple bootstrapping mechanism. This requires you set a context
 * listener in your web.xml deployment descriptor. See the documentation in
 * {@link Bootstrapper} for more details.
 * <p>Created by Jeff Gaynor<br>
 * on May 3, 2010 at  11:35:16 AM
 */
public abstract class AbstractServlet extends HttpServlet implements Logable {

    static ConfigurationLoader<? extends AbstractEnvironment> bootstrapper;

    public static ConfigurationLoader<? extends AbstractEnvironment> getBootstrapper() {
        return bootstrapper;
    }

    public static void setBootstrapper(ConfigurationLoader<? extends AbstractEnvironment> b) {
        bootstrapper = b;
    }

    public static AbstractEnvironment getEnvironment() throws IOException {
        return environment;
    }

    public static void setEnvironment(AbstractEnvironment env) {
        environment = env;
    }

    protected static AbstractEnvironment environment;

    /**
     * Loads the current environment. This is *not* called automatically. Usually a user will
     * create a custom environment and have a getter for that which checks if the envirnoment has
     * been set and if not, load it or if so, cast it and return the result.
     *
     * @throws IOException
     */
    public abstract void loadEnvironment() throws IOException;

    public boolean isDebugOn() {
        return getMyLogger().isDebugOn();
    }

    public void setDebugOn(boolean setOn) {
        getMyLogger().setDebugOn(setOn);
    }

    protected MyLogger getMyLogger() {
        if (myLogger == null) {
            myLogger = new MyLogger(getClass().getName(), false);
        }
        return myLogger;
    }

    public void debug(String x) {
        getMyLogger().debug(x);
    }

    public void error(String x) {
        getMyLogger().error(x);
    }

    public void info(String x) {
        getMyLogger().info(x);
    }

    public void warn(String x) {
        getMyLogger().warn(x);
    }

    MyLogger myLogger;

    /**
     * One stop shopping for exception handling. All thrown exceptions are intercepted and run through this.
     * Depending on their type they are wrapped or passed along. You can change this behavior if you need to.
     * <p>Note that all runtime exceptions, IOExceptions and ServletExceptions are not modified,
     * so if you over-ride this and throw one of those exceptions you will not get extra cruft.
     * <p>Also, a response is passed along. This may be used in over-rides, but is not used in the
     * basic implementation. If it is null, it should be ignored.</p>
     *
     * @param t
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    protected void handleException(Throwable t, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        error("INTERNAL ERROR: " + (t.getMessage() == null ? "(no message)" : t.getMessage()));
        if (isDebugOn()) {
            t.printStackTrace();
        }
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }
        if ((t instanceof IOException)) {
            throw (IOException) t;
        }
        if (t instanceof ServletException) {
            throw (ServletException) t;
        }
        throw new ServletException(t);
    }

    @Override
    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            doIt(httpServletRequest, httpServletResponse);
        } catch (Throwable t) {
            handleException(t, httpServletRequest, httpServletResponse);
        }
    }

    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            doIt(httpServletRequest, httpServletResponse);
        } catch (Throwable t) {
            handleException(t, httpServletRequest, httpServletResponse);
        }
    }

    protected abstract void doIt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Throwable;

    @Override
    public void init() throws ServletException {
        super.init();
        if (environment == null) {
            info("LOADING ENVIRONMENT");
            try {
                loadEnvironment();
            } catch (IOException e) {
                throw new GeneralException("Error loading environment", e);
            }
        }
    }

    /**
     * This nulls out the environment which should force a reload of it at the next call. Put anything
     * else you need nulled out here. This allows you to have, e.g., an updated configuration reloaded
     * on the fly without a tomcat restart.
     */
    public void resetState() {
        environment = null;
    }
}
