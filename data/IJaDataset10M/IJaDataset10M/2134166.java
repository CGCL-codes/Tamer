package net.sf.easyweb4j.container;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class EasyWeb4JListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(EasyWeb4JContainer.EASYWEB4J_CONTAINER, new EasyWeb4JContainer());
    }
}
