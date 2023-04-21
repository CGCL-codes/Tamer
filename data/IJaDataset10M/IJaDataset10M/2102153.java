package net.etherstorm.jOpenRPG;

import javax.swing.ImageIcon;
import net.etherstorm.jOpenRPG.utils.ExceptionHandler;
import java.util.Hashtable;
import java.net.URL;

/**
 * ImageLib is responsible for loading images from a directory structure
 * or jar archive.
 * 
 * @author Ted Berg
 * @version $Revision: 352 $
 * $Date: 2002-02-01 02:32:11 -0500 (Fri, 01 Feb 2002) $
 */
public class ImageLib {

    /**
	 *
	 */
    public ImageLib() {
    }

    /**
	 *
	 */
    private static Hashtable cache = new Hashtable();

    /**
	 *
	 */
    private static boolean debug = true;

    /**
	 * @return debug
	 */
    public static boolean isDebug() {
        return debug;
    }

    /**
	 * @param boolean newDebug
	 */
    public static void setDebug(boolean newDebug) {
        debug = newDebug;
    }

    /**
	 * @param name
	 * @return loads image
	 */
    public static ImageIcon loadImage(String name) {
        return loadAbsoluteImage("/images/" + name);
    }

    /**
	 * @param name
	 * @return load image
	 */
    public static ImageIcon loadAbsoluteImage(String name) {
        ImageIcon result = null;
        if ((name != null) && (!name.equals(""))) {
            URL url = ImageLib.class.getResource(name);
            result = loadUrlImage(url);
        }
        return result;
    }

    /**
	 * @param url
	 * @return loads image from cache
	 */
    public static ImageIcon loadUrlImage(URL url) {
        ImageIcon result = null;
        if (url != null) {
            result = (ImageIcon) cache.get(url.toString());
            if (result == null) {
                try {
                    result = new ImageIcon(url);
                    cache.put(url.toString(), result);
                } catch (NullPointerException npe) {
                    return null;
                }
            }
        }
        return result;
    }

    /**
	 * @param name of toolbar image
	 * @return loads toolbar button graphic
	 */
    public static ImageIcon loadToolbarImage(String name) {
        return loadImage("toolbarButtonGraphics/" + name);
    }
}
