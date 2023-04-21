package org.soatest.core.log;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Formatter the log messages to show in HTML file
 * 
 * @author Rodrigo Prestes Machado
 * @version November 16, 2009
 */
public class FileFormatter extends Formatter {

    public String format(LogRecord rec) {
        StringBuffer buf = new StringBuffer(1000);
        if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
            buf.append("<B>");
            buf.append(rec.getLevel());
            buf.append("</B>");
        } else {
            buf.append(rec.getLevel());
        }
        buf.append(' ');
        buf.append(formatMessage(rec));
        buf.append("<BR>");
        return buf.toString();
    }

    /**
     * Creates the HTML header
     */
    public String getHead(Handler h) {
        return "<HTML><HEAD>" + (new Date()) + "</HEAD><BODY><PRE>";
    }

    /**
     * Creates the HTML footer or tail
     */
    public String getTail(Handler h) {
        return "</PRE></BODY></HTML>";
    }
}
