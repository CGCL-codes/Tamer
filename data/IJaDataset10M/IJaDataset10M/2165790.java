package de.uni_leipzig.lots.common.util;

import de.uni_leipzig.lots.common.exceptions.LOTSException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Logformatter, welcher pro Record eine Zeile in Anspruch nimmt (außer Stacktraces).
 *
 * @author Alexander Kiel
 * @version $Id: ThinLogFormatter.java,v 1.8 2007/10/23 06:29:16 mai99bxd Exp $
 * @see UserReadableLogFormatter
 */
public class ThinLogFormatter extends Formatter {

    private DateFormat dateFormatter = DateFormat.getDateTimeInstance();

    private String lineSeparator = System.getProperty("line.separator", "\n");

    /**
     * Format the given LogRecord.
     *
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append(dateFormatter.format(new Date(record.getMillis())));
        if (record.getSourceClassName() != null) {
            sb.append(' ');
            sb.append(getLastPart(record.getSourceClassName()));
        }
        if (record.getSourceMethodName() != null) {
            sb.append(' ');
            sb.append(record.getSourceMethodName());
        }
        sb.append(' ');
        sb.append(record.getLevel().getLocalizedName());
        sb.append(':').append(' ');
        sb.append(record.getMessage());
        sb.append(lineSeparator);
        Throwable exception = record.getThrown();
        if (exception != null) {
            if (exception instanceof LOTSException) {
                sb.append(((LOTSException) exception).getAdminMessage());
            } else {
                sb.append(exception.getMessage());
            }
            sb.append(lineSeparator);
            if (stackTraceAtLevel(record.getLevel())) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                exception.printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            }
        }
        return sb.toString();
    }

    private boolean stackTraceAtLevel(Level level) {
        return Level.SEVERE.equals(level) || Level.WARNING.equals(level);
    }

    private final String getLastPart(String name) {
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
