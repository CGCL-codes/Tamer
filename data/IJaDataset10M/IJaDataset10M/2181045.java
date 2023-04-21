package net.sourceforge.wildlife.core.components.food;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 *
 * @author Jean Barata
 */
public class NectarPlugin extends Plugin {

    /**
	 * The plug-in unique instance.
	 */
    private static NectarPlugin _instance = null;

    /**
	 * Default constructor.
	 */
    public NectarPlugin() {
    }

    /**
	 * Gets the unique activator instance.
	 * 
	 * @return The plug-in.
	 */
    public static NectarPlugin getDefault() {
        return _instance;
    }

    /**
	 *
	 */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        _instance = this;
    }

    /**
	 *
	 */
    @Override
    public void stop(BundleContext context) throws Exception {
        _instance = null;
        super.stop(context);
    }
}
