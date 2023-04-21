package openrpg2.common.core.logging;

import java.text.DateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.Date;

/**
 *
 * @author Snowdog
 */
public class SingleLineFormatter extends Formatter {

    /** Creates a new instance of SingleLineFormatter */
    public SingleLineFormatter() {
    }

    public String format(LogRecord record) {
        Date d = new Date(record.getMillis());
        StringBuffer a = new StringBuffer();
        a.append(DateFormat.getDateInstance(DateFormat.SHORT).format(d));
        a.append(" ");
        a.append(DateFormat.getTimeInstance().getTimeInstance(DateFormat.SHORT).format(d));
        a.append(" [");
        a.append(record.getLevel().getLocalizedName());
        a.append("] (");
        a.append(record.getLoggerName());
        a.append("::");
        a.append(record.getSourceMethodName());
        a.append(") ");
        a.append(formatMessage(record));
        return a.toString();
    }
}
