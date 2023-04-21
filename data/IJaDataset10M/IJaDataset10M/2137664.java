package com.peterhi.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogMacros {

    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(new PeterHiFormatter());
        logger.addHandler(ch);
        logger.info("NEW JAVA.UTIL.LOGGING LOG SESSION (" + name + ") STARTED");
        return logger;
    }

    public static final void info(Logger logger, String msg, Object... objects) {
        LogRecord lr = new LogRecord(Level.INFO, msg);
        lr.setParameters(objects);
        Exception ex = new Exception();
        if ((ex.getStackTrace() != null) && (ex.getStackTrace().length > 1)) {
            lr.setSourceClassName(ex.getStackTrace()[1].getClassName());
            lr.setSourceMethodName(ex.getStackTrace()[1].getMethodName() + " " + ex.getStackTrace()[1].getLineNumber());
        }
        logger.log(lr);
    }

    public static final void warn(Logger logger, String msg, Object... objects) {
        LogRecord lr = new LogRecord(Level.WARNING, msg);
        lr.setParameters(objects);
        Exception ex = new Exception();
        if ((ex.getStackTrace() != null) && (ex.getStackTrace().length > 1)) {
            lr.setSourceClassName(ex.getStackTrace()[1].getClassName());
            lr.setSourceMethodName(ex.getStackTrace()[1].getMethodName() + " " + ex.getStackTrace()[1].getLineNumber());
        }
        logger.log(lr);
    }

    public static final void enter(Logger logger, String cls, String method, Object... objects) {
        logger.entering(cls, method, objects);
    }

    public static final void exit(Logger logger, String cls, String method, Object... objects) {
        logger.exiting(cls, method, objects);
    }

    public static final void throwing(Logger logger, String cls, String method, Throwable th, Object... objects) {
        LogRecord lr = new LogRecord(Level.SEVERE, "");
        lr.setSourceClassName(cls);
        lr.setSourceMethodName(method);
        lr.setThrown(th);
        lr.setParameters(objects);
        logger.log(lr);
    }
}
