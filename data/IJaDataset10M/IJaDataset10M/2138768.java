package thinlet;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;

/**
 * Manages icons used by a Thinlet instance.  This class ensures that only one instance of an
 * image should be held in memory at any one time.
 * @author Alex alex@frontlinesms.com
 */
public class IconManager {

    /** Thinlet object that owns this manager. */
    private final Thinlet thinlet;

    /**
	 * Icons stored by the manager.  We use weak references to the icons here so that if
	 * an icon is no longer used in the UI, it can be garbage collected.  Otherwise, we only
	 * ever need to keep one copy of a particular image in memory at any one time.
	 */
    private final HashMap<String, WeakIconReference> icons = new HashMap<String, WeakIconReference>();

    /**
	 * Create a new instance of this class.
	 * @param thinlet Thinlet that owns this manager
	 */
    IconManager(Thinlet thinlet) {
        this.thinlet = thinlet;
    }

    /**
	 * Gets an icon from {@link #icons}.
	 * @param location path to the icon
	 * @return The icon, or <code>null</code> if it could not be found.
	 */
    public synchronized Image getIcon(String location) {
        if (location == null) {
            return null;
        }
        WeakIconReference ref = this.icons.get(location);
        Image icon = null;
        if (ref != null) {
            icon = ref.get();
        }
        if (icon == null) {
            icon = this.getIcon(location, true);
            this.icons.put(location, new WeakIconReference(icon));
        }
        return icon;
    }

    /**
	 * Creates an image from the specified resource.
	 * To speed up loading the same images use a cache (a simple hashtable).
	 * And flush the resources being used by an image when you won't use it henceforward
	 *
	 * @param path is relative to your thinlet instance or the classpath, or an URL
	 * @param preload waits for the whole image if true, starts loading
	 * (and repaints, and updates the layout) only when required (painted, or size requested) if false
	 * @return the loaded image or null
	 */
    private Image getIcon(String path, boolean preload) {
        if ((path == null) || (path.length() == 0)) {
            return null;
        }
        Image image = null;
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                image = Toolkit.getDefaultToolkit().getImage(url);
            }
        } catch (Throwable e) {
        }
        if (image == null) {
            InputStream is = null;
            try {
                is = getClass().getResourceAsStream(path);
                if (is != null) {
                    byte[] data = new byte[is.available()];
                    is.read(data, 0, data.length);
                    image = thinlet.getToolkit().createImage(data);
                    is.close();
                } else {
                    image = Toolkit.getDefaultToolkit().getImage(new URL(path));
                }
            } catch (Throwable e) {
                if (is != null) try {
                    is.close();
                } catch (IOException ex) {
                }
            }
        }
        if (preload && (image != null)) {
            MediaTracker mediatracker = new MediaTracker(this.thinlet);
            mediatracker.addImage(image, 1);
            try {
                mediatracker.waitForID(1, 5000);
            } catch (InterruptedException ie) {
            }
        }
        return image;
    }
}

/**
 * {@link WeakReference} to an {@link Image}.  Specifically used here to reference an
 * icon.
 * @author Alex alex@frontlinesms.com
 */
class WeakIconReference extends WeakReference<Image> {

    public WeakIconReference(Image referent) {
        super(referent);
    }
}
