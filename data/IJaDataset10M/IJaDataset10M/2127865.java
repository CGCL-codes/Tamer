package org.plazmaforge.studio.ext;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ExtPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.plazmaforge.studio.ext";

    private static ExtPlugin plugin;

    /**
	 * The constructor
	 */
    public ExtPlugin() {
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
    public static ExtPlugin getDefault() {
        return plugin;
    }
}
