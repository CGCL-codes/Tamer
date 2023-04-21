package com.rcreations.timeout;

import org.apache.log4j.Logger;

/**
 * Log utility methods.
 */
public class LogUtils {

    static final Logger g_logger = Logger.getLogger(LogUtils.class.getPackage().getName());

    /**
    * Get logger for package.
    */
    public static Logger getLogger() {
        return g_logger;
    }
}
