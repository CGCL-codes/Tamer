package cz.vse.keg.patomat.xd.transformation;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Ondrej Zamazal
 * 
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "cz.vse.keg.patomat.xd.transformation";

    private static Activator plugin;

    /**
	 * The constructor
	 */
    public Activator() {
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
    public static Activator getDefault() {
        return plugin;
    }
}
