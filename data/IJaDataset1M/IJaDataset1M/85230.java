package com.mousefeed.eclipse;

import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notNull;
import com.mousefeed.client.Messages;
import com.mousefeed.client.collector.Collector;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The activator class controls the plug-in life cycle.
 * @author Andriy Palamarchuk
 */
public class Activator extends AbstractUIPlugin {

    /**
     * The plug-in ID.
     */
    public static final String PLUGIN_ID = "com.mousefeed";

    /**
     * Provides messages text.
     */
    private static final Messages MESSAGES = new Messages(Activator.class);

    /**
     * The shared instance.
     */
    private static Activator plugin;

    /**
     * @see #getCollector()
     */
    private final Collector collector = new CollectorFactory().create();

    /**
     * The constructor.
     */
    public Activator() {
        isTrue(plugin == null);
        plugin = this;
        PluginProvider.getInstance().setPlugin(this);
    }

    /**
     * Returns the shared instance.
     *
     * @return the shared instance.
     * <code>null</code> before an activator is created or after it is stopped.
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Logs the specified status with this plug-in's log.
     * 
     * @param status status to log. Not <code>null</code>.
     */
    public void log(IStatus status) {
        notNull(status);
        getDefault().getLog().log(status);
    }

    /**
     * Logs an exception.
     * @param t the exception to log. Not <code>null</code>.
     */
    public void log(Throwable t) {
        notNull(t);
        if (t instanceof CoreException) {
            log(((CoreException) t).getStatus());
        } else {
            log(newErrorStatus(MESSAGES.get("error.generic"), t));
        }
    }

    /**
     * Returns a new error status for this plug-in with the given message.
     * @param message the message to be included in the status.
     * Not <code>null</code>.
     * @param t the exception to be included in the status,
     * or <code>null</code> if none.
     * @return a new error status. Never <code>null</code>.
     */
    private IStatus newErrorStatus(String message, Throwable t) {
        return new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, message + " ", t);
    }

    /**
     * Gathers user activity data.
     * @return the data collector. Not <code>null</code>.
     */
    public Collector getCollector() {
        return collector;
    }
}
