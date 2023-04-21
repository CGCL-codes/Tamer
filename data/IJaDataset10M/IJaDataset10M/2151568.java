package org.eclipse.help.ui.internal;

import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Uses a resource bundle to load images and strings from a property file. This
 * class needs to properly use the desired locale.
 */
public class HelpUIResources {

    /**
	 * WorkbenchResources constructor comment.
	 */
    public HelpUIResources() {
        super();
    }

    /**
	 * Returns a string from a property file
	 */
    public static URL getImagePath(String name) {
        IPath path = new Path("$nl$/icons/").append(name);
        return FileLocator.find(HelpUIPlugin.getDefault().getBundle(), path, null);
    }

    /**
	 * Returns an image descriptor from a property file
	 * @param name simple image file name
	 * @return the descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String name) {
        URL imagePath = getImagePath(name);
        ImageRegistry registry = HelpUIPlugin.getDefault().getImageRegistry();
        ImageDescriptor desc = registry.getDescriptor(name);
        if (desc == null) {
            desc = ImageDescriptor.createFromURL(imagePath);
            registry.put(name, desc);
        }
        return desc;
    }

    public static ImageDescriptor getImageDescriptor(String bundleId, String name) {
        ImageRegistry registry = HelpUIPlugin.getDefault().getImageRegistry();
        ImageDescriptor desc = registry.getDescriptor(name);
        if (desc == null) {
            Bundle bundle = Platform.getBundle(bundleId);
            if (bundle == null) return null;
            URL url = FileLocator.find(bundle, new Path(name), null);
            desc = ImageDescriptor.createFromURL(url);
            registry.put(name, desc);
        }
        return desc;
    }

    /**
	 * Returns an image from a property file
	 * @param name simple image file name
	 * @return the new image or <code>null</code> if image
	 * could not be created
	 */
    public static Image getImage(String name) {
        ImageRegistry registry = HelpUIPlugin.getDefault().getImageRegistry();
        getImageDescriptor(name);
        return registry.get(name);
    }

    public static Image getImage(URL url) {
        ImageRegistry registry = HelpUIPlugin.getDefault().getImageRegistry();
        String name = url.toString();
        ImageDescriptor desc = registry.getDescriptor(name);
        if (desc == null) {
            desc = ImageDescriptor.createFromURL(url);
            registry.put(name, desc);
        }
        return registry.get(name);
    }
}
