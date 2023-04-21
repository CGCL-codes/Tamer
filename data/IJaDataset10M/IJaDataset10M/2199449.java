package ru.spbu.dorms.geo.rmp;

import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class Activator extends AbstractUIPlugin {

    private static Activator plugin;

    /**
	 * The constructor.
	 */
    public Activator() {
        plugin = this;
    }

    public static String getPluginId() {
        return plugin.getBundle().getSymbolicName();
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
        super.stop(context);
        plugin = null;
    }

    /**
	 * Returns the shared instance.
	 */
    public static Activator getDefault() {
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
        return AbstractUIPlugin.imageDescriptorFromPlugin("ru.spbu.dorms.geo.rmp", path);
    }

    public static void log(Throwable t) {
        log(t, "");
    }

    public static void log(Throwable t, String message) {
        getDefault().getLog().log(new Status(IStatus.ERROR, getDefault().getBundle().getSymbolicName(), 0, message, t));
    }
}
