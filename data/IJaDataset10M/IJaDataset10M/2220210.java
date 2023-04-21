package jorgan.skin;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A cache of images.
 */
public class ImageCache {

    private static Component component = new Component() {
    };

    private static Map<URL, Image> images = new HashMap<URL, Image>();

    /**
	 * Flush all cached images.
	 */
    public static void flush() {
        images.clear();
    }

    /**
	 * Get an image for the given URL.
	 * 
	 * @param url
	 *            url to get image for
	 * @return image
	 */
    public static Image getImage(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url must not be null");
        }
        Image img = images.get(url);
        if (img == null) {
            img = createImage(url);
            loadImage(img);
            images.put(url, img);
        }
        return img;
    }

    /**
	 * Create an image for the given URL.
	 * 
	 * @param url
	 *            url to create image from
	 * @return created image
	 */
    private static Image createImage(URL url) {
        return Toolkit.getDefaultToolkit().createImage(url);
    }

    /**
	 * Loads the image, returning only when the image is loaded.
	 * 
	 * @param image
	 *            the image
	 */
    private static void loadImage(Image image) {
        MediaTracker tracker = new MediaTracker(component);
        tracker.addImage(image, -1);
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            throw new Error("unexpected interruption");
        }
        tracker.removeImage(image);
    }
}
