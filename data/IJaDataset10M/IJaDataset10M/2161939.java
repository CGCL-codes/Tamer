package org.eclipse.dltk.freemarker.launching;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class FreemarkerLaunchingPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.eclipse.dltk.freemarker.launching";

    private static FreemarkerLaunchingPlugin plugin;

    /**
	 * The constructor
	 */
    public FreemarkerLaunchingPlugin() {
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
    public static FreemarkerLaunchingPlugin getDefault() {
        return plugin;
    }
}
