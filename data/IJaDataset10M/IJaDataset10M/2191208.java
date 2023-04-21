package hambo.svc.log.enhydra;

import com.lutris.logging.LogChannel;
import hambo.svc.log.*;

/**
 * Implements an Enhydra based Log.
 */
public class EnhydraLog implements Log {

    /** The name of the log level representing Log.DEBUG1 in Enhydra */
    public static final String DEBUG1_LEVEL = "HAMBO_DEBUG1";

    /** The name of the log level representing Log.DEBUG2 in Enhydra */
    public static final String DEBUG2_LEVEL = "HAMBO_DEBUG2";

    /** The name of the log level representing Log.DEBUG3 in Enhydra */
    public static final String DEBUG3_LEVEL = "HAMBO_DEBUG3";

    /** The name of the log level representing Log.INFO in Enhydra */
    public static final String INFO_LEVEL = "HAMBO_INFO";

    /** The name of the log level representing Log.ERROR in Enhydra */
    public static final String ERROR_LEVEL = "HAMBO_ERROR";

    /** The integer representation of DEBUG_LEVEL1. */
    private int logLevelDebug1;

    /** The integer representation of DEBUG_LEVEL2. */
    private int logLevelDebug2;

    /** The integer representation of DEBUG_LEVEL3. */
    private int logLevelDebug3;

    /** The integer representation of INFO_LEVEL. */
    private int logLevelInfo;

    /** The integer representation of ERROR_LEVEL. */
    private int logLevelError;

    /** The enhydra log channel doing the actual logging. */
    private LogChannel logChannel = null;

    /**
   * Creates a new instance of the enhydra log implementation.
   * <pre>
   * Logg level mapping (HAMBO ==> Enhydra):
   *   Log.ERROR  ==> HAMBO_ERROR
   *   Log.INFO   ==> HAMBO_INFO
   *   Log.DEBUG1 ==> HAMBO_DEBUG1
   *   Log.DEBUG2 ==> HAMBO_DEBUG2
   *   Log.DEBUG3 ==> HAMBO_DEBUG3
   * </pre>
   */
    EnhydraLog(LogChannel logChannel) {
        this.logChannel = logChannel;
        logLevelDebug1 = logChannel.getLevel(DEBUG1_LEVEL);
        logLevelDebug2 = logChannel.getLevel(DEBUG2_LEVEL);
        logLevelDebug3 = logChannel.getLevel(DEBUG3_LEVEL);
        logLevelInfo = logChannel.getLevel(INFO_LEVEL);
        logLevelError = logChannel.getLevel(ERROR_LEVEL);
    }

    /**
   * Writes a log message to the log file.
   */
    public void println(int level, String msg) {
        logChannel.write(transformToEnhydraLevel(level), msg);
    }

    /**
   * Writes a log message and an exception to the log file.
   */
    public void println(int level, String msg, Throwable exception) {
        logChannel.write(transformToEnhydraLevel(level), msg, exception);
    }

    /**
   * Determine if logging is enabled.
   */
    public boolean isEnabled(int level) {
        return logChannel.isEnabled(transformToEnhydraLevel(level));
    }

    /**
   * Transforms a Hambo to an Enhydra debug level. 
   */
    protected int transformToEnhydraLevel(int level) {
        switch(level) {
            case Log.ERROR:
                return logLevelError;
            case Log.INFO:
                return logLevelInfo;
            case Log.DEBUG2:
                return logLevelDebug2;
            case Log.DEBUG3:
                return logLevelDebug3;
            case Log.DEBUG1:
            default:
                return logLevelDebug1;
        }
    }
}
