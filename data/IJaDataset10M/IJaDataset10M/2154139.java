package org.eclipse.mylyn.monitor.tests;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class MonitorTestsPlugin extends Plugin {

    private static MonitorTestsPlugin plugin;

    /**
	 * The constructor.
	 */
    public MonitorTestsPlugin() {
        super();
        plugin = this;
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
	 * Returns the shared instance.
	 */
    public static MonitorTestsPlugin getDefault() {
        return plugin;
    }
}
