package net.sf.eclint.splint;

import net.sf.eclint.splint.options.factories.SplintOptionsListFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class SplintPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "net.sf.eclint.splint";

    private static SplintPlugin plugin;

    private static SplintOptionsListFactory factory;

    /**
	 * The constructor
	 */
    public SplintPlugin() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
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
    public static SplintPlugin getDefault() {
        return plugin;
    }

    public static SplintOptionsListFactory getFactory() {
        if (factory == null) factory = new SplintOptionsListFactory();
        return factory;
    }
}
