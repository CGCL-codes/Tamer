package org.mariella.logging;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class LogPlugin extends Plugin {

    public static final String PLUGIN_ID = "org.mariella.logging";

    private static LogPlugin plugin;

    public LogPlugin() {
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

    public static LogPlugin getDefault() {
        return plugin;
    }
}
