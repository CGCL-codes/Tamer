package net.sourceforge.eclipsetrader.directaworld;

import org.eclipse.ui.plugin.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

public class DirectaWorldPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "net.sourceforge.eclipsetrader.directaworld";

    public static final String USERNAME_PREFS = "USERNAME";

    public static final String PASSWORD_PREFS = "PASSWORD";

    private static DirectaWorldPlugin plugin;

    public DirectaWorldPlugin() {
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
     */
    public static DirectaWorldPlugin getDefault() {
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
        return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
