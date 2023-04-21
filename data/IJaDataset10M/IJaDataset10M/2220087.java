package com.inetmon.jn.statistic.general;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import com.inetmon.jn.core.CorePlugin;
import com.inetmon.jn.statistic.general.model.GeneralStatProvider;

/**
 * The main plugin class to be used in the desktop.
 */
public class GeneralPlugin extends AbstractUIPlugin {

    private static GeneralPlugin plugin;

    private ResourceBundle resourceBundle;

    private static GeneralStatProvider generalViewSync;

    /**
     * The constructor.
     */
    public GeneralPlugin() {
        super();
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle("com.inetmon.jn.statistic.general.GeneralPluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
    }

    /**
     * This method is called upon plug-in activation
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        generalViewSync = new GeneralStatProvider();
        CorePlugin.getDefault().getRawPacketHandler().addRawPacketListener(generalViewSync);
    }

    /**
     * This method is called when the plug-in is stopped
     */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     */
    public static GeneralPlugin getDefault() {
        return plugin;
    }

    /**
     * Returns the string from the plugin's resource bundle, or 'key' if not
     * found.
     */
    public static String getResourceString(String key) {
        ResourceBundle bundle = GeneralPlugin.getDefault().getResourceBundle();
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
     * Returns the plugin's resource bundle,
     */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public static GeneralStatProvider getGeneralViewSync() {
        return generalViewSync;
    }
}
