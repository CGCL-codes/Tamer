package com.duroty.utils.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * DOCUMENT ME!
 *
 * @author DUROT
 * @version 1.0
 */
public class DLog {

    /** DOCUMENT ME! */
    public static final int DEBUG = Level.DEBUG_INT;

    /** DOCUMENT ME! */
    public static final int INFO = Level.INFO_INT;

    /** DOCUMENT ME! */
    public static final int WARN = Level.WARN_INT;

    /** DOCUMENT ME! */
    public static final int ERROR = Level.ERROR_INT;

    /** DOCUMENT ME! */
    public static final int FATAL = Level.FATAL_INT;

    /** DOCUMENT ME! */
    public static final int ALL = Level.ALL_INT;

    /** DOCUMENT ME! */
    public static final int OFF = Level.OFF_INT;

    /**
     * DOCUMENT ME!
     *
     * @param level DOCUMENT ME!
     * @param classe DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    public static void log(int level, Class classe, String message) {
        if ((message == null) || message.trim().equals("")) {
            return;
        }
        Logger logger = Logger.getLogger(classe);
        if (level == DEBUG) {
            logger.debug(message);
        } else if (level == INFO) {
            logger.info(message);
        } else if (level == WARN) {
            logger.warn(message);
        } else if (level == ERROR) {
            logger.error(message);
        } else if (level == FATAL) {
            logger.fatal(message);
        } else if (level == ALL) {
            logger.debug(message);
            logger.info(message);
            logger.warn(message);
            logger.error(message);
            logger.fatal(message);
        } else {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param level DOCUMENT ME!
     * @param classe DOCUMENT ME!
     * @param exception DOCUMENT ME!
     */
    public static void log(int level, Class classe, Exception exception) {
        String message = "Unknown Exception";
        if (exception != null) {
            message = exception.getClass().getName() + " - ";
            if (exception.getMessage() != null) {
                message = message + exception.getMessage();
            }
        }
        log(level, classe, message);
    }
}
