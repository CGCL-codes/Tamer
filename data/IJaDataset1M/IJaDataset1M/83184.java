package werkzeugkasten.console.anchor;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import werkzeugkasten.common.runtime.LogUtil;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

    public static final String PLUGIN_ID = "werkzeugkasten.console.anchor";

    private static Activator plugin;

    /**
	 * The constructor
	 */
    public Activator() {
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
    public static Activator getDefault() {
        return plugin;
    }

    public static void log(Throwable t) {
        LogUtil.log(getDefault(), t);
    }
}
