package net.sf.fysix.leveleditor.util.image;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

/**
 * @author Markus
 */
public class ImageUtil {

    public static ImageIcon loadImageIcon(String path, Class c) {
        java.net.URL imgURL = c.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static ImageIcon loadImageIcon(String path) {
        return new ImageIcon(path);
    }

    public static Image loadImage(String path, Class c) {
        java.net.URL imgURL = c.getResource(path);
        if (imgURL != null) {
            return Toolkit.getDefaultToolkit().getImage(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static Image loadImage(String path) {
        return Toolkit.getDefaultToolkit().getImage(path);
    }

    public static Image loadImage(byte[] imgData) {
        return Toolkit.getDefaultToolkit().createImage(imgData);
    }
}
