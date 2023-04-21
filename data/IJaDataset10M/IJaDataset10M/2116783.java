package org.light.portal.listener;

import static org.light.portal.util.Constants._EVENTS_STARTUP;
import static org.light.portal.util.Constants._EVENTS_SHUTDOWN;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.light.portal.core.event.StartupEvent;
import org.light.portal.core.event.ShutdownEvent;
import org.light.portal.logger.Logger;
import org.light.portal.logger.LoggerFactory;

/**
 * 
 * @author Jianmin Liu
 **/
public class ApplicationListener implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void contextInitialized(ServletContextEvent ce) {
        try {
            for (String event : _EVENTS_STARTUP) {
                Object eventObject = Class.forName(event).newInstance();
                ;
                if (eventObject instanceof StartupEvent) {
                    StartupEvent startupEvent = (StartupEvent) eventObject;
                    startupEvent.execute(ce.getServletContext());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void contextDestroyed(ServletContextEvent ce) {
        try {
            for (String event : _EVENTS_SHUTDOWN) {
                Object eventObject = Class.forName(event).newInstance();
                ;
                if (eventObject instanceof ShutdownEvent) {
                    ShutdownEvent shutdownEvent = (ShutdownEvent) eventObject;
                    shutdownEvent.execute(ce.getServletContext());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
