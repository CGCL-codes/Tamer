package org.judo.service.systemservices;

import org.apache.log4j.Logger;

public class Log4JLogger {

    public static void debug(Object obj, String message) {
        Logger logger = Logger.getLogger(obj.getClass());
        logger.debug(message);
    }

    public static void info(Object obj, String message) {
        Logger logger = Logger.getLogger(obj.getClass());
        logger.info(message);
    }

    public static void warn(Object obj, String message) {
        Logger logger = Logger.getLogger(obj.getClass());
        logger.warn(message);
    }

    public static void error(Object obj, String message) {
        Logger logger = Logger.getLogger(obj.getClass());
        logger.error(message);
    }

    public static void fatal(Object obj, String message) {
        Logger logger = Logger.getLogger(obj.getClass());
        logger.fatal(message);
    }
}
