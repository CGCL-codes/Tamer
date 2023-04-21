package org.deft.util;

import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.osgi.framework.Bundle;

public class ImageHelper {

    private static ImageRegistry registry = new ImageRegistry();

    private static final ImageDescriptor MISSING_IMAGE_DESC = ImageDescriptor.createFromImageData(new ImageData(6, 6, 1, new PaletteData(new RGB[] { new RGB(255, 255, 255) })));

    ;

    private static final Image MISSING_IMAGE = MISSING_IMAGE_DESC.createImage();

    /**
	 * Searches the /icons/ directory of DEFT and returns an Image. Use this instead of getImageDescriptor,
	 * because here the images are fetched via a registry.
	 * 
	 * @param iconName The filename of the icon image
	 */
    public static Image getDeftIcon(String iconName) {
        return getImage("icons/" + iconName, "org.deft");
    }

    /**
	 * Searches the path in the given plugin directory and returns an Image. Use this instead of getImageDescriptor,
	 * because here the images are fetched via a registry.
	 * 
	 * @param imagePath the imagePath relative to the given plugin
	 */
    public static Image getImage(String imagePath, String pluginID) {
        String imageID = imagePath + pluginID;
        Image image = registry.get(imageID);
        if (image == null) {
            ImageDescriptor desc = getImageDescriptor(imagePath, pluginID);
            if (desc == MISSING_IMAGE_DESC) {
                image = MISSING_IMAGE;
            } else {
                image = desc.createImage();
            }
            registry.put(imageID, image);
        }
        return image;
    }

    /**
	 * Searches in the /icons/ directory and returns an ImageDescriptor.
	 * 
	 * @param iconName The filename of the icon image
	 */
    public static ImageDescriptor getDeftIconDescriptor(String iconName) {
        return getImageDescriptor("icons/" + iconName, "org.deft");
    }

    /**
	 * Returns an ImageDescriptor out of a pluginID and a path.
	 * This method tries to access the plug-in with pluginID, then
	 * it tries to access the given path inside it. If found it 
	 * tries to return an ImageDescriptor. If sth. went wrong
	 * the "missing image" ImageDescriptor is returned.
	 * 
	 * @param imagePath the image path relative to the given plugin
	 * @param pluginID id of the plugin in which to search
	 */
    public static ImageDescriptor getImageDescriptor(String imagePath, String pluginID) {
        ImageDescriptor imageDesc = registry.getDescriptor(imagePath + pluginID);
        if (imageDesc == null) {
            try {
                if (imagePath == null) {
                    imageDesc = MISSING_IMAGE_DESC;
                } else {
                    Bundle bundle = Platform.getBundle(pluginID);
                    URL url = FileLocator.resolve(bundle.getEntry("/"));
                    url = new URL(url.toString() + imagePath);
                    imageDesc = ImageDescriptor.createFromURL(url);
                }
            } catch (Exception e) {
                e.printStackTrace();
                imageDesc = ImageDescriptor.getMissingImageDescriptor();
            }
        }
        return imageDesc;
    }
}
