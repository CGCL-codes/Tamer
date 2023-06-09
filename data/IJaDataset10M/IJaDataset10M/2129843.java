package org.eclipse.ui.internal.decorators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * The OverlayCache is a helper class used by the DecoratorManger
 * to manage the lifecycle of overlaid images.
 */
class OverlayCache {

    private Map cache = new HashMap();

    /**
     * Returns and caches an image corresponding to the specified icon.
     * @param icon the icon
     * @return the image
     */
    Image getImageFor(DecoratorOverlayIcon icon) {
        Image image = (Image) cache.get(icon);
        if (image == null) {
            image = icon.createImage();
            cache.put(icon, image);
        }
        return image;
    }

    /**
     * Disposes of all images in the cache.
     */
    void disposeAll() {
        for (Iterator it = cache.values().iterator(); it.hasNext(); ) {
            Image image = (Image) it.next();
            image.dispose();
        }
        cache.clear();
    }

    /**
     * Apply the descriptors for the receiver to the supplied
     * image.
     * @param source
     * @param descriptors
     * @return Image
     */
    Image applyDescriptors(Image source, ImageDescriptor[] descriptors) {
        Rectangle bounds = source.getBounds();
        Point size = new Point(bounds.width, bounds.height);
        DecoratorOverlayIcon icon = new DecoratorOverlayIcon(source, descriptors, size);
        return getImageFor(icon);
    }
}
