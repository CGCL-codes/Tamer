package org.nexopenframework.ide.eclipse.intro;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>The activator class controls the plug-in life cycle</p>
 * 
 * @see AbstractUIPlugin
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.nexopenframework.ide.eclipse.intro";

    private static Activator plugin;

    /**
	 * The constructor
	 */
    public Activator() {
        plugin = this;
    }

    /**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
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
