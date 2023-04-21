package net.sourceforge.qvtrel2op.plugin;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Implements the Eclipse plug-in job 'Translate to QVT-Operational'.
 * 
 * @author Pascal Muellender (p_muellender@users.sourceforge.net)
 */
public class QvtRel2OpPlugin extends AbstractUIPlugin {

    /**
	 * The plug-in ID
	 */
    public static final String PLUGIN_ID = "net.sourceforge.qvtrel2op";

    /**
	 * The shared plug-in instance
	 */
    private static QvtRel2OpPlugin plugin;

    /**
	 * Constructor
	 */
    public QvtRel2OpPlugin() {
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
	 * Returns the shared plug-in instance.
	 *
	 * @return the shared plug-in instance
	 */
    public static QvtRel2OpPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path The path
	 * @return the Image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
