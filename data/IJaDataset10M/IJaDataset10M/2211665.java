package net.sourceforge.svnmonitor.base;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class BaseActivator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "net.sourceforge.svnmonitor.base";

    private static BaseActivator plugin;

    /**
	 * The constructor
	 */
    public BaseActivator() {
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
    public static BaseActivator getDefault() {
        return plugin;
    }
}
