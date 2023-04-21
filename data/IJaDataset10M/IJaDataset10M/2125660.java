package org.orbeon.oxf.webapp;

import org.orbeon.oxf.pipeline.api.WebAppExternalContext;
import org.orbeon.oxf.servlet.ServletWebAppExternalContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * This listener listens for HTTP context lifecycle changes.
 *
 * WARNING: This class must only depend on the Servlet API and the OXF Class Loader.
 */
public class OXFServletContextListener implements ServletContextListener {

    private ServletContextListener servletContextListenerDelegate;

    public void contextInitialized(ServletContextEvent event) {
        WebAppExternalContext webAppExternalContext = new ServletWebAppExternalContext(event.getServletContext());
        initializeDelegate(webAppExternalContext);
        Thread currentThread = Thread.currentThread();
        ClassLoader oldThreadContextClassLoader = currentThread.getContextClassLoader();
        try {
            currentThread.setContextClassLoader(OXFClassLoader.getClassLoader(webAppExternalContext));
            servletContextListenerDelegate.contextInitialized(event);
        } finally {
            currentThread.setContextClassLoader(oldThreadContextClassLoader);
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        WebAppExternalContext webAppExternalContext = new ServletWebAppExternalContext(event.getServletContext());
        initializeDelegate(webAppExternalContext);
        Thread currentThread = Thread.currentThread();
        ClassLoader oldThreadContextClassLoader = currentThread.getContextClassLoader();
        try {
            currentThread.setContextClassLoader(OXFClassLoader.getClassLoader(webAppExternalContext));
            servletContextListenerDelegate.contextDestroyed(event);
        } finally {
            currentThread.setContextClassLoader(oldThreadContextClassLoader);
        }
    }

    private void initializeDelegate(WebAppExternalContext webAppExternalContext) {
        try {
            if (servletContextListenerDelegate == null) {
                Class delegateServletClass = OXFClassLoader.getClassLoader(webAppExternalContext).loadClass(OXFServletContextListener.class.getName() + OXFClassLoader.DELEGATE_CLASS_SUFFIX);
                servletContextListenerDelegate = (ServletContextListener) delegateServletClass.newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
