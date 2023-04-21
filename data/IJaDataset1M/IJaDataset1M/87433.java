package net.openchrom.chromatogram.msd.ui.swt;

import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "net.openchrom.chromatogram.msd.ui.swt";

    private static Activator plugin;

    private static ImageRegistry imageRegistry;

    public static final String IMAGE_ION = "IMAGE_ION";

    /**
	 * The constructor
	 */
    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        initializeImageRegistry();
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
        imageRegistry.dispose();
    }

    /**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
    public static Activator getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param key
	 *            the key
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String key) {
        if (imageRegistry != null) {
            return imageRegistry.getDescriptor(key);
        }
        return null;
    }

    /**
	 * Returns the image of the corresponding key.
	 * 
	 * @param key
	 * @return Image
	 */
    public static Image getImage(String key) {
        if (imageRegistry != null) {
            return imageRegistry.get(key);
        }
        return null;
    }

    /**
	 * Initializes the image registry for the actual plugin.
	 */
    private void initializeImageRegistry() {
        if (PlatformUI.isWorkbenchRunning()) {
            if (Display.getCurrent() != null) {
                imageRegistry = new ImageRegistry(Display.getCurrent());
            } else if (PlatformUI.getWorkbench() != null) {
                imageRegistry = new ImageRegistry(PlatformUI.getWorkbench().getDisplay());
            }
            ImageDescriptor imageDescriptor = null;
            Bundle bundle = Platform.getBundle(PLUGIN_ID);
            imageDescriptor = createImageDescriptor(bundle, "icons/ion.gif");
            imageRegistry.put(IMAGE_ION, imageDescriptor);
        }
    }

    /**
	 * Helps to create an image descriptor.
	 * 
	 * @param bundle
	 * @param string
	 * @return ImageDescriptor
	 */
    private ImageDescriptor createImageDescriptor(Bundle bundle, String string) {
        ImageDescriptor imageDescriptor = null;
        IPath path = new Path(string);
        URL url = FileLocator.find(bundle, path, null);
        imageDescriptor = ImageDescriptor.createFromURL(url);
        return imageDescriptor;
    }
}
