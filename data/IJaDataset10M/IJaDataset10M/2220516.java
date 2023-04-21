package org.kalypso.nofdpidss.measure.construction;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class NofdpMeasureConstructionPlugin extends Plugin {

    public static final String PLUGIN_ID = "org.kalypso.nofdpidss.measure.construction";

    private static NofdpMeasureConstructionPlugin plugin;

    /**
   * The constructor
   */
    public NofdpMeasureConstructionPlugin() {
    }

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
    public static NofdpMeasureConstructionPlugin getDefault() {
        return plugin;
    }
}
