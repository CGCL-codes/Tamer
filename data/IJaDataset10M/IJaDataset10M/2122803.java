package net.sf.myway.gps.garmin;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class GarminPlugin extends Plugin {

    public static final String PLUGIN_ID = "net.sf.myway.gps.garmin";

    private static GarminPlugin plugin;

    /**
	 * Returns the shared instance.
	 */
    public static GarminPlugin getDefault() {
        return plugin;
    }

    /**
	 * The constructor.
	 */
    public GarminPlugin() {
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    @Override
    public void stop(final BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }
}
