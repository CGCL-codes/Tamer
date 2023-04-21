package org.objectstyle.wolips.documentation;

import org.objectstyle.wolips.core.runtime.AbstractCorePlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author uli
 * @author markus
 */
public class DocumentationPlugin extends AbstractCorePlugin {

    private static DocumentationPlugin plugin;

    /**
	 * The constructor.
	 */
    public DocumentationPlugin() {
        super();
        plugin = this;
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
    public static DocumentationPlugin getDefault() {
        return plugin;
    }
}
