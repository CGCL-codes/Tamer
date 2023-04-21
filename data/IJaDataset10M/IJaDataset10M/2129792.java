package org.parallelj.mda.archetypes;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

    private static Activator plugin;

    public static final String ID = "org.parallelj.mda.archetypes";

    /**
	 * The constructor.
	 */
    public Activator() {
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
	 * Returns the shared instance.
	 */
    public static Activator getDefault() {
        return plugin;
    }
}
