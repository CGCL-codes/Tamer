package org.eclipse.core.internal.runtime;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.runtime.*;
import org.osgi.framework.Bundle;

/**
 * 
 */
public class Log implements ILog {

    Bundle bundle;

    Set logListeners = new HashSet(5);

    public Log(Bundle plugin) {
        this.bundle = plugin;
    }

    /**
	 * Adds the given log listener to this log.  Subsequently the log listener will
	 * receive notification of all log events passing through this log.
	 *
	 * @see Platform#addLogListener(ILogListener)
	 */
    public void addLogListener(ILogListener listener) {
        synchronized (logListeners) {
            logListeners.add(listener);
        }
    }

    /**
	 * Returns the plug-in with which this log is associated.
	 */
    public Bundle getBundle() {
        return bundle;
    }

    /**
	 * Logs the given status.  The status is distributed to the log listeners
	 * installed on this log and then to the log listeners installed on the platform.
	 *
	 * @see Plugin#getLog()
	 */
    public void log(final IStatus status) {
        InternalPlatform.getDefault().log(status);
        ILogListener[] listeners;
        synchronized (logListeners) {
            listeners = (ILogListener[]) logListeners.toArray(new ILogListener[logListeners.size()]);
        }
        for (int i = 0; i < listeners.length; i++) {
            final ILogListener listener = listeners[i];
            ISafeRunnable code = new ISafeRunnable() {

                public void run() throws Exception {
                    listener.logging(status, bundle.getSymbolicName());
                }

                public void handleException(Throwable e) {
                }
            };
            InternalPlatform.getDefault().run(code);
        }
    }

    /**
	 * Removes the given log listener to this log.  Subsequently the log listener will
	 * no longer receive notification of log events passing through this log.
	 *
	 * @see Platform#removeLogListener(ILogListener)
	 */
    public void removeLogListener(ILogListener listener) {
        synchronized (logListeners) {
            logListeners.remove(listener);
        }
    }
}
