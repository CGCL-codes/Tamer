package com.beam.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.beam.constants.Erp;

public class StartupListener implements ServletContextListener {

    public static String contextPath = null;

    public void contextDestroyed(ServletContextEvent event) {
    }

    public void contextInitialized(ServletContextEvent event) {
        Erp.contextPath = event.getServletContext().getRealPath("/");
        Erp.configPath = Erp.contextPath + "WEB-INF/conf/";
    }
}
