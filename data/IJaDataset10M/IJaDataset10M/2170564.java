package dioscuri.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author Bram Lohman
 * @author Bart Kiers
 */
public class ConsoleFormatter extends Formatter {

    public String format(LogRecord record) {
        return record.getMessage() + '\n';
    }
}
