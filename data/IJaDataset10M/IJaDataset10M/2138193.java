package org.objectstyle.wolips.commons;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.core.runtime.Plugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class CommonsPlugin extends Plugin {

    private static CommonsPlugin plugin;

    private ResourceBundle resourceBundle;

    /**
	 * The constructor.
	 */
    public CommonsPlugin() {
        super();
        plugin = this;
        try {
            this.resourceBundle = ResourceBundle.getBundle("org.objectstyle.wolips.commons.CommonsPluginResources");
        } catch (MissingResourceException x) {
            this.resourceBundle = null;
        }
    }

    /**
	 * @return Returns the shared instance.
	 */
    public static CommonsPlugin getDefault() {
        return plugin;
    }

    /**
	 * @return Returns the plugin's resource bundle,
	 */
    public ResourceBundle getResourceBundle() {
        return this.resourceBundle;
    }
}
