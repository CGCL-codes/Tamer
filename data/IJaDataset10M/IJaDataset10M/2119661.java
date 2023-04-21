package org.nakedobjects.utility.image.java;

import org.nakedobjects.object.NakedObjects;
import org.nakedobjects.utility.image.TemplateImage;
import org.nakedobjects.utility.image.TemplateImageLoader;
import java.awt.Canvas;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;

public class AwtTemplateImageLoader implements TemplateImageLoader {

    private static final String[] EXTENSIONS = { "png", "gif", "jpg", "jpeg" };

    private static final Logger LOG = Logger.getLogger(AwtTemplateImageLoader.class);

    private static final String IMAGE_DIRECTORY = "images";

    private static final String IMAGE_DIRECTORY_PARAM = Properties.PROPERTY_BASE + "image-directory";

    private static final String SEPARATOR = "/";

    private Boolean alsoLoadAsFiles = null;

    protected final MediaTracker mt = new MediaTracker(new Canvas());

    /**
     * A keyed list of core images, one for each name, keyed by the image path.
     */
    private Hashtable loadedImages = new Hashtable();

    private Vector missingImages = new Vector();

    private String directory;

    public AwtTemplateImageLoader() {
    }

    /**
     * Returns an image template for the specified image (as specified by a path to a file or resource). If
     * the path has no extension (.gif, .png etc) then all valid extensions are searched for.
     * 
     * This method attempts to load the image from the jar/zip file this class was loaded from ie, your
     * application, and then from the file system as a file if can't be found as a resource. If neither method
     * works the default image is returned.
     * 
     * @return returns a TemplateImage for the specified image file, or null if none found.
     */
    public TemplateImage getTemplateImage(final String name) {
        String path = directory() + name;
        final int extensionAt = path.lastIndexOf('.');
        final String root = extensionAt == -1 ? path : path.substring(0, extensionAt);
        if (loadedImages.contains(root)) {
            return (AwtTemplateImage) loadedImages.get(root);
        } else if (missingImages.contains(root)) {
            return null;
        } else {
            LOG.debug("searching for image " + path);
            Image image = null;
            if (extensionAt >= 0) {
                image = load(path);
                return AwtTemplateImage.create(image);
            } else {
                for (int i = 0; i < EXTENSIONS.length; i++) {
                    image = load(root + "." + EXTENSIONS[i]);
                    if (image != null) {
                        return AwtTemplateImage.create(image);
                    }
                }
            }
            LOG.debug("failed to find image for " + path);
            missingImages.addElement(root);
            return null;
        }
    }

    private String directory() {
        if (directory == null) {
            directory = NakedObjects.getConfiguration().getString(IMAGE_DIRECTORY_PARAM, IMAGE_DIRECTORY);
            if (!directory.endsWith(SEPARATOR)) {
                directory = directory.concat(SEPARATOR);
            }
        }
        return directory;
    }

    private Image load(final String path) {
        Image image = loadAsResource(path);
        if (image == null) {
            if (alsoLoadAsFiles == null) {
                alsoLoadAsFiles = new Boolean(NakedObjects.getConfiguration().getBoolean(Properties.PROPERTY_BASE + "load-images-from-files", true));
            }
            if (alsoLoadAsFiles.booleanValue()) {
                image = loadAsFile(path);
            }
        }
        return image;
    }

    /**
     * Get an Image object from the specified file path on the file system.
     */
    private Image loadAsFile(final String path) {
        final File file = new File(path);
        if (!file.exists()) {
            return null;
        } else {
            Toolkit t = Toolkit.getDefaultToolkit();
            Image image = t.getImage(file.getAbsolutePath());
            if (image != null) {
                mt.addImage(image, 0);
                try {
                    mt.waitForAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (mt.isErrorAny()) {
                    LOG.error("found image file but failed to load it: " + file.getAbsolutePath());
                    mt.removeImage(image);
                    image = null;
                } else {
                    mt.removeImage(image);
                    LOG.info("image loaded from file: " + file);
                }
            }
            return image;
        }
    }

    /**
     * Get an Image object from the jar/zip file that this class was loaded from.
     */
    protected Image loadAsResource(final String path) {
        LOG.info("seeking image from resources: /" + path);
        URL url = AwtTemplateImageLoader.class.getResource("/" + path);
        if (url == null) {
            LOG.info("  not found in resources: /" + path);
            return null;
        }
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        if (image != null) {
            mt.addImage(image, 0);
            try {
                mt.waitForAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mt.isErrorAny()) {
                LOG.error("found image but failed to load it from resources: " + url + " " + mt.getErrorsAny()[0]);
                mt.removeImage(image);
                image = null;
            } else {
                mt.removeImage(image);
                LOG.info("image loaded from resources: /" + url);
            }
        }
        if (image == null || image.getWidth(null) == -1) {
            throw new RuntimeException(image.toString());
        }
        return image;
    }
}
