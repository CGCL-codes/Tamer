package freja.internal.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author SiroKuro
 */
public class LineFormatter extends Formatter {

    @Override
    public String format(LogRecord aRecord) {
        final StringBuffer message = new StringBuffer(131);
        message.append(String.format("%tD %<tT.%<tL", aRecord.getMillis()));
        message.append('\t');
        message.append(aRecord.getLevel().toString());
        message.append('\t');
        String className = aRecord.getSourceClassName();
        message.append(className != null ? className : aRecord.getLoggerName());
        message.append("#");
        String methodName = aRecord.getSourceMethodName();
        message.append(methodName != null ? methodName : "N/A");
        message.append("\n");
        message.append(formatMessage(aRecord));
        message.append("\n");
        Throwable th = aRecord.getThrown();
        if (th != null) {
            if (aRecord.getLevel() == Level.SEVERE) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                th.printStackTrace(pw);
                pw.close();
                message.append(sw);
            } else {
                message.append(th);
            }
            message.append("\n");
        }
        message.append("\n");
        return message.toString();
    }
}
