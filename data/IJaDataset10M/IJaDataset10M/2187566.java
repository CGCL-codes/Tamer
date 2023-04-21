package com.cooldatasoft.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.ApplicationServlet;

public class AutowiringApplicationServlet extends ApplicationServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6906853406878827435L;

    private WebApplicationContext webApplicationContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            this.webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        } catch (IllegalStateException e) {
            throw new ServletException("could not locate containing WebApplicationContext");
        }
    }

    protected final WebApplicationContext getWebApplicationContext() throws ServletException {
        if (this.webApplicationContext == null) throw new ServletException("can't retrieve WebApplicationContext before init() is invoked");
        return this.webApplicationContext;
    }

    protected final AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws ServletException {
        try {
            return getWebApplicationContext().getAutowireCapableBeanFactory();
        } catch (IllegalStateException e) {
            throw new ServletException("containing context " + getWebApplicationContext() + " is not autowire-capable", e);
        }
    }

    @Override
    protected Application getNewApplication(HttpServletRequest request) throws ServletException {
        Class<? extends Application> cl = null;
        try {
            cl = getApplicationClass();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        AutowireCapableBeanFactory beanFactory = getAutowireCapableBeanFactory();
        try {
            return beanFactory.createBean(cl);
        } catch (BeansException e) {
            throw new ServletException("failed to create new instance of " + cl, e);
        }
    }
}
