package org.brandao.brutos;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import org.brandao.brutos.old.programatic.IOCManager;

/**
 * @deprecated 
 * @author Afonso Brandao
 */
public class ContextLoaderListener extends org.brandao.brutos.web.ContextLoaderListener {

    private BrutosContext brutosInstance;

    public static ThreadLocal<ServletRequest> currentRequest;

    public static ServletContext currentContext;

    public ContextLoaderListener() {
        currentRequest = new ThreadLocal<ServletRequest>();
        brutosInstance = new BrutosContext();
    }

    public void contextInitialized(ServletContextEvent sce) {
        currentContext = sce.getServletContext();
        brutosInstance.start(sce);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        currentRequest = null;
        if (brutosInstance != null) brutosInstance.stop(sce);
    }

    public void sessionCreated(HttpSessionEvent se) {
    }

    public void sessionDestroyed(HttpSessionEvent se) {
    }

    public void requestDestroyed(ServletRequestEvent sre) {
        if (currentRequest != null) currentRequest.remove();
    }

    public HttpServletRequest getRequest(ServletRequest request) {
        try {
            HttpServletRequest brutosRequest = (HttpServletRequest) Class.forName(brutosInstance.getConfiguration().getProperty("org.brandao.brutos.request", "org.brandao.brutos.http.DefaultBrutosRequest"), true, Thread.currentThread().getContextClassLoader()).getConstructor(HttpServletRequest.class).newInstance(request);
            return brutosRequest;
        } catch (Exception e) {
            throw new BrutosException("problem getting the request: " + e.getMessage(), e);
        }
    }

    public void requestInitialized(ServletRequestEvent sre) {
        currentRequest.set(getRequest(sre.getServletRequest()));
    }

    public void servletInitialized(Servlet servlet) {
    }

    public void servletDestroyed(Servlet servlet) {
    }
}
