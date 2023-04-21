package org.architecture.common.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.architecture.common.util.StringUtils;

/**
 * Application Context Listener
 * 
 * @author love
 *
 */
public final class ApplicationContextListener implements ServletContextListener {

    private static final Log log = LogFactory.getLog(ApplicationContextListener.class);

    /**
	 * 어플리케이션 컨텍스트 키
	 */
    public static final String APPLICATION_CONTEXT_KEY = "application.context";

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        try {
            ApplicationConfig applicationConfig = new ApplicationConfig();
            String scName = servletContext.getServletContextName();
            String wasName = servletContext.getServerInfo();
            String appPath = StringUtils.cleanPath(servletContext.getRealPath("/"));
            String servletVersion = servletContext.getMajorVersion() + "." + servletContext.getMinorVersion();
            ApplicationContext applicationContext = new ApplicationContext(applicationConfig, wasName, appPath, servletVersion);
            ApplicationContextFactory factory = ApplicationContextFactory.getInstance();
            factory.setApplicationContext(applicationContext);
            servletContext.setAttribute(APPLICATION_CONTEXT_KEY, applicationContext);
            log.info("#===========================================================");
            log.info("# Architecture Application Framework Initialized.");
            log.info("#-----------------------------------------------------------");
            log.info("# Servlet Context Name = [" + scName + "]");
            log.info("# Servlet Version = [" + servletVersion + "]");
            log.info("# Web Application Server Name = [" + wasName + "]");
            log.info("# Application Path = [" + appPath + "]");
            log.info("#-----------------------------------------------------------");
        } catch (Exception e) {
            log.error("Couldn't create ApplicationContext attribute : " + e.getMessage(), e);
        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        servletContext.removeAttribute(APPLICATION_CONTEXT_KEY);
        log.info("# Architecture Application Framework Destroyed.\n\n\n");
    }
}
