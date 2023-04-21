package org.xhtmlrenderer.util;

import java.io.*;
import java.text.MessageFormat;
import java.util.logging.*;

/**
 * A java.util.logging.Formatter class that writes a bare-bones log messages,
 * with no origin class name and no date/time.
 *
 * @author   Patrick Wright
 */
public class XRSimpleLogFormatter extends Formatter {

    /** Description of the Field */
    private final MessageFormat mformat;

    /** Description of the Field */
    private final MessageFormat exmformat;

    /** Description of the Field */
    private static final String msgFmt;

    /** Description of the Field */
    private static final String exmsgFmt;

    /** Constructor for the XRSimpleLogFormatter object */
    public XRSimpleLogFormatter() {
        super();
        mformat = new MessageFormat(msgFmt);
        exmformat = new MessageFormat(exmsgFmt);
    }

    /**
     * Format the given log record and return the formatted string.
     *
     * @param record  PARAM
     * @return        Returns
     */
    public String format(LogRecord record) {
        Throwable th = record.getThrown();
        String thName = "";
        String thMessage = "";
        String trace = null;
        if (th != null) {
            StringWriter sw = new StringWriter();
            th.printStackTrace(new PrintWriter(sw));
            trace = sw.toString();
            thName = th.getClass().getName();
            thMessage = th.getMessage();
        }
        String args[] = { String.valueOf(record.getMillis()), record.getLoggerName(), record.getLevel().toString(), record.getSourceClassName(), record.getSourceMethodName(), record.getMessage(), thName, thMessage, trace };
        String log = null;
        if (th == null) {
            log = mformat.format(args);
        } else {
            log = exmformat.format(args);
        }
        return log;
    }

    /**
     * Localize and format the message string from a log record.
     *
     * @param record  PARAM
     * @return        Returns
     */
    public String formatMessage(LogRecord record) {
        return super.formatMessage(record);
    }

    /**
     * Return the header string for a set of formatted records.
     *
     * @param h  PARAM
     * @return   The head value
     */
    public String getHead(Handler h) {
        return super.getHead(h);
    }

    /**
     * Return the tail string for a set of formatted records.
     *
     * @param h  PARAM
     * @return   The tail value
     */
    public String getTail(Handler h) {
        return super.getTail(h);
    }

    static {
        msgFmt = Configuration.valueFor("xr.simple-log-format", "{1}:\n  {5}\n").trim() + "\n";
        exmsgFmt = Configuration.valueFor("xr.simple-log-format-throwable", "{1}:\n  {5}\n{8}").trim() + "\n";
    }
}
