package computational;

import java.io.*;
import java.util.*;

/**
 * @author Massimo Bartoletti
 * @version 1.0
 */
public abstract class LogManager {

    /** Tells whether logging is enabled or disabled on this service. */
    protected boolean loggingEnabled;

    /** Tells whether dating is enabled or disabled on this service. */
    protected boolean datingEnabled;

    /**
	 * Enables logging on this LogManager.
	 */
    public void enableLogging() {
        loggingEnabled = true;
    }

    /**
	 * Disables logging on this LogManager.
	 */
    public void disableLogging() {
        loggingEnabled = false;
    }

    /**
	 * Enables dating on this LogManager.
	 */
    public void enableDating() {
        datingEnabled = true;
    }

    /**
	 * Disables dating on this LogManager.
	 */
    public void disableDating() {
        datingEnabled = false;
    }

    /**
	 * Logs the specified object on the predefined output stream.
	 * This method is thread-safe, i.e. objects logged by concurrent
	 * threads do not overlap.
	 * @param msg the object to be logged.
	 */
    public abstract void log(Object msg);
}
