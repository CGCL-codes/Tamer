package org.ujorm.logger;

import java.util.logging.*;

/**
 * Bridge to logging framework JSF4J.
 * @author Ponec
 */
public final class UjoLoggerFactory implements UjoLogger {

    /** SLF4J Support */
    private static volatile boolean slf4jSupport = true;

    /** Sign to show a log */
    private static volatile boolean showLog = true;

    /** Target Logger */
    private final Logger logger;

    private UjoLoggerFactory(String name) {
        this.logger = java.util.logging.Logger.getLogger(name);
    }

    /** Konstructor */
    public UjoLoggerFactory(Class name) {
        this(name.getName());
    }

    /** {@inheritdoc} */
    public boolean isLoggable(Level level) {
        return logger.isLoggable(level);
    }

    /** {@inheritdoc} */
    public void log(Level level, String message) {
        logger.log(level, message);
    }

    /** {@inheritdoc} */
    public void log(Level level, String message, Throwable e) {
        logger.log(level, message, e);
    }

    /** {@inheritdoc} */
    public void log(Level level, String message, Object... parameters) {
        logger.log(level, message, parameters);
    }

    public static UjoLogger getLogger(Class<?> name) {
        return slf4jSupport ? newUjoLoggerBridge2Slf4j(name) : new UjoLoggerFactory(name);
    }

    private static UjoLogger newUjoLoggerBridge2Slf4j(Class name) {
        UjoLogger result;
        try {
            result = new UjoLoggerBridge2Slf4j(name);
            if (showLog) {
                showLog = false;
                result.log(Level.INFO, "Ujorm logging is switched to the SLF4J.");
            }
        } catch (Throwable e) {
            slf4jSupport = false;
            result = new UjoLoggerFactory(name);
            result.log(Level.INFO, "Ujorm logging is switched to the JUL.");
        }
        return result;
    }
}
