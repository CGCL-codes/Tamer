package de.uniwue.tm.textmarker.extension.sample;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class SampleExtensionPlugin extends Plugin {

    public static final String PLUGIN_ID = "de.uniwue.tm.textmarker.extension.sample";

    private static SampleExtensionPlugin plugin;

    /**
   * The constructor
   */
    public SampleExtensionPlugin() {
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
    public static SampleExtensionPlugin getDefault() {
        return plugin;
    }
}
