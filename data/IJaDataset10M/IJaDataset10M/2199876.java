package com.darwinit.xmleditor.log;

import org.apache.log4j.Logger;

/**
 * localLogger: Helper class to externalize logging 
 *
 * @author Martien van den Akker
 * @author Darwin IT Professionals
 *
 */
public class LocalLogger {

    protected Logger logger;

    /**
     * Constructor
     */
    public LocalLogger(Class parentClass) {
        logger = Logger.getLogger(parentClass);
    }

    /**
     * Get Current Thread Id.
     * @return
     */
    public long getCurrentThreadId() {
        Thread currentThread = Thread.currentThread();
        long threadId = currentThread.getId();
        return threadId;
    }

    /**
     * Get Current Thread Id as String.
     * @return
     */
    public String getCurrentThreadIdStr() {
        Thread currentThread = Thread.currentThread();
        long threadId = currentThread.getId();
        String threadIdStr = String.valueOf(threadId);
        return threadIdStr;
    }

    /**
     * Get Current Thread Name.
     * @return
     */
    public String getCurrentThreadName() {
        Thread currentThread = Thread.currentThread();
        String threadName = currentThread.getName();
        return threadName;
    }

    /**
     * Get a prompt before a message.
     * Now filled with thread Name. 
     * @return
     */
    public String prompt() {
        String prompt = "";
        return prompt;
    }

    /**
     * Local logger
     * 
     * @param message
     */
    public void log(String message) {
        logger.info(prompt() + message);
    }

    /**
     * Local logger
     * 
     * @param message
     */
    public void error(String message) {
        logger.error(prompt() + message);
    }

    /**
     * Local logger
     * 
     * @param stackTrace StackTraceElement[]
     */
    public void errorStack(StackTraceElement[] stackTrace) {
        for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement e = stackTrace[i];
            String line = "    at " + e.getClassName() + "." + e.getMethodName() + "(" + e.getFileName() + ":" + e.getLineNumber() + ")";
            error(line);
        }
    }

    /**
     * Local logger
     * 
     * @param e Exception
     */
    public void error(Exception e) {
        error("Cause: " + e.getCause());
        error("Localized Message: " + e.getLocalizedMessage());
        error("Message: " + e.getMessage());
        error("Exception: " + e.getClass().getName());
        errorStack(e.getStackTrace());
    }

    /**
     * Local logger
     * 
     * @param message
     */
    public void warn(String message) {
        logger.warn(prompt() + message);
    }

    /**
     * Local logger
     * 
     * @param message
     */
    public void info(String message) {
        logger.info(prompt() + message);
    }

    /**
     * Local logger
     * 
     * @param message
     */
    public void debug(String message) {
        logger.debug(prompt() + message);
    }

    /**
     * Local logger
     * 
     * @param message
     */
    public void debug(StringBuffer message) {
        logger.debug(prompt() + message.toString());
    }
}
