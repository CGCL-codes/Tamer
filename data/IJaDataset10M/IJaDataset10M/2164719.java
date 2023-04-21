package hu.takacsot.validator.basic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author takacsot
 * @version $Id: CommonLoggingHandler.java,v 1.1 2005/06/22 20:07:43 takacsot Exp $
 * 
 * 2004.10.29. 12:06:39
 */
public class CommonLoggingHandler extends LogHandler {

    public enum LogLevel {

        TRACE, DEBUG, INFO, WARN, ERROR, FATAL
    }

    ;

    protected Log logger;

    protected LogLevel level;

    public CommonLoggingHandler() {
        this(LogLevel.DEBUG);
    }

    public CommonLoggingHandler(Log logger) {
        this(logger, LogLevel.DEBUG);
    }

    public CommonLoggingHandler(LogLevel level) {
        this(LogFactory.getLog(CommonLoggingHandler.class), level);
    }

    public CommonLoggingHandler(Log logger, LogLevel level) {
        setLogger(logger);
        setLevel(level);
    }

    protected void log(Object o) {
        if (LogLevel.TRACE == level) {
            logger.trace(o);
        } else if (LogLevel.DEBUG == level) {
            logger.debug(o);
        } else if (LogLevel.INFO == level) {
            logger.info(o);
        } else if (LogLevel.WARN == level) {
            logger.warn(o);
        } else if (LogLevel.ERROR == level) {
            logger.error(o);
        } else if (LogLevel.FATAL == level) {
            logger.fatal(o);
        }
    }

    /**
     * @return Returns the logger.
     */
    public Log getLogger() {
        return logger;
    }

    /**
     * @param logger The logger to set.
     */
    public void setLogger(Log logger) {
        this.logger = logger;
    }

    /**
     * @return Returns the level.
     */
    public LogLevel getLevel() {
        return level;
    }

    /**
     * @param level The level to set.
     */
    public void setLevel(LogLevel level) {
        this.level = level;
    }
}
