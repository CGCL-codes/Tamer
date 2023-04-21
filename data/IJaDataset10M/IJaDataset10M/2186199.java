package org.imogene.tools.libraryhelper;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class LibraryHelperPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.imogene.tools.libraryhelper";

    private static LibraryHelperPlugin plugin;

    /**
	 * The constructor
	 */
    public LibraryHelperPlugin() {
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
    public static LibraryHelperPlugin getDefault() {
        return plugin;
    }
}
