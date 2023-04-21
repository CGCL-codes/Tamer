package net.sourceforge.eclipsex.tests;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EclipsexTests extends Plugin {

    public static final String PLUGIN_ID = "net.sourceforge.eclipsex.tests";

    private static EclipsexTests plugin;

    /**
	 * The constructor
	 */
    public EclipsexTests() {
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
    public static EclipsexTests getDefault() {
        return plugin;
    }
}
