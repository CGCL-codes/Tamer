package com.amazonaws.eclipse.datatools.enablement.simpledb.editor;

import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    /** The plug-in ID */
    public static final String PLUGIN_ID = "com.amazonaws.eclipse.datatools.enablement.simpledb.editor.ui";

    /** The shared instance */
    private static Activator plugin;

    /**
     * The constructor
     */
    public Activator() {
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
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Helper method for easier error logging.
     * 
     * @param mess
     * @param e
     * @param severity
     */
    public static void logMessage(final String mess, final Exception e, final int severity) {
        final Status status = new Status(severity, PLUGIN_ID, 1, "SimpleDB: " + mess, e);
        getDefault().getLog().log(status);
    }
}
