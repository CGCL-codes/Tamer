package net.sourceforge.ba_fo_ma.internal;

import net.sourceforge.ba_fo_ma.internal.constant.PluginConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * 
 */
public class InterfacePlugin extends AbstractUIPlugin {

    /**
     * 
	 */
    private static InterfacePlugin DEFAULT_PLUGIN_INSTANCE;

    /**
     * 
	 */
    public InterfacePlugin() {
        InterfacePlugin.DEFAULT_PLUGIN_INSTANCE = this;
    }

    /**
	 *
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 *
	 */
    public void stop(BundleContext context) throws Exception {
        InterfacePlugin.DEFAULT_PLUGIN_INSTANCE = null;
        super.stop(context);
    }

    /**
     * 
	 */
    protected void initializeImageRegistry(ImageRegistry registry) {
    }

    /**
	 *
	 */
    public static InterfacePlugin getInstance() {
        return InterfacePlugin.DEFAULT_PLUGIN_INSTANCE;
    }

    /**
     *
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin(PluginConstants.INTERFACE_PLUGIN_ID, path);
    }
}
