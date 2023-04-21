package be.fedict.trust.startup.listener;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.fedict.trust.service.InitializationService;

/**
 * Servlet context listener to start up the eID Trust Service system.
 * 
 * @author Frank Cornelis
 * 
 */
public class StartupServletContextListener implements ServletContextListener {

    private static final Log LOG = LogFactory.getLog(StartupServletContextListener.class);

    @EJB
    private InitializationService initializationService;

    public void contextInitialized(ServletContextEvent event) {
        LOG.debug("context initialized");
        this.initializationService.initialize();
    }

    public void contextDestroyed(ServletContextEvent event) {
        LOG.debug("context destroyed");
    }
}
