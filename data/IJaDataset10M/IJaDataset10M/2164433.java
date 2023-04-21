package org.eclipse.twipse.logging;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.twipse.TwipsePlugin;

public class TwipseLog {

    public static void logInfo(String m) {
        log(IStatus.INFO, IStatus.OK, m, null);
    }

    public static void logError(Throwable e) {
        logError("Unexpected exception", e);
    }

    public static void logError(String mess, Throwable e) {
        log(IStatus.ERROR, IStatus.OK, mess, e);
    }

    public static void log(int severity, int code, String mess, Throwable exception) {
        log(createStatus(severity, code, mess, exception));
    }

    private static IStatus createStatus(int severity, int code, String mess, Throwable exception) {
        return new Status(severity, TwipsePlugin.PLUGIN_ID, code, mess, exception);
    }

    public static void log(IStatus s) {
        TwipsePlugin.getDefault().getLog().log(s);
    }
}
