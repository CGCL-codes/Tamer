package it.battlehorse.rcp.tools;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author battlehorse
 * @since Nov 18, 2005
 */
public class Activator extends AbstractUIPlugin {

    private static Activator plugin;

    /**
	 * The constructor.
	 */
    public Activator() {
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    @Override
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
        return AbstractUIPlugin.imageDescriptorFromPlugin("it.battlehorse.rcp.tools", path);
    }
}
