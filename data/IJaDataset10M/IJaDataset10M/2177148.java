package org.iocframework.log;

/**
 * @author David M. Sledge
 *
 */
public interface LoggerFacade {

    void trace(Class<?> cls, Throwable t, String msg, Object... args);

    void debug(Class<?> cls, Throwable t, String msg, Object... args);

    void info(Class<?> cls, Throwable t, String msg, Object... args);

    void warn(Class<?> cls, Throwable t, String msg, Object... args);

    void error(Class<?> cls, Throwable t, String msg, Object... args);

    void fatal(Class<?> cls, Throwable t, String msg, Object... args);
}
