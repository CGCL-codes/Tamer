package net.sf.beatrix.internal.module.analyzer;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * 
 * @author Christian Wressnegger <chwress@users.sourceforge.net>
 */
public class Activator extends Plugin {

    /** The plug-in ID as specified in the MANIFEST.MF */
    public static final String PLUGIN_ID = "net.sf.beatrix.core.analyser";

    /** The shared instance. */
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
}
