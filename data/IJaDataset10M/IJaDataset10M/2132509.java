package net.sf.beatrix.util.runtime;

import net.sf.beatrix.internal.util.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class Exceptions {

    /**
   * Wraps a <code>CoreException</code> with a consistent status.
   * 
   * @param message
   *          The exception message.
   * @param severity
   *          The severity ID.
   * @throws CoreException
   */
    public static void throwCoreException(String message, int severity) throws CoreException {
        IStatus status = new Status(severity, Activator.PLUGIN_ID, IStatus.OK, message, null);
        throw new CoreException(status);
    }

    public static void throwCoreException(String pluginID, String message, int severity) throws CoreException {
        IStatus status = new Status(severity, pluginID, IStatus.OK, message, null);
        throw new CoreException(status);
    }
}
