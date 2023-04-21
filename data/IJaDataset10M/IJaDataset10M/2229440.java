package net.sf.forecash.commons.log4j;

import net.sf.forecash.commons.core.CommonsCorePlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Log4jListenerPlugin extends CommonsCorePlugin {

    private static Log4jListenerPlugin plugin;

    /**
	 * The constructor
	 */
    public Log4jListenerPlugin() {
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
    public static Log4jListenerPlugin getDefault() {
        return plugin;
    }
}
