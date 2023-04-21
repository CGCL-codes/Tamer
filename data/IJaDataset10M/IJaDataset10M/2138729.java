package com.rabidgremlin.svnviz.util;

/**
 * Very simple logger class. Not thread safe.
 * 
 * @author Jonathan Ackerman
 * @version $Id$
 */
public final class Log {

    /** True if debug logging is enabled. Default is false. */
    private static boolean debugEnabled = false;

    /**
   * Toggles debug logging on or off.
   * 
   * @param debugOn True to turn on debug logging.
   */
    public static void setDebug(boolean debugOn) {
        debugEnabled = debugOn;
    }

    /**
   * This method returns true if debug logging is one.
   * 
   * @return True if debug logging is one.
   */
    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    /**
   * This method logs a debug message. Log will only be written if debug logging is turned on.
   * 
   * @param message The debug message.
   */
    public static void debug(String message) {
        if (debugEnabled) {
            System.out.print("DEBUG: ");
            System.out.println(message);
        }
    }

    /**
   * This method logs an info message.
   * 
   * @param message The info message.
   */
    public static void info(String message) {
        System.out.println(message);
    }

    /**
   * This method logs an error message.
   * 
   * @param message The error message.
   */
    public static void error(String message) {
        System.out.print("ERROR: ");
        System.out.println(message);
    }

    /**
   * This method logs the error message and the stacktrace of the cause of the error.
   * 
   * @param message The error message.
   * @param t The cause of the error.
   */
    public static void error(String message, Throwable t) {
        System.out.println(message);
        t.printStackTrace(System.out);
    }
}
