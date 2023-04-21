package fr.cnes.sitools.logging;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.restlet.data.Status;
import org.restlet.engine.log.AccessLogFileHandler;
import org.restlet.engine.util.DateUtils;
import org.restlet.resource.ResourceException;
import org.restlet.service.LogService;

/**
 * Custom log service
 * 
 * @author Jean-Christophe Malapert, CNES Copyright (C) 2010.
 * @version 0.1
 * 
 * @see The GNU Public License (GPL)
 */
public class LogDataServerService extends LogService {

    /** Logger for DataServerService */
    private static Logger logger = Logger.getLogger("fr.cnes.sitools");

    /**
   * Constructor
   * 
   * @param outputFile
   *          Path to store the log file
   * @param levelName
   *          level name of the log
   * @param logFormat
   *          logging template
   * @param logName
   *          logger name
   * @param enabled
   *          True to make enable the service. False to make disable the service
   * @see http://java.sun.com/javase/6/docs/api/java/util/logging/Level.html
   */
    public LogDataServerService(String outputFile, String levelName, String logFormat, String logName, boolean enabled) {
        super(enabled);
        try {
            if (logFormat != null && !logFormat.equals("")) {
                this.setLogFormat(logFormat);
            }
            this.setLoggerName(logName);
            Level level = Level.parse(levelName);
            AccessLogFileHandler accessLogFileHandler = new AccessLogFileHandler(outputFile, true);
            accessLogFileHandler.setFormatter(new LogAccessFormatter());
            accessLogFileHandler.setLevel(level);
            if ((logName != null) && !logName.equals("")) {
                logger = Logger.getLogger(logName);
            }
            logger.setLevel(level);
            logger.setUseParentHandlers(false);
            logger.addHandler(accessLogFileHandler);
        } catch (IOException ex) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);
        } catch (SecurityException ex) {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);
        }
    }

    /**
   * This class defines a new log format.
   */
    public class LogAccessFormatter extends Formatter {

        /**
     * Define the log format
     * 
     * @param record
     *          Format
     * @return Returns a format such as Date - record
     */
        @Override
        public String format(LogRecord record) {
            return (DateUtils.format(new Date(), DateUtils.FORMAT_RFC_3339.get(0)) + " - " + record.getMessage() + "\n");
        }
    }
}
