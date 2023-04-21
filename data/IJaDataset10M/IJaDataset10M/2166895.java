package com.google.appengine.eclipse.datatools;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import java.text.MessageFormat;

/**
 * Logging class for the Datatools plugin.
 */
public class GoogleDatatoolsPluginLog {

    public static final String PLUGIN_ID = GoogleDatatoolsPluginLog.class.getPackage().getName();

    /**
   * Create a status object representing the specified information.
   * 
   * @param severity the severity; one of the following: <code>IStatus.OK</code>
   *          , <code>IStatus.ERROR</code>, <code>IStatus.INFO</code>, or
   *          <code>IStatus.WARNING</code>.
   * @param code the plug-in-specific status code, or <code>OK</code>.
   * @param message a human-readable message, localized to the current locale.
   * @param exception a low-level exception, or <code>null</code> if not
   *          applicable.
   * @return the status object (not <code>null</code>).
   */
    public static IStatus createStatus(int severity, int code, String message, Throwable exception) {
        return new Status(severity, PLUGIN_ID, code, message, exception);
    }

    /**
   * Log the specified information.
   * 
   * @param severity the severity; one of the following: <code>IStatus.OK</code>
   *          , <code>IStatus.ERROR</code>, <code>IStatus.INFO</code>, or
   *          <code>IStatus.WARNING</code>.
   * @param code the plug-in-specific status code, or <code>OK</code>.
   * @param message a human-readable message, localized to the current locale.
   * @param exception a low-level exception, or <code>null</code> if not
   *          applicable.
   */
    public static void log(int severity, int code, String message, Throwable exception) {
        log(createStatus(severity, code, message, exception));
    }

    /**
   * Log the given status.
   * 
   * @param status the status to log.
   */
    public static void log(IStatus status) {
        GoogleDatatoolsPlugin.getDefault().getLog().log(status);
    }

    /**
   * Log the specified error.
   * 
   * @param message a human-readable message, localized to the current locale.
   */
    public static void logError(String message) {
        logError(null, message);
    }

    /**
   * Log the specified error.
   * 
   * @param message a human-readable message, localized to the current locale.
   * @param args message arguments.
   */
    public static void logError(String message, Object... args) {
        logError(null, message, args);
    }

    /**
   * Log the specified error.
   * 
   * @param exception a low-level exception.
   */
    public static void logError(Throwable exception) {
        logError(exception, "Unexpected Exception");
    }

    /**
   * Log the specified error.
   * 
   * @param exception a low-level exception, or <code>null</code> if not
   *          applicable.
   * @param message a human-readable message, localized to the current locale.
   */
    public static void logError(Throwable exception, String message) {
        log(IStatus.ERROR, IStatus.OK, message, exception);
    }

    /**
   * Log the specified error.
   * 
   * @param exception a low-level exception, or <code>null</code> if not
   *          applicable.
   * @param message a human-readable message, localized to the current locale.
   * @param args message arguments.
   */
    public static void logError(Throwable exception, String message, Object... args) {
        message = MessageFormat.format(message, args);
        log(IStatus.ERROR, IStatus.OK, message, exception);
    }

    /**
   * Log the specified information.
   * 
   * @param message a human-readable message, localized to the current locale.
   */
    public static void logInfo(String message) {
        logInfo(message, new Object[0]);
    }

    /**
   * Log the specified information.
   * 
   * @param message a human-readable message, localized to the current locale.
   * @param args message arguments.
   */
    public static void logInfo(String message, Object... args) {
        message = MessageFormat.format(message, args);
        log(IStatus.INFO, IStatus.OK, message, null);
    }

    /**
   * Log the specified warning.
   * 
   * @param message a human-readable message, localized to the current locale.
   */
    public static void logWarning(String message) {
        log(IStatus.WARNING, IStatus.OK, message, null);
    }

    /**
   * Log the specified warning with an exception.
   */
    public static void logWarning(Throwable t, String string) {
        log(IStatus.WARNING, IStatus.OK, string, t);
    }
}
