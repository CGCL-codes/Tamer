package org.regilo.core.configuration;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class RegiloConfiguratorPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.regilo.core.configuration";

    private static RegiloConfiguratorPlugin plugin;

    /**
	 * The constructor
	 */
    public RegiloConfiguratorPlugin() {
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
    public static RegiloConfiguratorPlugin getDefault() {
        return plugin;
    }
}
