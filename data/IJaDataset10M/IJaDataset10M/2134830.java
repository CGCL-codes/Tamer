package com.gjl.app.view.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import ognl.OgnlRuntime;

public class AppEngineInitListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent servletConextEvent) {
    }

    @Override
    public void contextInitialized(ServletContextEvent servletConextEvent) {
        OgnlRuntime.setSecurityManager(null);
    }
}
