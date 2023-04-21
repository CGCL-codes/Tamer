package log_error;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.MODULE;

/**
 * @author Chris Bayruns This is the logging class. It runs all of the logs.
 * @version $Revision: 1.0 $
 */
public class Log {

    /**
     * Field DEFAULTLOG.
     */
    private static final Logger DEFAULTLOG = Logger.getLogger(Log.class.getName());

    /**
     * 
     * This creates all of the log listeners, and sets their levels.
     * 
     */
    public static void initLogger() {
        DEFAULTLOG.setLevel(Level.INFO);
        try {
            final FileHandler fileHandler = new FileHandler("default.log", true);
            DEFAULTLOG.addHandler(fileHandler);
        } catch (Exception e) {
        }
    }

    /**
     * 
     * @return the defaultLog
     */
    public static Logger getDefaultLog() {
        return DEFAULTLOG;
    }

    public static void writeToLog(MODULE loc, Level level, String message) {
        DEFAULTLOG.log(level, message, loc);
    }

    /**
     * Method toString.
     * 
     * @return String
     */
    public String toString() {
        return null;
    }
}
