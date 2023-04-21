package net.sf.catchup.common.logging;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * This class is responsible for logging the message passed by the caller. The
 * logging works simply by calling the corresponding method in the apache log
 * api
 * 
 * @author Ramachandra
 * 
 */
class SimpleLogger implements ILogger {

    private static Map<String, Logger> loggerPool = new HashMap<String, Logger>();

    public void debug(String message) {
        getLogger(getClassname()).debug(message);
    }

    public void debug(String message, Throwable e) {
        getLogger(getClassname()).debug(message, e);
    }

    public void debug(Throwable e) {
        getLogger(getClassname()).debug(e);
    }

    public void error(String message) {
        getLogger(getClassname()).error(message);
    }

    public void error(String message, Throwable e) {
        getLogger(getClassname()).error(message, e);
    }

    public void error(Throwable e) {
        getLogger(getClassname()).error(e);
    }

    public void info(String message) {
        getLogger(getClassname()).info(message);
    }

    public void info(String message, Throwable e) {
        getLogger(getClassname()).info(message, e);
    }

    public void info(Throwable e) {
        getLogger(getClassname()).info(e);
    }

    public void warn(String message) {
        getLogger(getClassname()).warn(message);
    }

    public void warn(String message, Throwable e) {
        getLogger(getClassname()).warn(message, e);
    }

    public void warn(Throwable e) {
        getLogger(getClassname()).warn(e);
    }

    private Logger getLogger(final String className) {
        synchronized (loggerPool) {
            if (loggerPool.containsKey(className)) {
                return loggerPool.get(className);
            } else {
                final Logger logger = Logger.getLogger(className);
                loggerPool.put(className, logger);
                return logger;
            }
        }
    }

    private synchronized String getClassname() {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        final String className;
        if (stackTrace.length > 3) {
            className = stackTrace[3].getClassName();
        } else {
            className = stackTrace[stackTrace.length - 1].getClassName();
        }
        return className;
    }
}
