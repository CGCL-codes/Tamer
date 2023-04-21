package org.kablink.teaming.spring.web.util;

import javax.servlet.ServletContextEvent;
import org.kablink.util.ServerDetector;

/**
 * 
 * @author jong
 *
 */
public class Log4jConfigListener extends org.springframework.web.util.Log4jConfigListener {

    public void contextInitialized(ServletContextEvent event) {
        if (!ServerDetector.isJBoss()) {
            super.contextInitialized(event);
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        if (!ServerDetector.isJBoss()) {
            super.contextDestroyed(event);
        }
    }
}
