package tools;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author ssoldatos
 */
public class MySeriesLogger {

    public static Logger logger;

    /**
   * Cretates a default HTML logger
   * @param logName The name of the logger
   * @param logFile The fileneame of the logger
   * @return The html logger
   */
    public static Logger createHtmlLogger(String logName, String logFile) {
        return createHtmlLogger(logName, logFile, 131072, true, 1);
    }

    /**
   * Creates a logger
   * @param logName The name of the logger
   * @param logFile The filename to save the logs to
   * @param limit Maximum size of each log
   * @param append Append new logs to the file
   * @param numOfLogs Maximum number of log files
   * @return
   */
    public static Logger createTxtLogger(String logName, String logFile, int limit, boolean append, int numOfLogs) {
        return createLogger(logName, logFile, new SimpleFormatter(), limit, append, numOfLogs);
    }

    private static Logger createLogger(String logName, String logFile, Formatter formatter, int limit, boolean append, int numOfLogs) {
        Locale locale = Locale.getDefault();
        locale = Locale.ENGLISH;
        Locale.setDefault(locale);
        Logger logger = null;
        if (formatter instanceof HTMLFormatter) {
            logFile = logFile + "_%g.html";
        } else {
            logFile = logFile + "_%g.log";
        }
        try {
            FileHandler fh = new FileHandler(logFile, limit, numOfLogs, append);
            fh.setEncoding("UTF-8");
            fh.setFormatter(formatter);
            logger = Logger.getLogger(logName);
            logger.addHandler(fh);
            logger.setLevel(Level.OFF);
        } catch (IOException ex) {
            Logger.getLogger(MySeriesLogger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(MySeriesLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
        return logger;
    }

    /**
   * Creates the logger
   * @param logName The name
   * @param logFile The filename
   * @param limit the max filesize in bytes
   * @param append Append to the file or not
   * @param numOfLogs The maximum number of logs
   * @return The logger
   */
    public static Logger createHtmlLogger(String logName, String logFile, int limit, boolean append, int numOfLogs) {
        return createLogger(logName, logFile, new HTMLFormatter(), limit, append, numOfLogs);
    }

    /**
   * Creates a logger
   * @param logName The name of the logger
   * @param logFile The filename to save the logs to
   * @return the logger
   */
    public static Logger createSimpleLogger(String logName, String logFile) {
        return createTxtLogger(logName, logFile, 131072, true, 1);
    }
}
