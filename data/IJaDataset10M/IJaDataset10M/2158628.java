package com.grooveapp.web;

import javax.servlet.ServletContextEvent;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class GrooveContextLoaderListener extends ContextLoaderListener {

    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
    }

    public void contextDestroyed(ServletContextEvent event) {
        ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
        SessionFactory sessionFactory = (SessionFactory) appContext.getBean("sessionFactory");
        sessionFactory.close();
        super.contextDestroyed(event);
    }
}
