package com.persistent.appfabric.common;

import com.persistent.appfabric.common.logger.Logger;

/**
 * Class used to log exceptions
 * */
public class LoggerUtil {

    private static Logger logger_;

    private static boolean isLoggingOn = false;

    /**
	 * Get logger
	 * */
    public static Logger getLogger() {
        return logger_;
    }

    /**
	 * Initialize logging
	 * @param filename File to which the logs should be recorded
	 * */
    public static void initLogging(String filename) {
        logger_ = new Logger(filename);
        isLoggingOn = true;
    }

    /**
	 * Check if event logging is turned on
	 * */
    public static boolean getIsLoggingOn() {
        return isLoggingOn;
    }
}
