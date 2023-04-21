package org.gecko.jee.community.mobidick.tracability;

import org.apache.commons.logging.Log;

public class LoggerDefault implements Logger {

    private static final String CONCATENATION_STRING = " => ";

    private static final String EMPTY_STRING = "";

    private Log logger;

    public void debug(final String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public void debug(final String message, final Object objectToTrace) {
        if (logger.isDebugEnabled()) {
            if (message == null) {
                logger.debug(objectToTrace);
            } else {
                logger.debug(message + LoggerDefault.CONCATENATION_STRING + objectToTrace);
            }
        }
    }

    public void error(final String message, final Object objectToTrace, final Throwable cause) {
        if (logger.isErrorEnabled()) {
            if (message == null) {
                logger.error(objectToTrace, cause);
            } else {
                logger.error(message + LoggerDefault.CONCATENATION_STRING + objectToTrace, cause);
            }
        }
    }

    public void error(final String message, final Throwable cause) {
        if (logger.isErrorEnabled()) {
            logger.error(message, cause);
        }
    }

    public void error(final Throwable cause) {
        if (logger.isErrorEnabled()) {
            logger.error(LoggerDefault.EMPTY_STRING, cause);
        }
    }

    public void fatal(final String message, final Object objectToTrace, final Throwable cause) {
        if (logger.isFatalEnabled()) {
            if (message == null) {
                logger.fatal(objectToTrace, cause);
            } else {
                logger.fatal(message + LoggerDefault.CONCATENATION_STRING + objectToTrace, cause);
            }
        }
    }

    public void fatal(final String message, final Throwable cause) {
        if (logger.isFatalEnabled()) {
            logger.fatal(message, cause);
        }
    }

    public void fatal(final Throwable cause) {
        if (logger.isFatalEnabled()) {
            logger.fatal(LoggerDefault.EMPTY_STRING, cause);
        }
    }

    public void info(final String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public void info(final String message, final Object objectToTrace) {
        if (logger.isInfoEnabled()) {
            if (message == null) {
                logger.info(objectToTrace);
            } else {
                logger.info(message + LoggerDefault.CONCATENATION_STRING + objectToTrace);
            }
        }
    }

    protected void setLogger(final Log logger) {
        this.logger = logger;
    }

    public void warn(final String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    public void warn(final String message, final Object objectToTrace) {
        if (logger.isWarnEnabled()) {
            if (message == null) {
                logger.warn(objectToTrace);
            } else {
                logger.warn(message + LoggerDefault.CONCATENATION_STRING + objectToTrace);
            }
        }
    }

    public void warn(final String message, final Object objectToTrace, final Throwable cause) {
        if (logger.isWarnEnabled()) {
            if (message == null) {
                logger.warn(objectToTrace, cause);
            } else {
                logger.warn(message + LoggerDefault.CONCATENATION_STRING + objectToTrace, cause);
            }
        }
    }
}
