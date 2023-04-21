package net.infian.framework;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import net.infian.framework.logging.LogManager;

/**
 * This class handles the initialization of the other framework components.
 */
public final class FrameworkInitListener implements ServletContextListener {

    private static String path;

    public static final String getPath() {
        return path;
    }

    @Override
    public final void contextInitialized(ServletContextEvent event) {
        path = event.getServletContext().getRealPath("/");
        try {
            Class.forName(FrameworkConstants.logInit);
            Class.forName(FrameworkConstants.dbInit);
            Class.forName(FrameworkConstants.daoInit);
            Class.forName(FrameworkConstants.cacheInit);
        } catch (Exception e) {
            LogManager.logException(e);
        }
    }

    @Override
    public final void contextDestroyed(ServletContextEvent event) {
    }
}
