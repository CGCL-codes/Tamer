package mclib.util;

import mclib.slick.DropShell;

/**
 * Static class for dealing with logging
 * 
 * @author Doug Reeves
 * 
 */
public class Log {

    /**
	 * if true, VerboseInfo will be logged
	 */
    public static boolean logVerboseInfo = true;

    /**
	 * if true, Info will be logged
	 */
    public static boolean logInfo = true;

    /**
	 * if true, Debug info will be logged
	 */
    public static boolean logDebug = true;

    /**
	 * if true, Warnings will be logged
	 */
    public static boolean logWarning = true;

    /**
	 * if true, Errors will be logged
	 */
    public static boolean logError = true;

    private static DropShell shell = null;

    /**
	 * Register the shell with the logging system. This only needs to be done
	 * once.
	 * 
	 * @param s
	 *            The shell to be registered with the logging system
	 */
    public static void registerShell(final DropShell s) {
        shell = s;
    }

    /**
	 * This logging level is to be used for information that produces lots and
	 * lots and lots of output.
	 * 
	 * @param text
	 *            The message that will be logged
	 */
    public static void verboseInfo(final String text) {
        if (logVerboseInfo) write("VINFO:   ", text);
    }

    /**
	 * This logging level is to be used for message that do not imply harm and
	 * do not imply that there could be something wrong, they simply let the
	 * user know what is going on and can be used for troubleshooting the
	 * program.
	 * 
	 * @param text
	 *            The message that will be logged
	 */
    public static void info(final String text) {
        if (logInfo) write("INFO:    ", text);
    }

    /**
	 * This logging level is for messages that tell the user that the program
	 * has encountered an error and probably cannot recover from it.
	 * 
	 * @param text
	 *            The message that will be logged
	 */
    public static void error(final String text) {
        if (logError) write("ERROR:   ", text);
    }

    /**
	 * This logging level it for message that tell the user that something is
	 * not optimal but the program should be able to run anyways.
	 * 
	 * @param text
	 *            The message that will be logged
	 */
    public static void warning(final String text) {
        if (logWarning) write("WARNING: ", text);
    }

    /**
	 * This logging level is for messages that will help determing where a bud
	 * is in the program or help determing how well the program is working.
	 * 
	 * @param text
	 *            The message that will be logged
	 */
    public static void debug(final String text) {
        if (logDebug) write("DEBUG:   ", text);
    }

    /**
	 * Log a message from the shell, used so that the message is not written
	 * back to the shell making an infinite loop.
	 * 
	 * @param text
	 *            The string from the shell that should be logged.
	 */
    public static void shellMessage(final String text) {
        String[] split = text.split("\n");
        for (String s : split) {
            System.out.println("Console Message: " + s);
        }
    }

    /**
	 * Writes the message to wherever stuff is being logged
	 * 
	 * @param text
	 *            The message that will be logged
	 */
    private static void write(final String level, final String text) {
        String[] split = text.split("\n");
        for (String s : split) {
            System.out.println(level + s);
            if (shell != null) {
                shell.logShellOnly(level + s);
            }
        }
    }
}
