package com.blogspot.qbeukes.logging;

/**
 *
 * @author quintin
 */
public abstract class Log {

    public static enum Level {

        TRACE, DEBUG, INFO, WARN, ERROR, FATAL
    }

    ;

    public static Log getLog(Class<?> clazz) {
        return new SimpleLog(clazz);
    }

    public abstract void trace(String msg);

    public abstract void debug(String msg);

    public abstract void info(String msg);

    public abstract void warn(String msg);

    public abstract void error(String msg);

    public abstract void fatal(String msg);

    public abstract void trace(String msg, Throwable t);

    public abstract void debug(String msg, Throwable t);

    public abstract void info(String msg, Throwable t);

    public abstract void warn(String msg, Throwable t);

    public abstract void error(String msg, Throwable t);

    public abstract void fatal(String msg, Throwable t);
}
