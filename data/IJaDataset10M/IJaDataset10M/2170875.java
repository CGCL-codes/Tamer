package twowaysqleditor.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

public class StatusUtil {

    public static IStatus create(Plugin plugin, int severity, int code, String message, Throwable throwable) {
        return new Status(severity, plugin.getBundle().getSymbolicName(), code, message, throwable);
    }

    public static IStatus createError(Plugin plugin, int code, Throwable throwable) {
        String message = throwable.getMessage();
        if (message == null) {
            message = throwable.getClass().getName();
        }
        return create(plugin, IStatus.ERROR, code, message, throwable);
    }

    public static IStatus createError(Plugin plugin, int code, String message, Throwable throwable) {
        return create(plugin, IStatus.ERROR, code, message, throwable);
    }

    public static IStatus createWarning(Plugin plugin, int code, String message, Throwable throwable) {
        return create(plugin, IStatus.WARNING, code, message, throwable);
    }

    public static IStatus createInfo(Plugin plugin, int code, String message, Throwable throwable) {
        return create(plugin, IStatus.INFO, code, message, throwable);
    }

    public static boolean isError(IStatus status) {
        return status.getSeverity() == IStatus.ERROR || status.getSeverity() == IStatus.WARNING;
    }
}
