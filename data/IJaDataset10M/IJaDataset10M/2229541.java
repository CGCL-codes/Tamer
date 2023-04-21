package ch.tarnet.library;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author Damien Plumettaz
 * @since 14 avr. 2012
 *
 */
public class Utilities {

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(Utilities.class.getName());

    public static void configureLogger() {
        Logger.getLogger("ch.tarnet").setLevel(Level.FINEST);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.FINEST);
        Logger.getLogger("").getHandlers()[0].setFormatter(new Formatter() {

            @Override
            public String format(LogRecord record) {
                String msg = record.getMessage();
                if ("EMPTY".equals(msg)) {
                    return "";
                }
                if ("ENTRY".equals(msg)) {
                    msg = "Entering " + record.getSourceClassName() + "." + record.getSourceMethodName();
                } else if ("RETURN".equals(msg)) {
                    msg = "Exiting " + record.getSourceClassName() + "." + record.getSourceMethodName() + "\n";
                }
                return String.format("%1$tT.%1$tL - %2$7s %4$s - %3$s\n", record.getMillis(), record.getLevel().getName(), msg, record.getLoggerName());
            }
        });
    }
}
