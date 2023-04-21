package org.snipsnap.net;

import org.radeox.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        Logger.log(Logger.DEBUG, "WebApplication started: " + event.getServletContext().getRealPath("/"));
    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}
