package net.sourceforge.chaperon.common;

import org.apache.commons.logging.Log;

/**
 * Logger sending everything to the standard output streams. This is mainly for the cases when you
 * have a utility that does not have a logger to supply.
 *
 * @author <a href="mailto:stephan@apache.org">Stephan Michels </a>
 * @version CVS $Id: ConsoleLog.java,v 1.4 2003/12/14 09:53:56 benedikta Exp $
 */
public final class ConsoleLog implements Log {

    public static final int TRACE = 0;

    public static final int DEBUG = 1;

    public static final int INFO = 2;

    public static final int WARN = 3;

    public static final int ERROR = 4;

    public static final int FATAL = 5;

    private int level = DEBUG;

    public ConsoleLog() {
    }

    public ConsoleLog(int level) {
        this.level = level;
    }

    /**
   * <p>
   * Log a message with trace log level.
   * </p>
   *
   * @param message log this message
   */
    public void trace(Object message) {
        if (isTraceEnabled()) {
            System.out.print("[TRACE] ");
            System.out.println(message);
        }
    }

    /**
   * <p>
   * Log an error with trace log level.
   * </p>
   *
   * @param message log this message
   * @param t log this cause
   */
    public void trace(Object message, Throwable t) {
        if (isTraceEnabled()) {
            System.out.print("[TRACE] ");
            System.out.println(message);
            t.printStackTrace(System.out);
        }
    }

    /**
   * <p>
   * Is trace logging currently enabled?
   * </p>
   * 
   * <p>
   * Call this method to prevent having to perform expensive operations (for example,
   * <code>String</code> concatination) when the log level is more than trace.
   * </p>
   */
    public boolean isTraceEnabled() {
        return level <= TRACE;
    }

    /**
   * <p>
   * Log a message with debug log level.
   * </p>
   *
   * @param message log this message
   */
    public void debug(Object message) {
        if (isDebugEnabled()) {
            System.out.print("[DEBUG] ");
            System.out.println(message);
        }
    }

    /**
   * <p>
   * Log an error with debug log level.
   * </p>
   *
   * @param message log this message
   * @param t log this cause
   */
    public void debug(Object message, Throwable t) {
        if (isDebugEnabled()) {
            System.out.print("[DEBUG] ");
            System.out.println(message);
            t.printStackTrace(System.out);
        }
    }

    /**
   * <p>
   * Is debug logging currently enabled?
   * </p>
   * 
   * <p>
   * Call this method to prevent having to perform expensive operations (for example,
   * <code>String</code> concatination) when the log level is more than debug.
   * </p>
   */
    public boolean isDebugEnabled() {
        return level <= DEBUG;
    }

    /**
   * <p>
   * Log a message with info log level.
   * </p>
   *
   * @param message log this message
   */
    public void info(Object message) {
        if (isInfoEnabled()) {
            System.out.print("[INFO] ");
            System.out.println(message);
        }
    }

    /**
   * <p>
   * Log an error with info log level.
   * </p>
   *
   * @param message log this message
   * @param t log this cause
   */
    public void info(Object message, Throwable t) {
        if (isInfoEnabled()) {
            System.out.print("[INFO] ");
            System.out.println(message);
            t.printStackTrace(System.out);
        }
    }

    /**
   * <p>
   * Is info logging currently enabled?
   * </p>
   * 
   * <p>
   * Call this method to prevent having to perform expensive operations (for example,
   * <code>String</code> concatination) when the log level is more than info.
   * </p>
   */
    public boolean isInfoEnabled() {
        return level <= INFO;
    }

    /**
   * <p>
   * Log a message with warn log level.
   * </p>
   *
   * @param message log this message
   */
    public void warn(Object message) {
        if (isWarnEnabled()) {
            System.out.print("[WARN] ");
            System.out.println(message);
        }
    }

    /**
   * <p>
   * Log an error with warn log level.
   * </p>
   *
   * @param message log this message
   * @param t log this cause
   */
    public void warn(Object message, Throwable t) {
        if (isWarnEnabled()) {
            System.out.print("[WARN] ");
            System.out.println(message);
            t.printStackTrace(System.out);
        }
    }

    /**
   * <p>
   * Is warning logging currently enabled?
   * </p>
   * 
   * <p>
   * Call this method to prevent having to perform expensive operations (for example,
   * <code>String</code> concatination) when the log level is more than warning.
   * </p>
   */
    public boolean isWarnEnabled() {
        return level <= WARN;
    }

    /**
   * <p>
   * Log a message with error log level.
   * </p>
   *
   * @param message log this message
   */
    public void error(Object message) {
        if (isErrorEnabled()) {
            System.out.print("[ERROR] ");
            System.out.println(message);
        }
    }

    /**
   * <p>
   * Log an error with error log level.
   * </p>
   *
   * @param message log this message
   * @param t log this cause
   */
    public void error(Object message, Throwable t) {
        if (isErrorEnabled()) {
            System.out.print("[ERROR] ");
            System.out.println(message);
            t.printStackTrace(System.out);
        }
    }

    /**
   * <p>
   * Is error logging currently enabled?
   * </p>
   * 
   * <p>
   * Call this method to prevent having to perform expensive operations (for example,
   * <code>String</code> concatination) when the log level is more than error.
   * </p>
   */
    public boolean isErrorEnabled() {
        return level <= ERROR;
    }

    /**
   * <p>
   * Log a message with fatal log level.
   * </p>
   *
   * @param message log this message
   */
    public void fatal(Object message) {
        if (isFatalEnabled()) {
            System.out.print("[FATAL] ");
            System.out.println(message);
        }
    }

    /**
   * <p>
   * Log an error with fatal log level.
   * </p>
   *
   * @param message log this message
   * @param t log this cause
   */
    public void fatal(Object message, Throwable t) {
        if (isFatalEnabled()) {
            System.out.print("[FATAL] ");
            System.out.println(message);
            t.printStackTrace(System.out);
        }
    }

    /**
   * <p>
   * Is fatal logging currently enabled?
   * </p>
   * 
   * <p>
   * Call this method to prevent having to perform expensive operations (for example,
   * <code>String</code> concatination) when the log level is more than fatal.
   * </p>
   */
    public boolean isFatalEnabled() {
        return level <= FATAL;
    }
}
