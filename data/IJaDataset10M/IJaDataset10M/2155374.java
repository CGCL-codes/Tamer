package net.sf.freenote;

import net.sf.component.config.ConfigHelper;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The plugin class (singleton).
 * <p>
 * This instance can be shared between all extensions in the plugin. Information
 * shared between extensions can be persisted by using the PreferenceStore.
 * </p>
 * 
 * @see org.eclipse.ui.plugin.AbstractUIPlugin#getPreferenceStore()
 * @author Elias Volanakis
 */
public class ShapesPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "net.sf.pim.freenote";

    /** Single plugin instance. */
    private static ShapesPlugin singleton;

    /**
	 * Returns the shared plugin instance.
	 */
    public static ShapesPlugin getDefault() {
        return singleton;
    }

    /**
	 * The constructor.
	 */
    public ShapesPlugin() {
        if (singleton == null) {
            singleton = this;
        }
    }

    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        ConfigHelper.store();
        super.stop(context);
    }
}
