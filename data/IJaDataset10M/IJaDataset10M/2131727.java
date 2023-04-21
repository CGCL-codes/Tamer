package com.bkoenig.photo.toolkit.utils;

import java.io.File;
import org.apache.log4j.*;

public class Logger {

    public static org.apache.log4j.Logger logger = null;

    public static int logLevel = 0;

    public static String OFF = "OFF";

    public static String DEBUG = "DEBUG";

    public static String INFO = "INFO";

    public static String WARN = "WARN";

    public static String ERROR = "ERROR";

    private static void createLogger() {
        try {
            File tmp = new File("PhotoToolkit.log");
            if (tmp.length() > 5242880) tmp.delete();
            logger = org.apache.log4j.Logger.getRootLogger();
            logger.setLevel(Level.OFF);
            FileAppender fileAppender = new FileAppender(new PatternLayout("%d{ISO8601} %-5p %m%n"), "PhotoToolkit.log", true);
            logger.addAppender(fileAppender);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void debug(String msg) {
        if (logger == null) createLogger();
        logger.debug("[" + new Exception().getStackTrace()[1].getClassName() + ":" + new Exception().getStackTrace()[1].getLineNumber() + "] " + msg);
    }

    public static void info(String msg) {
        if (logger == null) createLogger();
        logger.info("[" + new Exception().getStackTrace()[1].getClassName() + ":" + new Exception().getStackTrace()[1].getLineNumber() + "] " + msg);
    }

    public static void warn(String msg) {
        if (logger == null) createLogger();
        logger.warn("[" + new Exception().getStackTrace()[1].getClassName() + ":" + new Exception().getStackTrace()[1].getLineNumber() + "] " + msg);
    }

    public static void error(String msg) {
        if (logger == null) createLogger();
        logger.error("[" + new Exception().getStackTrace()[1].getClassName() + ":" + new Exception().getStackTrace()[1].getLineNumber() + "] " + msg);
    }

    public static void setLogLevel(String level) {
        if (logger == null) createLogger();
        if (level.equals(OFF)) logger.setLevel(Level.OFF); else if (level.equals(ERROR)) logger.setLevel(Level.ERROR); else if (level.equals(WARN)) logger.setLevel(Level.WARN); else if (level.equals(INFO)) logger.setLevel(Level.INFO); else if (level.equals(DEBUG)) logger.setLevel(Level.DEBUG); else logger.setLevel(Level.ALL);
    }
}
