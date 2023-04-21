package com.emental.mindraider.ui.gfx;

import java.awt.Component;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

/**
 * Icons and images registry.
 *
 * @author Martin.Dvorak
 * @author Francesco.Tinti
 * @version $Revision: 1.7 $ ($Author: ftinti $)
 */
public final class IconsRegistry extends Component {

    /**
     * The const for icon directory.
     */
    public static final String DIRECTORY_ICONS = "resources/images/";

    /**
     * The directory image.
     */
    public static final String DIRECTORY_IMAGES = "resources/icons/";

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(IconsRegistry.class);

    /**
     * The serial version uid for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    private IconsRegistry() {
    }

    /**
     * Get an image.
     *
     * @param name
     *            the image file name
     * @return the Image object
     */
    public static Image getImage(String name) {
        logger.debug("Loading image: " + name + " from " + DIRECTORY_IMAGES + name);
        Image img = null;
        try {
            URL fileLoc = ClassLoader.getSystemClassLoader().getResource(DIRECTORY_IMAGES + name);
            ImageIcon imgIcon = new ImageIcon(fileLoc, "Image");
            img = imgIcon.getImage();
        } catch (Exception e) {
            logger.debug("error loading image resource: " + name, e);
        }
        return img;
    }

    /**
     * Return the ImageIcon object.
     *
     * @param name
     *            the icon file name
     * @return the ImageIcon object
     */
    public static ImageIcon getImageIcon(String name) {
        ImageIcon imgIcon = null;
        try {
            URL fileLoc = ClassLoader.getSystemClassLoader().getResource(DIRECTORY_ICONS + name);
            imgIcon = new ImageIcon(fileLoc, "Icon");
        } catch (Exception e) {
            logger.debug("error loading image icon resource: " + name, e);
        }
        return imgIcon;
    }
}
