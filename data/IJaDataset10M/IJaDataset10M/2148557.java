package org.bd.banglasms.util;

import java.io.IOException;
import java.util.Hashtable;
import javax.microedition.lcdui.Image;
import org.bd.banglasms.App;
import org.bd.banglasms.Logger;
import org.bd.banglasms.ResourceManager;

/**
 * A {@link ResourceManager} implementation that caches all the resources after
 * first fetch.
 *
 */
public class ResourceManagerImpl implements ResourceManager {

    private Hashtable cache = new Hashtable();

    public Image fetchImage(String url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("ResourceManager cannot accept null url");
        }
        Object object = cache.get(url);
        if (object == null) {
            object = loadImage(url);
            cache.put(url, object);
        }
        return (Image) object;
    }

    public Image fetchImageSafely(String url) {
        try {
            return fetchImage(url);
        } catch (IOException ex) {
            App.getLogger().log("Util.getImageSafely (" + url + ") caught " + ex, Logger.LEVEL_WARNING);
        }
        return null;
    }

    protected Image loadImage(String url) throws IOException {
        return Image.createImage(url);
    }
}
