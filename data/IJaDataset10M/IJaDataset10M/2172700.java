package com.comarch.xml;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "com.comarch.xml";

    private static Activator plugin;

    /**
	 * The constructor
	 */
    public Activator() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        System.err.println("######### STARTING " + PLUGIN_ID + " #########");
        super.start(context);
    }

    public void stop(BundleContext context) throws Exception {
        System.err.println("######### STOPPING " + PLUGIN_ID + " #########");
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
}
