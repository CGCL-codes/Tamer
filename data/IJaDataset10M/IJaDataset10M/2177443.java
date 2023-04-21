package com.google.code.shell4eclipse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * 
 *
 * @author Kamen Petroff 
 * @since Mar 2, 2007
 */
public class Shell4EclipsePlugin extends AbstractUIPlugin {

    /**
	 * Unique identifier constant (value <code>"com.google.code.shell4eclipse"</code>)
	 * for the UI Console plug-in.
	 */
    private static final String ID_PLUGIN = "com.google.code.shell4eclipse";

    private static Shell4EclipsePlugin plugin;

    /**
	 * The constructor.
	 */
    public Shell4EclipsePlugin() {
        plugin = this;
    }

    /**
	 * Convenience method which returns the unique identifier of this plug-in.
	 */
    public static String getUniqueIdentifier() {
        return ID_PLUGIN;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    public void stop(BundleContext context) throws Exception {
        try {
        } finally {
            super.stop(context);
            plugin = null;
        }
    }

    /**
	 * Convenience method for logging statuses to the plugin log
	 * 
	 * @param status  the status to log
	 */
    public static void log(IStatus status) {
        if (plugin != null) {
            getDefault().getLog().log(status);
        }
    }

    public static void log(CoreException e) {
        log(e.getStatus().getSeverity(), "Internal error", e);
    }

    /**
	 * Log the given exception along with the provided message and severity indicator
	 */
    public static void log(int severity, String message, Throwable e) {
        log(new Status(severity, ID_PLUGIN, 0, message, e));
    }

    /**
	 * Returns the shared instance.
	 */
    public static Shell4EclipsePlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin(ID_PLUGIN, path);
    }
}
