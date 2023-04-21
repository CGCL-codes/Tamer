package neissmodel;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Class to handle logging of all NeissModel messages. It writes all Log
 * messages from the <code>neissmodel</code> package to the console and a file.
 * Classes that make use of logging should call <code>NeissModelLogger.setup()</code>
 * to make sure that everything is logged correctly.
 *
 * @author Nick Malleson
 */
public class NeissModelLogger {

    private static FileHandler fileHandler;

    private static ConsoleHandler consoleHandler;

    private static Formatter fileFormatter;

    private static Formatter consoleFormatter;

    private static boolean setup = false;

    private static void setup() {
        if (!setup) {
            try {
                Logger logger = Logger.getLogger("neissmodel");
                logger.setUseParentHandlers(false);
                logger.setLevel(Level.ALL);
                String logFile = NeissModel.getProperty(PCONST.LOG_FILE, true);
                fileHandler = new FileHandler(logFile);
                consoleHandler = new SystemOutConsoleHandler();
                fileFormatter = new SimpleFormatter();
                consoleFormatter = new StraightFormatter();
                fileHandler.setFormatter(fileFormatter);
                consoleHandler.setFormatter(consoleFormatter);
                logger.addHandler(fileHandler);
                logger.addHandler(consoleHandler);
                setup = true;
            } catch (IOException ex) {
                System.err.println("IOException creating logger: " + ex.toString());
            } catch (SecurityException ex) {
                System.err.println("SecurityException creating logger: " + ex.toString());
            }
        }
    }

    public static Logger getLogger(String name) {
        setup();
        Logger l = Logger.getLogger(name);
        l.setParent(Logger.getLogger("neissmodel"));
        l.setUseParentHandlers(true);
        return l;
    }
}

/**
 * Simple class used to remove any formatting from a lof message; messages are
 * returned without dates, log levels etc. Useful for sending to console. Will
 * show stack traces if available though.
 * @author Nick Malleson
 */
class StraightFormatter extends Formatter {

    public String format(LogRecord rec) {
        StringBuilder theError = new StringBuilder();
        theError.append(rec.getMessage() + "\n");
        if (rec.getThrown() != null) {
            if (rec.getThrown().getMessage() != null) {
                theError.append(rec.getThrown().getMessage() + "\n");
            }
            for (StackTraceElement s : rec.getThrown().getStackTrace()) {
                theError.append("\t" + s.toString() + "\n");
            }
        }
        return theError.toString();
    }
}

/**
 * A console handler that writes to System.out, rather than System.err (default)
 * 
 * @author Nick Malleson
 */
class SystemOutConsoleHandler extends ConsoleHandler {

    public SystemOutConsoleHandler() {
        super();
    }
}
