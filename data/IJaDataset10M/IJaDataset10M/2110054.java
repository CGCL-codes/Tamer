package edu.smu.cse8377.inf.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * The purpose of this class is to encapsulate formatting
 * code to ensure that log statements are not ugly and/or
 * hard to read.  This will aid the debugging process.
 * 
 * @author mdtrl
 *
 */
public class LogFormatter extends Formatter {

    /**
     * Formatter for the Date/timestamp
     */
    private SimpleDateFormat formatter;

    /**
     * Constructor for LogFormatter
     *
     */
    public LogFormatter() {
        super();
        formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS z");
    }

    /** 
     * MANDATORY METHOD OVERRIDE:
     * This method is intended to format a LogRecord
     * for output to a Log, using the Java Logging
     * framework.
     *
     * @param record LogRecord - to be written to a log
     * @return String formatted log stmt to output to log
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    @Override
    public String format(LogRecord record) {
        StringBuilder logStr = new StringBuilder();
        Date now = new Date(System.currentTimeMillis());
        String level = record.getLevel().toString();
        String logRec = formatter.format(now) + " (" + level.toUpperCase() + ") TID=" + record.getThreadID() + ": ";
        logStr.append(logRec);
        logStr.append(record.getMessage());
        logStr.append("\n");
        return logStr.toString();
    }
}
