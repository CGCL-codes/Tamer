package net.narusas.haircomby;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.InputStream;
import java.util.Hashtable;

public class ImageLoader {

    private static Hashtable cache = new Hashtable();

    private static Component trackerComponent = new Canvas();

    public static Image loadImage(String resourceName) {
        Image cached = (Image) cache.get(resourceName);
        if (cached != null) return cached;
        try {
            InputStream resource = ImageLoader.class.getResourceAsStream(resourceName);
            byte[] bytes = new byte[resource.available()];
            resource.read(bytes);
            Image image = Toolkit.getDefaultToolkit().createImage(bytes);
            MediaTracker tracker = new MediaTracker(trackerComponent);
            tracker.addImage(image, 0);
            tracker.waitForID(0);
            cache.put(resourceName, image);
            return image;
        } catch (Exception ex) {
            System.out.println("ImageLoader() can't load '" + resourceName + "' with class '" + ImageLoader.class.getName() + "'.");
            ex.printStackTrace();
        }
        return null;
    }

    public static void flushCache() {
        cache.clear();
        cache = new Hashtable();
    }
}
