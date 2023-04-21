package com.pl.itsense.ftsm.common.log;

import org.apache.log4j.Logger;

public class FTSMLogger {

    public static enum Level {

        DEBUG, INFO, ERROR
    }

    private final Logger logger;

    public FTSMLogger(Logger logger) {
        this.logger = logger;
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public Level getLevel() {
        if (logger.getLevel() == org.apache.log4j.Level.DEBUG) {
            return Level.DEBUG;
        } else {
            if (logger.getLevel() == org.apache.log4j.Level.INFO) {
                return Level.INFO;
            } else {
                if (logger.getLevel() == org.apache.log4j.Level.ERROR) {
                    return Level.ERROR;
                }
            }
        }
        return null;
    }

    public static FTSMLogger getLogger(Class logClass) {
        return new FTSMLogger(Logger.getLogger(logClass));
    }
}
