package org.objectstyle.cayenne.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.objectstyle.cayenne.conf.Configuration;

/**
 * A ResourceLocator that can find resources relative to web application context.
 * 
 * @author Andrei Adamchik
 */
public class WebApplicationResourceLocator extends ResourceLocator {

    private static Logger logObj;

    static {
        Predicate p = new Predicate() {

            public boolean evaluate(Object o) {
                return Configuration.isLoggingConfigured();
            }
        };
        logObj = new PredicateLogger(WebApplicationResourceLocator.class, p);
    }

    protected ServletContext context;

    protected List additionalContextPaths;

    /**
     * @since 1.2
     */
    public WebApplicationResourceLocator() {
        this.additionalContextPaths = new ArrayList();
        this.addFilesystemPath("/WEB-INF/");
    }

    /**
     * Creates new WebApplicationResourceLocator with default lookup policy including user
     * home directory, current directory and CLASSPATH.
     */
    public WebApplicationResourceLocator(ServletContext context) {
        this();
        setServletContext(context);
    }

    /**
     * Sets the ServletContext used to locate resources.
     */
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    /**
     * Gets the ServletContext used to locate resources.
     */
    public ServletContext getServletContext() {
        return this.context;
    }

    /**
     * Looks for resources relative to /WEB-INF/ directory or any extra context paths
     * configured. Internal ServletContext is used to find resources.
     */
    public URL findResource(String location) {
        if (!additionalContextPaths.isEmpty() && getServletContext() != null) {
            String suffix = location != null ? location : "";
            if (suffix.startsWith("/")) {
                suffix = suffix.substring(1);
            }
            Iterator cpi = this.additionalContextPaths.iterator();
            while (cpi.hasNext()) {
                String prefix = (String) cpi.next();
                if (!prefix.endsWith("/")) {
                    prefix += "/";
                }
                String fullName = prefix + suffix;
                logObj.debug("searching for: " + fullName);
                try {
                    URL url = getServletContext().getResource(fullName);
                    if (url != null) {
                        return url;
                    }
                } catch (MalformedURLException ex) {
                    logObj.debug("Malformed URL, ignoring.", ex);
                }
            }
        }
        return super.findResource(location);
    }

    /**
     * Override ResourceLocator.addFilesystemPath(String) to intercept context paths
     * starting with "/WEB-INF/" to place in additionalContextPaths.
     */
    public void addFilesystemPath(String path) {
        if (path != null) {
            if (path.startsWith("/WEB-INF/")) {
                this.additionalContextPaths.add(path);
            } else {
                super.addFilesystemPath(path);
            }
        } else {
            throw new IllegalArgumentException("Path must not be null.");
        }
    }
}
