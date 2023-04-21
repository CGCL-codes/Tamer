package org.peaseplate.utils.log.internal;

import org.peaseplate.utils.log.LogLevel;
import org.slf4j.Logger;

public class SLF4JLogHandler extends AbstractLogHandler {

    private final Logger logger;

    public SLF4JLogHandler(final Logger logger) {
        super();
        this.logger = logger;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean isEnabled(final LogLevel level, final String name) {
        switch(level) {
            case DEBUG:
                return logger.isDebugEnabled();
            case INFO:
                return logger.isInfoEnabled();
            case WARN:
                return logger.isWarnEnabled();
            case ERROR:
            case FATAL:
                return logger.isErrorEnabled();
            default:
                throw new UnsupportedOperationException("The log level " + level + " is not considered, here. This may indicate a bug");
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void log(final LogLevel level, final String name, final String message) {
        switch(level) {
            case DEBUG:
                if (logger.isDebugEnabled()) {
                    logger.debug(message);
                }
                break;
            case INFO:
                if (logger.isInfoEnabled()) {
                    logger.info(message);
                }
                break;
            case WARN:
                if (logger.isWarnEnabled()) {
                    logger.warn(message);
                }
                break;
            case ERROR:
            case FATAL:
                if (logger.isErrorEnabled()) {
                    logger.error(message);
                }
                break;
            default:
                throw new UnsupportedOperationException("The log level " + level + " is not considered, here. This may indicate a bug");
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void log(final LogLevel level, final String name, final String message, final Throwable e) {
        switch(level) {
            case DEBUG:
                if (logger.isDebugEnabled()) {
                    logger.debug(message, e);
                }
                break;
            case INFO:
                if (logger.isInfoEnabled()) {
                    logger.info(message, e);
                }
                break;
            case WARN:
                if (logger.isWarnEnabled()) {
                    logger.warn(message, e);
                }
                break;
            case ERROR:
            case FATAL:
                if (logger.isErrorEnabled()) {
                    logger.error(message, e);
                }
                break;
            default:
                throw new UnsupportedOperationException("The log level " + level + " is not considered, here. This may indicate a bug");
        }
    }
}
