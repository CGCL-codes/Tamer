package net.sf.jwisp.internal.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    public static final Log getLogger(Class<?> clazz) {
        return new Log(LoggerFactory.getLogger(clazz));
    }

    public static final Log getLogger(String name) {
        return new Log(LoggerFactory.getLogger(name));
    }

    Logger logger;

    public Log(Logger logger) {
        this.logger = logger;
    }

    public void trace(String msg) {
        if (logger.isTraceEnabled()) logger.trace(msg);
    }

    public void trace(String msg, Throwable t) {
        if (logger.isTraceEnabled()) logger.trace(msg, t);
    }

    public void trace(Throwable t) {
        if (logger.isTraceEnabled()) logger.trace(null, t);
    }

    public void trace(String format, Object... args) {
        if (logger.isTraceEnabled()) logger.trace(format, args);
    }

    public void debug(String msg) {
        if (logger.isDebugEnabled()) logger.debug(msg);
    }

    public void debug(String msg, Throwable t) {
        if (logger.isDebugEnabled()) logger.debug(msg, t);
    }

    public void debug(Throwable t) {
        if (logger.isDebugEnabled()) logger.debug(null, t);
    }

    public void debug(String format, Object... args) {
        if (logger.isDebugEnabled()) logger.debug(format, args);
    }

    public void info(String msg) {
        if (logger.isInfoEnabled()) logger.info(msg);
    }

    public void info(String msg, Throwable t) {
        if (logger.isInfoEnabled()) logger.info(msg, t);
    }

    public void info(Throwable t) {
        if (logger.isInfoEnabled()) logger.info(null, t);
    }

    public void info(String format, Object... args) {
        if (logger.isInfoEnabled()) logger.info(format, args);
    }

    public void error(String msg) {
        if (logger.isErrorEnabled()) logger.error(msg);
    }

    public void error(String msg, Throwable t) {
        if (logger.isErrorEnabled()) logger.error(msg, t);
    }

    public void error(Throwable t) {
        if (logger.isErrorEnabled()) logger.error(null, t);
    }

    public void error(String format, Object... args) {
        if (logger.isErrorEnabled()) logger.error(format, args);
    }

    public void warn(String msg) {
        if (logger.isWarnEnabled()) logger.warn(msg);
    }

    public void warn(String msg, Throwable t) {
        if (logger.isWarnEnabled()) logger.warn(msg, t);
    }

    public void warn(Throwable t) {
        if (logger.isWarnEnabled()) logger.warn(null, t);
    }

    public void warn(String format, Object... args) {
        if (logger.isWarnEnabled()) logger.warn(format, args);
    }
}
