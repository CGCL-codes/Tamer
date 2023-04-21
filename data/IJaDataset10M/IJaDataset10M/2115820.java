package com.hyper9.uvapi;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import com.hyper9.common.beans.Bean;
import com.hyper9.uvapi.types.ConnectorBean;
import com.hyper9.uvapi.types.virt.resources.entities.EntityBean;

/**
 * A logging utility class.
 * 
 * @author akutz
 * 
 */
public class Log {

    private static final String LOGGER_NAME = "com.hyper9.uvapi";

    /**
     * Logs a message.
     * 
     * @param level The log level.
     * @param connector The connector bean
     * @param bean The bean to log (It's okay to pass a null value).
     * @param message The message to log.
     * @param throwable The throwable to log (It's okay to pass a null value).
     */
    public static void log(final Level level, final ConnectorBean connector, final Bean bean, final String message, final Throwable throwable) {
        final Thread currentThread = Thread.currentThread();
        final StackTraceElement[] stels = currentThread.getStackTrace();
        int stackLevel = 3;
        String methodName = stels[stackLevel].getMethodName();
        while (methodName.equals("log") || methodName.equals("error") || methodName.equals("debug") || methodName.equals("info") || methodName.equals("logOnLevel")) {
            ++stackLevel;
            methodName = stels[stackLevel].getMethodName();
        }
        final String targetAddress;
        final String targetUserName;
        if (connector == null || connector.getConnectionInformation() == null) {
            targetAddress = "[targetAddress]";
            targetUserName = "[targetUserName]";
        } else {
            targetAddress = connector.getConnectionInformation().getAddress();
            targetUserName = connector.getConnectionInformation().getAddress();
        }
        String beanName = null;
        String beanType = null;
        if (bean instanceof EntityBean) {
            EntityBean ebean = (EntityBean) bean;
            beanName = ebean.getName();
        } else if (bean != null) {
            beanType = bean.getClass().getSimpleName();
        }
        log(level, targetAddress, targetUserName, methodName, beanType, beanName, message, throwable);
    }

    /**
     * Logs a message.
     * 
     * @param level
     * @param resourceName
     * @param clientAddress
     * @param clientUserName
     * @param transactionID
     * @param targetAddress
     * @param targetUserName
     * @param methodName
     * @param beanType
     * @param beanName
     * @param message
     * @param throwable
     */
    public static void log(final Level level, final String targetAddress, final String targetUserName, final String methodName, final String beanType, final String beanName, final String message, final Throwable throwable) {
        final String toLog = String.format("%s, \"%s\", %s, %s, \"%s\", \"%s\"", targetAddress == null ? "[targetAddress]" : targetAddress, targetUserName == null ? "[targetUserName]" : targetUserName, methodName == null ? "[methodName]" : methodName, beanType == null ? "[beanType]" : beanType, beanName == null ? "[beanName]" : beanName, message == null ? "[message]" : message);
        Logger l = Logger.getLogger(LOGGER_NAME);
        if (throwable == null) {
            l.log(level, toLog);
        } else {
            l.log(level, toLog, throwable);
        }
    }

    /**
     * Gets the current log level.
     * 
     * @return The current log level.
     */
    public static Level getLevel() {
        Logger l = Logger.getLogger(LOGGER_NAME);
        return l.getLevel();
    }

    /**
     * Writes a log message.
     * 
     * @param level The intended log level.
     * @param connector
     * @param message The message to log.
     */
    public static void log(final Level level, final ConnectorBean connector, final String message) {
        log(level, connector, null, message, null);
    }

    /**
     * Writes a log message.
     * 
     * @param level The intended log level.
     * @param connector
     * @param message The message to log.
     * @param bean The object that is the target of the log message.
     */
    public static void log(final Level level, final ConnectorBean connector, final String message, final Bean bean) {
        log(level, connector, bean, message, null);
    }

