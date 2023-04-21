package com.noahsloan.nutils.log;

import java.util.logging.Level;

/**
 * Wrapper around Java logging because I prefer the Commons Logging/log4j way.
 * 
 * @author noah
 * 
 */
public class Logger {

    private java.util.logging.Logger log;

    public Logger(java.util.logging.Logger logger) {
        this.log = logger;
    }

    public void log(Level level, String msg) {
        log.log(level, msg);
    }

    public void log(Level level, String msg, Throwable thrown) {
        log.log(level, msg, thrown);
    }

    public void trace(String msg) {
        log.log(Level.FINER, msg);
    }

    public void trace(String msg, Throwable thrown) {
        log.log(Level.FINER, msg, thrown);
    }

    public void debug(String msg) {
        log.log(Level.FINE, msg);
    }

    public void debug(String msg, Throwable thrown) {
        log.log(Level.FINE, msg, thrown);
    }

    public void warn(String msg) {
        log.log(Level.WARNING, msg);
    }

    public void warn(String msg, Throwable thrown) {
        log.log(Level.WARNING, msg, thrown);
    }

    public void info(String msg) {
        log.log(Level.INFO, msg);
    }

    public void info(String msg, Throwable thrown) {
        log.log(Level.INFO, msg, thrown);
    }

    public void error(String msg) {
        log.log(Level.SEVERE, msg);
    }

    public void error(String msg, Throwable thrown) {
        log.log(Level.SEVERE, msg, thrown);
    }

    public static Logger getLogger(Class<?> c) {
        return new Logger(java.util.logging.Logger.getLogger(c.getCanonicalName()));
    }
}
