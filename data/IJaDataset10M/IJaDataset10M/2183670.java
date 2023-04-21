package org.plazmaforge.studio.appmanager;

import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;
import org.plazmaforge.framework.config.ConfigurerManager;
import org.plazmaforge.studio.core.AbstractStudioPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class AppManagerPlugin extends AbstractStudioPlugin {

    public static final String PLUGIN_ID = "org.plazmaforge.studio.appmanager";

    private static AppManagerPlugin plugin;

    private static boolean configurationModify;

    /**
	 * The constructor
	 */
    public AppManagerPlugin() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
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
    public static AppManagerPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static boolean isConfigurationModify() {
        return configurationModify;
    }

    public static void setConfigurationModify(boolean configurationModify) {
        AppManagerPlugin.configurationModify = configurationModify;
    }

    public static void fireModifyState() {
        setConfigurationModify(ConfigurerManager.isModifyConfigurers());
    }
}
