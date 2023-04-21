package org.jogre.common.util;

/**
 * Interface which compliments the JogreLog class.
 *
 * @author  Bob Marks
 * @version Alpha 0.2.3
 */
public interface IJogreLog {

    /** No logging will be created. */
    public static final int NONE = 0;

    /** Level 1 priority (high). */
    public static final int ERROR = 1;

    /** Level 2 priority (medium). */
    public static final int INFO = 2;

    /** Level 3 priority (low). */
    public static final int DEBUG = 3;

    /** String values of priorities */
    public static final String[] PRIORITY_STRS = { "NONE", "ERROR", "INFO ", "DEBUG" };

    /** Default priority (if problems reading from the properties file. */
    public static final int DEFAULT_CONSOLE_PRIORITY = INFO;

    /** Default value for file output. */
    public static final int DEFAULT_FILE_PRIORITY = DEBUG;

    /** Default for showing a priority. */
    public static final boolean DEFAULT_SHOW_PRIORITY = false;
}
