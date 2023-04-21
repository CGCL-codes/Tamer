package org.criticalfailure.anp.core.util;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class for loading images and image descriptors from a specified bundle.
 * 
 * @author cipher@users.sourceforge.net
 * 
 */
public class ImageLoader {

    private static Logger logger = LoggerFactory.getLogger(ImageLoader.class);

    /**
     * Creates an image from the file at the specified path in the specified bundle.
     * 
     * @param bundle
     *            The bundle where the image file is located.
     * @param path
     *            The path in the bundle where the image file can be found.
     * @return An {@link Image}, or <tt>null</tt> if it couldn't be created.
     */
    public static Image getImage(Bundle bundle, String path) {
        Image img = null;
        try {
            img = new Image(Display.getDefault(), bundle.getBundleContext().getDataFile(path).getAbsolutePath());
        } catch (Exception e) {
            logger.warn("unable to create image for: " + path + ": " + e.getMessage());
        }
        return img;
    }

    /**
     * Creates an image from the file at the specified path using the classloader of the specified class.
     * 
     * @param klass
     *            The class whose classloader should be used to locate the file.
     * @param path
     *            The path where the image file can be found.
     * @return An {@link Image}, or <tt>null</tt> if it couldn't be created.
     */
    public static Image getImage(Class<?> klass, String path) {
        Image img = null;
        try {
            img = new Image(Display.getDefault(), klass.getClassLoader().getResourceAsStream(path));
        } catch (Exception e) {
            logger.warn("unable to create image for: " + path + ": " + e.getMessage());
        }
        return img;
    }

    /**
     * Creates an image descriptor from the file at the specified path in the specified bundle.
     * 
     * @param bundle
     *            The bundle where the image file is located.
     * @param path
     *            The path in the bundle where the image file can be found.
     * @return An {@link ImageDescriptor}, or <tt>null</tt> if it couldn't be created.
     */
    public static ImageDescriptor getImageDescriptor(Bundle bundle, String path) {
        ImageDescriptor idesc = null;
        try {
            idesc = ImageDescriptor.createFromURL(bundle.getEntry(path));
        } catch (Exception e) {
            logger.warn("unable to create image descriptor for: " + path + ": " + e.getMessage());
        }
        return idesc;
    }

    /**
     * Creates an image descriptor from the file at the specified path using the classloader of the specified class.
     * 
     * @param klass
     *            The class whose classloader should be used to locate the file.
     * @param path
     *            The path where the image file can be found.
     * @return An {@link ImageDescriptor}, or <tt>null</tt> if it couldn't be created.
     */
    public static ImageDescriptor getImageDescriptor(Class<?> klass, String path) {
        ImageDescriptor idesc = null;
        try {
            idesc = ImageDescriptor.createFromFile(klass, path);
        } catch (Exception e) {
            logger.warn("unable to create image descriptor for: " + path + ": " + e.getMessage());
        }
        return idesc;
    }

    /**
     * Creates an image from the shared image repository with the specified key.
     * 
     * @param key
     *            The key for the desired image.
     * @return An {@link Image} from the repository, or <tt>null</tt> if none was found.
     * @see org.eclipse.ui.ISharedImages
     */
    public static Image getStockImage(String key) {
        Image img = null;
        try {
            img = PlatformUI.getWorkbench().getSharedImages().getImage(key);
        } catch (Exception e) {
            logger.warn("unable to create image for: " + key + ": " + e.getMessage());
        }
        return img;
    }

    /**
     * Creates an image descriptor from the shared image repository with the specified key.
     * 
     * @param key
     *            The key for the desired image.
     * @return An {@link Image} from the repository, or <tt>null</tt> if none was found.
     * @see org.eclipse.ui.ISharedImages
     */
    public static ImageDescriptor getStockImageDescriptor(String key) {
        ImageDescriptor idesc = null;
        try {
            idesc = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(key);
        } catch (Exception e) {
            logger.warn("unable to create image descriptor for: " + key + ": " + e.getMessage());
        }
        return idesc;
    }
}
