package com.jeronimo.eko.webui.guice;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author J�r�me Bonnet
 */
public abstract class GuiceContextListener implements ServletContextListener {

    JsfApplicationScope applicationScope;

    JsfRequestScope requestScope;

    JsfSessionScope sessionScope;

    public GuiceContextListener() {
        applicationScope = new JsfApplicationScope();
        requestScope = new JsfRequestScope();
        sessionScope = new JsfSessionScope();
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.setAttribute(GuiceELResolver.GUICE_INJECTOR_FACECONTEXT_KEY, getInjectorInstance());
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.removeAttribute(GuiceELResolver.GUICE_INJECTOR_FACECONTEXT_KEY);
    }

    protected Injector getInjectorInstance() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            protected void configure() {
                bindScope(JsfApplicationScoped.class, applicationScope);
                bindScope(JsfRequestScoped.class, requestScope);
                bindScope(JsfSessionScoped.class, sessionScope);
                Module[] mods = getModules();
                for (Module m : mods) {
                    install(m);
                }
            }
        });
        return injector;
    }

    public abstract Module[] getModules();
}
