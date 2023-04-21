package net.sf.rcer.jcoimport;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The class that controls the plug-in lifecycle.
 * @author vwegert
 *
 */
public class Activator extends Plugin {

    /**
	 * The ID of the plug-in.
	 */
    public static final String PLUGIN_ID = "net.sf.rcer.jcoimport";

    /**
	 * The de-facto singleton instance. 
	 */
    private static Activator instance;

    /**
	 * Default constructor.
	 */
    public Activator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        instance = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        instance = null;
        super.stop(context);
    }

    /**
	 * @return the active instance
	 */
    public static Activator getDefault() {
        return instance;
    }
}
