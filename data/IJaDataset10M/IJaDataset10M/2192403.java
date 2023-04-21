package org.destecs.ide.libraries;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public class DestecsLibraryPlugin extends Plugin {

    public static final String PLUGIN_ID = ILibrariesConstants.PLUGIN_ID;

    public static final boolean DEBUG = true;

    private static DestecsLibraryPlugin plugin;

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
    public static DestecsLibraryPlugin getDefault() {
        return plugin;
    }

    public static void log(Throwable exception) {
        if (DEBUG) {
            getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, "DestecsLibraryPlugin", exception));
        }
    }

    public static void log(String message, Throwable exception) {
        if (DEBUG) {
            getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, message, exception));
        }
    }
}
