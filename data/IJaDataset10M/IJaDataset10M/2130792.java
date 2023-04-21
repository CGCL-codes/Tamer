package net.bull.javamelody;

import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;

/**
 * Logs des requêtes http exécutées et logs internes.
 * @author Emeric Vernat
 */
final class LOG {

    static final boolean LOG4J_ENABLED = isLog4jEnabled();

    static final boolean LOGBACK_ENABLED = isLogbackEnabled();

    private static final String INTERNAL_LOGGER_NAME = "net.bull.javamelody";

    private LOG() {
        super();
    }

    @SuppressWarnings("unused")
    static void logHttpRequest(HttpServletRequest httpRequest, String requestName, long duration, boolean systemError, int responseSize, String filterName) {
        if (LOGBACK_ENABLED) {
            logback(httpRequest, duration, systemError, responseSize, filterName);
        } else if (LOG4J_ENABLED) {
            log4j(httpRequest, duration, systemError, responseSize, filterName);
        } else {
            final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(filterName);
            if (logger.isLoggable(Level.INFO)) {
                logger.info(buildLogMessage(httpRequest, duration, systemError, responseSize));
            }
        }
    }

    private static void log4j(HttpServletRequest httpRequest, long duration, boolean systemError, int responseSize, String filterName) {
        final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(filterName);
        if (logger.isInfoEnabled()) {
            logger.info(buildLogMessage(httpRequest, duration, systemError, responseSize));
        }
    }

    private static void logback(HttpServletRequest httpRequest, long duration, boolean systemError, int responseSize, String filterName) {
        final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(filterName);
        if (logger.isInfoEnabled()) {
            logger.info(buildLogMessage(httpRequest, duration, systemError, responseSize));
        }
    }

    private static String buildLogMessage(HttpServletRequest httpRequest, long duration, boolean systemError, int responseSize) {
        final StringBuilder msg = new StringBuilder();
        msg.append("remoteAddr = ").append(httpRequest.getRemoteAddr());
        final String forwardedFor = httpRequest.getHeader("X-Forwarded-For");
        if (forwardedFor != null) {
            msg.append(", forwardedFor = ").append(forwardedFor);
        }
        msg.append(", request = ").append(httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()));
        if (httpRequest.getQueryString() != null) {
            msg.append('?').append(httpRequest.getQueryString());
        }
        msg.append(' ').append(httpRequest.getMethod());
        msg.append(": ").append(duration).append(" ms");
        if (systemError) {
            msg.append(", erreur");
        }
        msg.append(", ").append(responseSize / 1024).append(" Ko");
        return msg.toString();
    }

    static void debug(String msg) {
        if (LOGBACK_ENABLED) {
            org.slf4j.LoggerFactory.getLogger(INTERNAL_LOGGER_NAME).debug(msg);
        } else if (LOG4J_ENABLED) {
            final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(INTERNAL_LOGGER_NAME);
            logger.debug(msg);
        } else {
            final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(INTERNAL_LOGGER_NAME);
            logger.log(Level.FINE, msg);
        }
    }

    static void debug(String msg, Throwable throwable) {
        if (LOGBACK_ENABLED) {
            org.slf4j.LoggerFactory.getLogger(INTERNAL_LOGGER_NAME).debug(msg, throwable);
        } else if (LOG4J_ENABLED) {
            final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(INTERNAL_LOGGER_NAME);
            logger.debug(msg, throwable);
        } else {
            final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(INTERNAL_LOGGER_NAME);
            logger.log(Level.FINE, msg, throwable);
        }
    }

    static void info(String msg, Throwable throwable) {
        if (LOGBACK_ENABLED) {
            org.slf4j.LoggerFactory.getLogger(INTERNAL_LOGGER_NAME).info(msg, throwable);
        } else if (LOG4J_ENABLED) {
            final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(INTERNAL_LOGGER_NAME);
            logger.info(msg, throwable);
        } else {
            final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(INTERNAL_LOGGER_NAME);
            logger.log(Level.INFO, msg, throwable);
        }
    }

    static void warn(String msg, Throwable throwable) {
        try {
            if (LOGBACK_ENABLED) {
                org.slf4j.LoggerFactory.getLogger(INTERNAL_LOGGER_NAME).warn(msg, throwable);
            } else if (LOG4J_ENABLED) {
                final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(INTERNAL_LOGGER_NAME);
                logger.warn(msg, throwable);
            } else {
                final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(INTERNAL_LOGGER_NAME);
                logger.log(Level.WARNING, msg, throwable);
            }
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    private static boolean isLog4jEnabled() {
        try {
            Class.forName("org.apache.log4j.Logger");
            Class.forName("org.apache.log4j.AppenderSkeleton");
            return true;
        } catch (final Throwable e) {
            return false;
        }
    }

    private static boolean isLogbackEnabled() {
        try {
            Class.forName("ch.qos.logback.classic.Logger");
            return Class.forName("ch.qos.logback.classic.LoggerContext").isAssignableFrom(LoggerFactory.getILoggerFactory().getClass());
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }
}
