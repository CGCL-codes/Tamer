package org.objectstyle.wolips.launching;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.objectstyle.wolips.ui.plugins.AbstractWOLipsUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author uli
 * @author markus
 */
public class LaunchingPlugin extends AbstractWOLipsUIPlugin {

    private static LaunchingPlugin plugin;

    /**
	 * The constructor.
	 */
    public LaunchingPlugin() {
        super();
        plugin = this;
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
    public static LaunchingPlugin getDefault() {
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
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.objectstyle.wolips.launching", path);
    }
}
