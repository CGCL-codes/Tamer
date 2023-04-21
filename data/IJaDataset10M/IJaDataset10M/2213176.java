package pl.xperios.rdk.shared.common;

import com.google.gwt.core.client.GWT;
import java.util.ArrayList;

/**
 *
 * @author Praca
 */
public class XLog {

    /**
     *
     */
    public static final transient int OFF_LEVEL = 0;

    /**
     *
     */
    public static final transient int FATAL_LEVEL = 1;

    /**
     *
     */
    public static final transient int ERROR_LEVEL = 2;

    /**
     *
     */
    public static final transient int WARN_LEVEL = 3;

    /**
     *
     */
    public static final transient int DEBUG_LEVEL = 4;

    /**
     *
     */
    public static final transient int INFO_LEVEL = 5;

    /**
     *
     */
    public static final transient int TRACE_LEVEL = 6;

    private static transient XLog instance = new XLog();

    /**
     *
     * @param message
     */
    public static void fatal(String message) {
        instance.log(message, FATAL_LEVEL, GWT.isClient());
    }

    /**
     *
     * @param message
     */
    public static void error(String message) {
        instance.log(message, ERROR_LEVEL, GWT.isClient());
    }

    /**
     *
     * @param message
     * @param e
     */
    public static void error(String message, Throwable e) {
        instance.log(message + "\n" + e.getMessage(), ERROR_LEVEL, GWT.isScript());
    }

    /**
     *
     * @param message
     */
    public static void warn(String message) {
        instance.log(message, WARN_LEVEL, GWT.isClient());
    }

    /**
     *
     * @param message
     */
    public static void debug(String message) {
        instance.log(message, DEBUG_LEVEL, GWT.isClient());
    }

    /**
     *
     * @param message
     */
    public static void info(String message) {
        instance.log(message, INFO_LEVEL, GWT.isClient());
    }

    /**
     *
     * @param message
     */
    public static void trace(String message) {
        instance.log(message, TRACE_LEVEL, GWT.isClient());
    }

    /**
     *
     * @param message
     * @param logLevel
     * @param source
     */
    public static void logMessage(String message, int logLevel, boolean source) {
        instance.log(message, logLevel, source);
    }

    /**
     *
     * @param handler
     */
    public static void registerNewHandler(LoggerHandler handler) {
        instance.handlers.add(handler);
    }

    /**
     *
     * @param e
     */
    public static void error(Throwable e) {
        String message = e.getMessage();
        for (int i = 0; i < e.getStackTrace().length; i++) {
            StackTraceElement stackTraceElement = e.getStackTrace()[i];
            message += "\n" + stackTraceElement.toString();
        }
        error(message);
        e.printStackTrace();
    }

    /**
     *
     * @param msg
     * @param e
     */
    public static void info(String msg, Throwable e) {
        String message = e.getMessage();
        for (int i = 0; i < e.getStackTrace().length; i++) {
            StackTraceElement stackTraceElement = e.getStackTrace()[i];
            message += "\n" + stackTraceElement.toString();
        }
        info(message);
    }

    /**
     *
     * @param msg
     * @param e
     */
    public static void warn(String msg, Throwable e) {
        String message = e.getMessage();
        for (int i = 0; i < e.getStackTrace().length; i++) {
            StackTraceElement stackTraceElement = e.getStackTrace()[i];
            message += "\n" + stackTraceElement.toString();
        }
        warn(message);
    }

    private final ArrayList<LoggerHandler> handlers = new ArrayList<LoggerHandler>();

    /**
     *
     * @param handler
     */
    public void registerLoggerHandler(LoggerHandler handler) {
        handlers.add(handler);
    }

    /**
     *
     * @param message
     * @param logLevel
     * @param source
     */
    public void log(String message, int logLevel, boolean source) {
        for (int i = 0; i < handlers.size(); i++) {
            LoggerHandler handler = handlers.get(i);
            if (handler != null) {
                handler.log(message, logLevel, source);
            }
        }
        if (handlers.isEmpty()) {
            System.out.println((source ? "[Client] " : "[Server]") + getTextForLevel(logLevel) + ":" + message);
        }
    }

    /**
     *
     * @param logLevel
     * @return
     */
    public static String getTextForLevel(int logLevel) {
        if (logLevel == XLog.FATAL_LEVEL) {
            return "FATAL";
        }
        if (logLevel == XLog.ERROR_LEVEL) {
            return "ERROR";
        }
        if (logLevel == XLog.WARN_LEVEL) {
            return "WARN";
        }
        if (logLevel == XLog.DEBUG_LEVEL) {
            return "DEBUG";
        }
        if (logLevel == XLog.INFO_LEVEL) {
            return "INFO";
        }
        if (logLevel == XLog.TRACE_LEVEL) {
            return "TRACE";
        }
        return "OFF";
    }
}
