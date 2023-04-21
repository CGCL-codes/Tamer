package com.darwinit.xmlfiles.exceptions;

import org.apache.log4j.Logger;

public abstract class ExceptionLogger {

    /**
	 * Local logger
	 * 
	 * @param StackTraceElement[]
	 */
    public static void errorStack(Logger logger, StackTraceElement[] stackTrace) {
        for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement e = stackTrace[i];
            String line = "    at " + e.getClassName() + "." + e.getMethodName() + "(" + e.getFileName() + ":" + e.getLineNumber() + ")";
            logger.error(line);
        }
    }

    /**
	 * Local logger
	 * 
	 * @param Exception
	 */
    public static void error(Logger logger, Exception e) {
        logger.error("Cause: " + e.getCause());
        logger.error("Localized Message: " + e.getLocalizedMessage());
        logger.error("Message: " + e.getMessage());
        logger.error("Exception: " + e.getClass().getName());
        errorStack(logger, e.getStackTrace());
    }
}
