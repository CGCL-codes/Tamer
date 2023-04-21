package cnaf.sidoc.ide.versionningbehaviours.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class SIDocVersionningBehavioursUIPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "cnaf.sidoc.ide.versionningbehaviours.ui";

    private static SIDocVersionningBehavioursUIPlugin plugin;

    /**
	 * The constructor
	 */
    public SIDocVersionningBehavioursUIPlugin() {
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
    public static SIDocVersionningBehavioursUIPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
