package org.eclipse.ebrf.core.internal;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EBRFCorePlugin extends Plugin {

    public static final String PLUGIN_ID = "org.eclipse.ebrf.core";

    private static EBRFCorePlugin plugin;

    /**
	 * The constructor
	 */
    public EBRFCorePlugin() {
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
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
    public static EBRFCorePlugin getDefault() {
        return plugin;
    }
}
