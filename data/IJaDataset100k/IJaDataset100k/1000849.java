package org.ice.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.ice.Config;

public class IceListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent sce) {
        Config.unload(sce.getServletContext());
    }

    public void contextInitialized(ServletContextEvent sce) {
        Config.load(sce.getServletContext());
    }
}
