package org.tn5250j.tools.logging;

import java.util.*;
import org.tn5250j.tools.logging.TN5250jLogger;

/**
 * An interface defining objects that can create Configure
 * instances.
 *
 * The model for the HashMap implementation of loggers came from the POI project
 * thanks to Nicola Ken Barozzi (nicolaken at apache.org) for the reference.
 *
 */
public abstract class TN5250jLogFactory {

    private static Map _loggers = new HashMap();

    private static boolean log4j;

    private static String customLogger;

    private static int level = TN5250jLogger.INFO;

    /**
    * Here we try to do a little more work up front.
    */
    static {
        try {
            String customLogger = System.getProperty(TN5250jLogFactory.class.getName());
            if (customLogger == null) {
                try {
                    Class classObject = Class.forName("org.apache.log4j.Logger");
                    log4j = true;
                } catch (Exception ex) {
                    ;
                }
            }
        } catch (Exception ex) {
            ;
        }
    }

    /**
    * Set package access only so we have to use getLogger() to return a logger object.
    */
    TN5250jLogFactory() {
    }

    /**
    * @return An instance of the TN5250jLogger.
    */
    public static TN5250jLogger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    /**
    * @return An instance of the TN5250jLogger.
    */
    public static TN5250jLogger getLogger(String clazzName) {
        TN5250jLogger logger = null;
        if (_loggers.containsKey(clazzName)) {
            logger = (TN5250jLogger) _loggers.get(clazzName);
        } else {
            if (customLogger != null) {
                try {
                    Class classObject = Class.forName(customLogger);
                    Object object = classObject.newInstance();
                    if (object instanceof TN5250jLogFactory) {
                        logger = (TN5250jLogger) object;
                    }
                } catch (Exception ex) {
                    ;
                }
            } else {
                if (logger == null) {
                    if (log4j) logger = new Log4jLogger(); else logger = new ConsoleLogger();
                }
                logger.initialize(clazzName);
                logger.setLevel(level);
                _loggers.put(clazzName, logger);
            }
        }
        return logger;
    }

    public static boolean isLog4j() {
        return log4j;
    }

    public static void setLogLevels(int newLevel) {
        if (level != newLevel) {
            level = newLevel;
            TN5250jLogger logger = null;
            Set loggerSet = _loggers.keySet();
            Iterator loggerIterator = loggerSet.iterator();
            while (loggerIterator.hasNext()) {
                logger = (TN5250jLogger) _loggers.get(loggerIterator.next());
                logger.setLevel(newLevel);
            }
        }
    }
}
