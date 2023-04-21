package de.uni_muenster.cs.sev.lethal.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * Common resource for loading functions.
 * @author Philipp
 *
 */
public class Resources {

    /**
	 * Loads an icon from the system resources.
	 * @param name filename of the icon to load
	 * @return icon object or null if the icon was not found
	 */
    public static ImageIcon loadIcon(String name) {
        URL url = ClassLoader.getSystemResource(name);
        if (url != null) return new ImageIcon(url);
        return null;
    }

    /**
	 * Loads an image from the system resources.
	 * @param name filename of the icon to load
	 * @return image object or null if the image was not found
	 */
    public static Image loadImage(String name) {
        URL url = ClassLoader.getSystemResource(name);
        if (url != null) return Toolkit.getDefaultToolkit().getImage(url);
        return null;
    }
}
