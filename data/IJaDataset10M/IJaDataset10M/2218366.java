package com.noelios.restlet.ext.servlet;

import java.util.Enumeration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import org.restlet.Application;
import org.restlet.Context;
import com.noelios.restlet.application.ApplicationContext;

/**
 * Context allowing access to the component's connectors, reusing the Servlet's
 * logging mechanism and adding the Servlet's initialization parameters to the
 * context's parameters.
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public class ServletContextAdapter extends ApplicationContext {

    /** The Servlet context. */
    private ServletContext servletContext;

    /**
	 * Constructor.
	 * 
	 * @param servlet
	 *            The parent Servlet.
	 * @param parentContext
	 *            The parent context.
	 * @param application
	 *            The parent application.
	 */
    @SuppressWarnings("unchecked")
    public ServletContextAdapter(Servlet servlet, Application application, Context parentContext) {
        super(application, parentContext, new ServletLogger(servlet.getServletConfig().getServletContext()));
        this.servletContext = servlet.getServletConfig().getServletContext();
        setWarClient(new ServletWarClient(parentContext, servlet.getServletConfig().getServletContext()));
        String initParam;
        javax.servlet.ServletConfig servletConfig = servlet.getServletConfig();
        for (Enumeration<String> enum1 = servletConfig.getInitParameterNames(); enum1.hasMoreElements(); ) {
            initParam = enum1.nextElement();
            getParameters().add(initParam, servletConfig.getInitParameter(initParam));
        }
        for (Enumeration<String> enum1 = getServletContext().getInitParameterNames(); enum1.hasMoreElements(); ) {
            initParam = enum1.nextElement();
            getParameters().add(initParam, getServletContext().getInitParameter(initParam));
        }
    }

    /**
	 * Returns the Servlet context.
	 * 
	 * @return The Servlet context.
	 */
    public ServletContext getServletContext() {
        return this.servletContext;
    }
}
