package org.wsmostudio.runtime;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * A utility class to facilitate logging messages, warnings and errors
 * in the standart Eclipse's destination. 
 * 
 * Additionally, a custom logger class can be attached to listen for
 * log events (the implementation delegates to the plugin's default log
 * manager).
 *
 * @author not attributable
 * @version $Revision: 469 $ $Date: 2006-01-09 07:51:14 -0500 (Mon, 09 Jan 2006) $
 */
public class LogManager {

    public static void logError(String message, Throwable error) {
        logEntry(IStatus.ERROR, "<unknown source>", message, error);
    }

    public static void logError(String message) {
        logEntry(IStatus.ERROR, "<unknown source>", message, null);
    }

    public static void logError(Throwable error) {
        logEntry(IStatus.ERROR, "<unknown source>", error.getMessage(), error);
    }

    public static void logError(String pluginID, String message, Throwable error) {
        logEntry(IStatus.ERROR, pluginID, message, error);
    }

    public static void logWarning(String message, Throwable error) {
        logEntry(IStatus.WARNING, "<unknown source>", message, error);
    }

    public static void logWarning(String message) {
        logEntry(IStatus.WARNING, "<unknown source>", message, null);
    }

    public static void logWarning(Throwable error) {
        logEntry(IStatus.WARNING, "<unknown source>", error.getMessage(), error);
    }

    public static void logWarning(String pluginID, String message, Throwable error) {
        logEntry(IStatus.WARNING, pluginID, message, error);
    }

    private static void logEntry(int severity, String pluginID, String message, Throwable error) {
        if (message == null) {
            message = "No message available";
        }
        Status stat = new Status(severity, pluginID, IStatus.OK, message, error);
        RuntimePlugin.getDefault().getLog().log(stat);
    }

    public static void addLogListener(ILogListener listener) {
        RuntimePlugin.getDefault().getLog().addLogListener(listener);
    }

    public static void removeLogListener(ILogListener listener) {
        RuntimePlugin.getDefault().getLog().removeLogListener(listener);
    }
}