    /**
     * Writes a log message.
     * 
     * @param level The intended log level.
     * @param connector
     * @param message The message to log.
     * @param throwable The exception being logged.
     */
    public static void log(final Level level, final ConnectorBean connector, final String message, final Throwable throwable) {
        log(level, connector, null, message, throwable);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * @param message The message to log.
     */
    public static void debug(final ConnectorBean connector, final String message) {
        log(Level.DEBUG, connector, null, message, null);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     * @param bean The object that is the target of the log message.
     */
    public static void debug(final ConnectorBean connector, final String message, final Bean bean) {
        log(Level.DEBUG, connector, bean, message, null);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     * @param throwable The exception being logged.
     */
    public static void debug(final ConnectorBean connector, final String message, final Throwable throwable) {
        log(Level.DEBUG, connector, null, message, throwable);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     * @param bean The object that is the target of the log message.
     * @param throwable The exception being logged.
     */
    public static void debug(final ConnectorBean connector, final String message, final Bean bean, final Throwable throwable) {
        log(Level.DEBUG, connector, bean, message, throwable);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     */
    public static void info(final ConnectorBean connector, final String message) {
        log(Level.INFO, connector, null, message, null);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     * @param bean The object that is the target of the log message.
     */
    public static void info(final ConnectorBean connector, final String message, final Bean bean) {
        log(Level.INFO, connector, bean, message, null);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     * @param throwable The exception being logged.
     */
    public static void info(final ConnectorBean connector, final String message, final Throwable throwable) {
        log(Level.INFO, connector, null, message, throwable);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     * @param bean The object that is the target of the log message.
     * @param throwable The exception being logged.
     */
    public static void info(final ConnectorBean connector, final String message, final Bean bean, final Throwable throwable) {
        log(Level.INFO, connector, bean, message, throwable);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     */
    public static void error(final ConnectorBean connector, final String message) {
        log(Level.ERROR, connector, null, message, null);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     * @param bean The object that is the target of the log message.
     */
    public static void error(final ConnectorBean connector, final String message, final Bean bean) {
        log(Level.ERROR, connector, bean, message, null);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     * @param throwable The exception being logged.
     */
    public static void error(final ConnectorBean connector, final String message, final Throwable throwable) {
        log(Level.ERROR, connector, null, message, throwable);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     * @param bean The object that is the target of the log message.
     * @param throwable The exception being logged.
     */
    public static void error(final ConnectorBean connector, final String message, final Bean bean, final Throwable throwable) {
        log(Level.ERROR, connector, bean, message, throwable);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     */
    public static void warn(final ConnectorBean connector, final String message) {
        log(Level.WARN, connector, null, message, null);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     * @param bean The object that is the target of the log message.
     */
    public static void warn(final ConnectorBean connector, final String message, final Bean bean) {
        log(Level.WARN, connector, bean, message, null);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * 
     * @param message The message to log.
     * @param throwable The exception being logged.
     */
    public static void warn(final ConnectorBean connector, final String message, final Throwable throwable) {
        log(Level.WARN, connector, null, message, throwable);
    }

    /**
     * Writes a log message.
     * 
     * @param connector
     * @param message The message to log.
     * @param bean The object that is the target of the log message.
     * @param throwable The exception being logged.
     */
    public static void warn(final ConnectorBean connector, final String message, final Bean bean, final Throwable throwable) {
        log(Level.WARN, connector, bean, message, throwable);
    }

    /**
     * 
     * @param connector
     * @param message
     * @param e
     */
    public static void logOnLevel(final ConnectorBean connector, final String message, final Exception e) {
        final Level lvl = Log.getLevel();
        debug(connector, "lvl=" + lvl);
        final boolean lvlGrtr = lvl.isGreaterOrEqual(Level.INFO);
        debug(connector, "lvlGrtr=" + lvlGrtr);
        if (lvlGrtr) {
            info(connector, message);
        } else {
            debug(connector, message, e);
        }
    }
}
