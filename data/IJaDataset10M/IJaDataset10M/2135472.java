package net.sf.jretina.trainer;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Plugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "net.sf.jretina.trainer";

    private static Plugin plugin;

    /**
	 * The constructor
	 */
    public Plugin() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
    public static Plugin getDefault() {
        return plugin;
    }
}
