package uk.co.q3c.deplan.rcp;

import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import uk.co.q3c.deplan.rcp.model.Contact;
import uk.co.q3c.deplan.rcp.model.Model;

/**
 * The main plugin class to be used in the desktop.
 */
public class DePlanRCPPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "DePlanRCPPlugin";

    /** The relative path to the images directory. */
    private static final String IMAGES_PATH = "icons/";

    private static DePlanRCPPlugin plugin;

    /**
	 * The constructor.
	 */
    public DePlanRCPPlugin() {
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation. Would set up the db4o
	 * service for {@link Database} if it worked, but direct connection used for
	 * now
	 */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    @Override
    public void stop(BundleContext context) throws Exception {
        Model.getDbc().close();
        Contact.disposeColors();
        super.stop(context);
        plugin = null;
    }

    /**
	 * Returns the shared instance.
	 */
    public static DePlanRCPPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *           the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    /**
	 * Creates an image by loading it from a file in the plugin's images
	 * directory.
	 * 
	 * @param imagePath
	 *           The path to the image file to load. The image path must not
	 *           include the plugin's images path location defined in
	 *           <code>IMAGES_PATH</code>. If you want to load
	 *           <code>IMAGES_PATH/myimage.gif</code>, simply pass
	 *           <code>myimage.gif</code> to this method.
	 * 
	 * @return The image object loaded from the image file
	 */
    public static Image createImage(String imagePath) {
        final Bundle pluginBundle = DePlanRCPPlugin.getDefault().getBundle();
        final Path imageFilePath = new Path(DePlanRCPPlugin.IMAGES_PATH + imagePath);
        final URL imageFileUrl = FileLocator.find(pluginBundle, imageFilePath, null);
        return ImageDescriptor.createFromURL(imageFileUrl).createImage();
    }
}
