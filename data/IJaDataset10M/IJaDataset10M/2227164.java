package com.googlecode.httl.support.loggers;

import java.io.Serializable;
import java.util.logging.Level;
import com.googlecode.httl.support.Constants;
import com.googlecode.httl.support.Logger;

/**
 * JdkLogger
 * 
 * @author Liang Fei (liangfei0201 AT gmail DOT com)
 */
public class JdkLogger implements Logger, Serializable {

    private static final long serialVersionUID = 1L;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Constants.HTTL);

    public void debug(String msg) {
        logger.log(Level.FINE, msg);
    }

    public void debug(String msg, Throwable e) {
        logger.log(Level.FINE, msg, e);
    }

    public void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    public void info(String msg, Throwable e) {
        logger.log(Level.INFO, msg, e);
    }

    public void warn(String msg) {
        logger.log(Level.WARNING, msg);
    }

    public void warn(String msg, Throwable e) {
        logger.log(Level.WARNING, msg, e);
    }

    public void error(String msg) {
        logger.log(Level.SEVERE, msg);
    }

    public void error(String msg, Throwable e) {
        logger.log(Level.SEVERE, msg, e);
    }

    public boolean isDebugEnabled() {
        return logger.isLoggable(Level.FINE);
    }

    public boolean isInfoEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    public boolean isWarnEnabled() {
        return logger.isLoggable(Level.WARNING);
    }

    public boolean isErrorEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }

    public boolean isFatalEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }
}
