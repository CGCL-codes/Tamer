package edu.harvard.fas.rbrady.tpteam.testplugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "edu.harvard.fas.rbrady.tpteam.testplugin";

    private static Activator plugin;

    private BundleContext mContext;

    /**
	 * The constructor
	 */
    public Activator() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        mContext = context;
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
    public static Activator getDefault() {
        return plugin;
    }

    public BundleContext getContext() {
        return mContext;
    }
}
