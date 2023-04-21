package org.dasein.util;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

public class PoolTerminator implements ServletContextListener {

    public static final Logger logger = Logger.getLogger(PoolTerminator.class);

    public static ArrayList<Callable<Boolean>> terminationHandlers = new ArrayList<Callable<Boolean>>();

    public static void addTerminationHandler(Callable<Boolean> handler) {
        synchronized (terminationHandlers) {
            terminationHandlers.add(handler);
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        logger.debug("enter - contextDestroyed(ServletContextEvent)");
        try {
            synchronized (terminationHandlers) {
                for (Callable<Boolean> handler : terminationHandlers) {
                    try {
                        Boolean success = handler.call();
                        if (success == null || !success) {
                            logger.warn("Failure from " + handler.getClass().getName());
                        }
                    } catch (Exception e) {
                        logger.warn(handler.getClass().getName() + ": " + e.getMessage());
                    }
                }
            }
        } finally {
            logger.debug("exit - contextDestroyed(ServletContextEvent)");
        }
    }

    public void contextInitialized(ServletContextEvent event) {
    }
}
