package org.omg.tacsit.worldwind.common.layers;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;

/**
 * An image pack that associates a key value to an image source.
 * <p>
 * The image pack will always return a valid image source.  If an explicit mapping is not provided, a "missing"
 * image will be returned.
 * @param <K> The key type that is used for defining images.
 * @author Matthew Child
 */
public class KeyedImagePack<K> implements ImagePack<K> {

    private static final Object DEFAULT_MISSING_IMAGE = WWIconUtils.toImageSource("/org/omg/tacsit/worldwind/common/resources/icons/question_mark_64.png");

    private Object missingImageSource;

    private Map<K, Object> identifierToImageSource;

    /**
   * Creates a new instance.
   */
    public KeyedImagePack() {
        this.identifierToImageSource = new HashMap();
        this.missingImageSource = DEFAULT_MISSING_IMAGE;
    }

    public Object getImageSource(K key) {
        Object imageSource = null;
        if (key != null) {
            imageSource = identifierToImageSource.get(key);
        }
        if (imageSource == null) {
            imageSource = missingImageSource;
        }
        return imageSource;
    }

    private void doSetImageSource(K key, Object imageSource) {
        identifierToImageSource.put(key, imageSource);
    }

    /**
   * Sets the image source that is used to represent a particular key.
   * @param key The key to define the image source of.
   * @param imagePath The location of the image (relative to the current working directory).
   */
    public void setImageSource(K key, String imagePath) {
        doSetImageSource(key, imagePath);
    }

    /**
   * Sets the image source that is used to represent a particular key.
   * @param key The key to define the image source of.
   * @param image The image to use as the image source.
   */
    public void setImageSource(K key, Image image) {
        Object imageSource = WWIconUtils.toImageSource(image);
        doSetImageSource(key, imageSource);
    }

    /**
   * Sets the image source that is used to represent a particular key.
   * @param key The key to define the image source of.
   * @param icon The icon to use as the image source.
   */
    public void setImageSource(K key, Icon icon) {
        Object imageSource = WWIconUtils.toImageSource(icon);
        doSetImageSource(key, imageSource);
    }

    /**
   * Gets the image source that will be returned if a key is not defined.
   * @return The image source that's used if the key isn't defined.
   */
    public Object getMissingImageSource() {
        return missingImageSource;
    }

    /**
   * Sets the image source that will be returned if a key isn't defined.
   * @param missingIconPath The path of the icon (relative to the current working directory).
   */
    public void setMissingImageSource(String missingIconPath) {
        this.missingImageSource = missingIconPath;
    }

    /**
   * Sets the image source that will be returned if a key isn't defined.
   * @param image The image to use as the image source.
   */
    public void setMissingImageSource(Image image) {
        this.missingImageSource = WWIconUtils.toImageSource(image);
    }

    /**
   * Sets the image source that will be returned if a key isn't defined.
   * @param icon The icon to use as the image source.
   */
    public void setMissingImageSource(Icon icon) {
        this.missingImageSource = WWIconUtils.toImageSource(icon);
    }
}
