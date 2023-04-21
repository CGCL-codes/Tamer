package jaxlib.logging;

import java.beans.ExceptionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import jaxlib.util.CheckArg;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: LoggingExceptionListener.java 3025 2011-12-12 18:16:47Z joerg_wassmer $
 */
public class LoggingExceptionListener extends Object implements ExceptionListener {

    public static final LoggingExceptionListener globalSevere = new LoggingExceptionListener(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), Level.SEVERE);

    public static final LoggingExceptionListener globalWarning = new LoggingExceptionListener(Logger.getLogger(Logger.GLOBAL_LOGGER_NAME), Level.WARNING);

    protected Logger logger;

    protected Level level;

    public LoggingExceptionListener(Logger logger, Level level) {
        super();
        CheckArg.notNull(logger, "logger");
        CheckArg.notNull(level, "level");
        this.logger = logger;
        this.level = level;
    }

    public Level getLevel() {
        return this.level;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public void exceptionThrown(final Exception ex) {
        Logger logger = getLogger();
        if (logger != null) {
            Level level = getLevel();
            if (level == null) level = Level.SEVERE;
            if (logger.isLoggable(level)) {
                String msg = ex.getMessage();
                if (msg == null) msg = "";
                XLogRecord record = XLogRecord.newInstance(level, msg);
                record.setThrown(ex);
                logger.log(record);
            }
        }
    }
}
