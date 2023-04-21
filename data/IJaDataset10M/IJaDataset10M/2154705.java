package org.tigr.seq.log;

import com.sun.java.util.collections.*;

/**
 *
 * The main Log class that client code is expected to interface with
 * for logging events of interest.  Clients should use either of the
 * two Log.log() methods to record an event.  This class will take
 * care of notifying all registered ILog implementations.  It is
 * intended that the Log.log() methods are callable from any thread
 * context, so ILog implementations will need to take appropriate
 * thread-safety precautions.
 *
 * <p>
 * Copyright &copy; 2001 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: Log.java,v $
 * $Revision: 1.6 $
 * $Date: 2005/11/16 17:38:35 $
 * $Author: dkatzel $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0
 */
public final class Log {

    /** No instantiation, please. */
    private Log() {
    }

    /**
     * Constant to indicate an explicitly invalid message severity
     * setting.  */
    public static final int INVALID = -1;

    /**
     * Constant to indicate an informational message.  */
    public static final int INFO = 0;

    /**
     * Constant to indicate a debug message.  */
    public static final int DEBUG = 1;

    /**
     * Constant to indicate a warning message.  */
    public static final int WARN = 2;

    /**
     * Constant to indicate a warning message.  */
    public static final int ERROR = 3;

    /**
     * Constant to indicate a fatal message.  */
    public static final int FATAL = 4;

    /**
     * Stringified INVALID severity */
    public static final String INVALID_STRING = ResourceUtil.getResource(Log.class, "text.invalid");

    /**
     * Stringified INFO severity */
    public static final String INFO_STRING = ResourceUtil.getResource(Log.class, "text.info");

    /**
     * Stringified DEBUG severity */
    public static final String DEBUG_STRING = ResourceUtil.getResource(Log.class, "text.debug");

    /**
     * Stringified WARN severity */
    public static final String WARN_STRING = ResourceUtil.getResource(Log.class, "text.warn");

    /**
     * Stringified ERROR severity */
    public static final String ERROR_STRING = ResourceUtil.getResource(Log.class, "text.error");

    /**
     * Stringified FATAL severity */
    public static final String FATAL_STRING = ResourceUtil.getResource(Log.class, "text.fatal");

    /**
     * A list of registered ILog implementers.  */
    private static Vector implementers = new Vector();

    /**
     * Describe variable <code>SHOULD_LOG_INFO</code> here.
     *
     *
     */
    private static boolean SHOULD_LOG_INFO = true;

    ;

    /**
     * Describe variable <code>SHOULD_LOG_DEBUG</code> here.
     *
     *
     */
    private static boolean SHOULD_LOG_DEBUG = true;

    static {
        String value;
        value = System.getProperty("log.should_log_debug");
        if (value != null) {
            if (value.equals("0") || value.equals("false")) {
                Log.SHOULD_LOG_DEBUG = false;
            }
        }
        value = System.getProperty("log.should_log_info");
        if (value != null) {
            if (value.equals("0") || value.equals("false")) {
                Log.SHOULD_LOG_INFO = false;
            }
        }
    }

    /**
     * Register a log implementer.
     * @param log an <code>ILog</code> value
     */
    public static void addLog(ILog log) {
        Log.implementers.add(log);
    }

    /**
     * Remove a log implementer.
     * @param log an <code>ILog</code> value
     */
    public static void removeLog(ILog log) {
        Log.implementers.remove(log);
    }

    /**
     * Standard logging method.
     * @param severity an <code>int</code> value
     * @param throwable a <code>Throwable</code> value
     * @param message an <code>IMessageResource</code> value
     */
    public static void log(int severity, Throwable throwable, IMessageResource message) {
        Log.log(severity, throwable, null, message);
    }

    /**
     * Logging method for caught exceptions.
     * @param severity an <code>int</code> value
     * @param throwable a <code>Throwable</code> value
     * @param caught an <code>Throwable</code> value
     * @param message an <code>IMessageResource</code> value
     */
    public static void log(int severity, Throwable throwable, Throwable caught, IMessageResource message) {
        DefaultLogEntry logEntry = new DefaultLogEntry(severity, throwable, message, caught);
        LogUtil.parseThrowableInfo(throwable, logEntry);
        Iterator iter = Log.implementers.iterator();
        ILog log;
        if ((severity != Log.INFO && severity != Log.DEBUG) || (severity == Log.INFO && Log.SHOULD_LOG_INFO) || (severity == Log.DEBUG && Log.SHOULD_LOG_DEBUG)) {
            while (iter.hasNext()) {
                log = (ILog) iter.next();
                log.log(logEntry);
            }
            if (Log.implementers.size() == 0) {
                System.out.println(message);
            }
        }
    }

    /**
     * Return the String representation of the specified severity code.
     * @param severity an <code>int</code> value
     * @return a <code>String</code> value
     */
    public static String severityToString(int severity) {
        String ret;
        switch(severity) {
            case Log.INFO:
                ret = Log.INFO_STRING;
                break;
            case Log.DEBUG:
                ret = Log.DEBUG_STRING;
                break;
            case Log.WARN:
                ret = Log.WARN_STRING;
                break;
            case Log.ERROR:
                ret = Log.ERROR_STRING;
                break;
            case Log.FATAL:
                ret = Log.FATAL_STRING;
                break;
            default:
                ret = Log.INVALID_STRING;
                break;
        }
        return ret;
    }
}
