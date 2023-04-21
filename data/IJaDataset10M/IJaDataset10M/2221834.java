package com.makotan.logger;

import com.makotan.logger.spi.InternalLogger;

/**
 * Loggerです
 * @author makoto
 *
 */
public class Logger {

    private InternalLogger logger;

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = new Logger();
        logger.logger = LoggerManager.getLogger(clazz);
        return logger;
    }

    public static Logger getLogger(String name) {
        Logger logger = new Logger();
        logger.logger = LoggerManager.getLogger(name);
        return logger;
    }

    public LoggingLevel getEffectiveLevel() {
        return logger.getEffectiveLevel();
    }

    public boolean isEnabledFor(LoggingLevel level) {
        return LoggerManager.isNomalEnabledFor(logger, level);
    }

    public boolean isTraceEnabled() {
        return isEnabledFor(LoggingLevel.TRACE);
    }

    public void trace(Object message) {
        log(LoggingLevel.TRACE, message, null, (Object[]) null);
    }

    public void trace(String message, Object... params) {
        log(LoggingLevel.TRACE, message, null, params);
    }

    public void trace(Object message, Throwable t) {
        log(LoggingLevel.TRACE, message, t, (Object[]) null);
    }

    public void trace(String message, Throwable t, Object... params) {
        log(LoggingLevel.TRACE, message, t, params);
    }

    public boolean isDebugEnabled() {
        return isEnabledFor(LoggingLevel.DEBUG);
    }

    public void debug(Object message) {
        log(LoggingLevel.DEBUG, message, null, (Object[]) null);
    }

    public void debug(String message, Object... params) {
        log(LoggingLevel.DEBUG, message, null, params);
    }

    public void debug(Object message, Throwable t) {
        log(LoggingLevel.DEBUG, message, t, (Object[]) null);
    }

    public void debug(String message, Throwable t, Object... params) {
        log(LoggingLevel.DEBUG, message, t, params);
    }

    public boolean isInfoEnabled() {
        return isEnabledFor(LoggingLevel.INFO);
    }

    public void info(Object message) {
        log(LoggingLevel.INFO, message, null, (Object[]) null);
    }

    public void info(String message, Object... params) {
        log(LoggingLevel.INFO, message, null, params);
    }

    public void info(Object message, Throwable t) {
        log(LoggingLevel.INFO, message, t, (Object[]) null);
    }

    public void info(String message, Throwable t, Object... params) {
        log(LoggingLevel.INFO, message, t, params);
    }

    public void warn(Object message) {
        log(LoggingLevel.WARN, message, null, (Object[]) null);
    }

    public void warn(String message, Object... params) {
        log(LoggingLevel.WARN, message, null, params);
    }

    public void warn(Object message, Throwable t) {
        log(LoggingLevel.WARN, message, t, (Object[]) null);
    }

    public void warn(String message, Throwable t, Object... params) {
        log(LoggingLevel.WARN, message, t, params);
    }

    public void error(Object message) {
        log(LoggingLevel.ERROR, message, null, (Object[]) null);
    }

    public void error(String message, Object... params) {
        log(LoggingLevel.ERROR, message, null, params);
    }

    public void error(Object message, Throwable t) {
        log(LoggingLevel.ERROR, message, t, (Object[]) null);
    }

    public void error(String message, Throwable t, Object... params) {
        log(LoggingLevel.ERROR, message, t, params);
    }

    public void fatal(Object message) {
        log(LoggingLevel.FATAL, message, null, (Object[]) null);
    }

    public void fatal(String message, Object... params) {
        log(LoggingLevel.FATAL, message, null, params);
    }

    public void fatal(Object message, Throwable t) {
        log(LoggingLevel.FATAL, message, t, (Object[]) null);
    }

    public void fatal(String message, Throwable t, Object... params) {
        log(LoggingLevel.FATAL, message, t, params);
    }

    public void log(LoggingLevel level, String message, Throwable t, Object... params) {
        LoggingInfo info = new LoggingInfo();
        info.level = level;
        info.logger = logger;
        info.messageStr = message;
        info.params = params;
        info.threadId = Thread.currentThread().getId();
        info.threadName = Thread.currentThread().getName();
        info.thrown = t;
        info.timestamp = System.currentTimeMillis();
        StackTraceElement st = getLoggingPoint();
        info.sourceClassName = st.getClassName();
        info.sourceFileName = st.getFileName();
        info.sourceLineNo = st.getLineNumber();
        info.sourceMethodName = st.getMethodName();
        st = null;
        LoggerManager.log(info);
        info = null;
    }

    public void log(LoggingLevel level, Object message, Throwable t, Object... params) {
        LoggingInfo info = new LoggingInfo();
        info.level = level;
        info.logger = logger;
        info.messageObj = message;
        info.params = params;
        info.threadId = Thread.currentThread().getId();
        info.threadName = Thread.currentThread().getName();
        info.thrown = t;
        info.timestamp = System.currentTimeMillis();
        StackTraceElement st = getLoggingPoint();
        info.sourceClassName = st.getClassName();
        info.sourceFileName = st.getFileName();
        info.sourceLineNo = st.getLineNumber();
        info.sourceMethodName = st.getMethodName();
        st = null;
        LoggerManager.log(info);
        info = null;
    }

    protected StackTraceElement getLoggingPoint() {
        Throwable th = new Throwable();
        StackTraceElement[] elements = th.getStackTrace();
        th = null;
        StackTraceElement ret = null;
        for (StackTraceElement st : elements) {
            if (isNotLoggerClass(st.getClassName())) {
                ret = st;
                break;
            }
        }
        elements = null;
        return ret;
    }

    protected boolean isNotLoggerClass(String className) {
        if (LoggerManager.isLoggerFqcn(className)) {
            return false;
        }
        return true;
    }
}
