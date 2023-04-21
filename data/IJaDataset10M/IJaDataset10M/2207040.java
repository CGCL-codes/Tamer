package org.neurox.esearch.mycroft;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class MycroftPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.neurox.esearch.mycroft";

    private static MycroftPlugin plugin = null;

    /**
	 * The constructor.
	 */
    public MycroftPlugin() {
        plugin = this;
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
	 * 
	 * @return the shared instance.
	 */
    public static MycroftPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
