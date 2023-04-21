package org.eclipse.mylyn.rememberthemilk.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class RTMUIPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.eclipse.mylyn.rememberthemilk.ui";

    public static final String TITLE_MESSAGE_DIALOG = "Mylyn RememberTheMilk Client";

    private static RTMUIPlugin plugin;

    /**
	 * The constructor
	 */
    public RTMUIPlugin() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
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
    public static RTMUIPlugin getDefault() {
        return plugin;
    }
}
