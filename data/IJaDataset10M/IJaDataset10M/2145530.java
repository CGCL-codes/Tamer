package org.swxjava.util;

public interface Log {

    void debug(Object arg0);

    void debug(Object arg0, Throwable arg1);

    void error(Object arg0, Throwable arg1);

    void error(Object arg0);

    void fatal(Object arg0, Throwable arg1);

    void fatal(Object arg0);

    void info(Object arg0, Throwable arg1);

    void info(Object arg0);

    void trace(Object arg0, Throwable arg1);

    void trace(Object arg0);

    void warn(Object arg0, Throwable arg1);

    void warn(Object arg0);

    boolean isDebugEnabled();

    boolean isErrorEnabled();

    boolean isFatalEnabled();

    boolean isInfoEnabled();

    boolean isTraceEnabled();

    boolean isWarnEnabled();
}
