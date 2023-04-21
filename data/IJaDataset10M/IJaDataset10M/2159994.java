package net.sf.l2j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import javolution.text.TextBuilder;

/**
 * This class ...
 *
 * @version $Revision: 1.1.4.2 $ $Date: 2005/03/27 15:30:08 $
 */
public class ConsoleLogFormatter extends Formatter {

    private static final String CRLF = "\r\n";

    @Override
    public String format(LogRecord record) {
        TextBuilder output = new TextBuilder();
        output.append(record.getMessage());
        output.append(CRLF);
        if (record.getThrown() != null) try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            record.getThrown().printStackTrace(pw);
            pw.close();
            output.append(sw.toString());
            output.append(CRLF);
        } catch (Exception ex) {
        }
        return output.toString();
    }
}
