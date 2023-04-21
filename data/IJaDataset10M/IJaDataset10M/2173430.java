package org.destecs.ide.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class DestecsUIPlugin extends AbstractUIPlugin {

    private static DestecsUIPlugin plugin;

    public DestecsUIPlugin() {
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

    public static DestecsUIPlugin getDefault() {
        return plugin;
    }

    public static void log(Exception e) {
        getDefault().getLog().log(new Status(IStatus.ERROR, IDestecsUiConstants.PLUGIN_ID, "DestecsUIPlugin", e));
    }

    public static void logErrorMessage(String message) {
        getDefault().getLog().log(new Status(IStatus.ERROR, IDestecsUiConstants.PLUGIN_ID, message));
    }

    public static void log(String message, Throwable exception) {
        getDefault().getLog().log(new Status(IStatus.ERROR, IDestecsUiConstants.PLUGIN_ID, message, exception));
    }
}
