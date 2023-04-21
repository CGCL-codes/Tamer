package com.magnatune.internal.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MagnatuneUIPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "com.magnatune.ui";

    private static MagnatuneUIPlugin plugin;

    /**
	 * The constructor
	 */
    public MagnatuneUIPlugin() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
    public static MagnatuneUIPlugin getDefault() {
        return plugin;
    }

    public static ImageDescriptor getImage(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static void log(Throwable exception) {
        getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, exception.getLocalizedMessage(), exception));
    }
}
