package org.tuba.representation.styledtext.plugin;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class StyledTextActivator extends Plugin {

    public static final String PLUGIN_ID = "org.tuba.representation.styledtext";

    private static StyledTextActivator plugin;

    /**
	 * The constructor
	 */
    public StyledTextActivator() {
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
    public static StyledTextActivator getDefault() {
        return plugin;
    }
}
