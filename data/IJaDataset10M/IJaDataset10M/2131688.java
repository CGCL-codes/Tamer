package net.sf.edevtools.lib.baselib.eclipse.nonui;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class BaseLibEclipseActivator extends Plugin {

    public static final String PLUGIN_ID = "de.pocketdeveloper.lib.eclipse.nonui";

    private static BaseLibEclipseActivator plugin;

    /**
	 * The constructor
	 */
    public BaseLibEclipseActivator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
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
    public static BaseLibEclipseActivator getDefault() {
        return plugin;
    }

    public void log(int aMsgType, String aLocalizedMessage, Throwable aEx) {
        getLog().log(new Status(aMsgType, PLUGIN_ID, aLocalizedMessage, aEx));
    }
}
