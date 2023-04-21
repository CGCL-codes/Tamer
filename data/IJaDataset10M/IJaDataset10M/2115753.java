package no.truben.dbox.util;

import no.truben.dbox.Main;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import nl.ikarus.nxt.priv.imageio.icoreader.obj.ICOFile;
import nl.ikarus.nxt.priv.imageio.icoreader.obj.IconEntry;

/**
 * Class that includes methods that manipulate images
 * 
 * @author pederskeidsvoll
 */
public class ImageHandlerer {

    private static ImageIcon defaultIcon;

    /**
     * Loads a icon from the file system
     * @param ikon the path to the icon
     * @return the icon
     */
    public static ImageIcon getImageIcon(String ikon) {
        if (ikon.equals("")) {
            return getDefaultIcon();
        } else {
            ImageIcon ii = getDefaultIcon();
            if (ikon.toLowerCase().endsWith("ico")) {
                try {
                    ImageInputStream in = ImageIO.createImageInputStream(new FileInputStream(new File(ikon)));
                    ICOFile f;
                    f = new ICOFile(in);
                    IconEntry ie = f.getEntry(0);
                    ii = new ImageIcon(ie.getBitmap().getImage());
                } catch (IOException ex) {
                    System.out.println("Error reading icon " + ikon);
                    return getDefaultIcon();
                }
            } else {
                ii = new ImageIcon(ikon);
            }
            return (resizeIcon(ii));
        }
    }

    /**
     * Loads a icon from the file system
     * @param ikon the path to the icon
     * @return the icon
     */
    public static ImageIcon getImageIconNoResize(String ikon) {
        if (ikon.equals("")) {
            return getDefaultIcon();
        } else {
            ImageIcon ii = getDefaultIcon();
            if (ikon.toLowerCase().endsWith("ico")) {
                try {
                    ImageInputStream in = ImageIO.createImageInputStream(new FileInputStream(new File(ikon)));
                    ICOFile f;
                    f = new ICOFile(in);
                    IconEntry ie = f.getEntry(0);
                    ii = new ImageIcon(ie.getBitmap().getImage());
                } catch (IOException ex) {
                    System.out.println("Error reading icon " + ikon);
                    return getDefaultIcon();
                }
            } else {
                ii = new ImageIcon(ikon);
            }
            return ii;
        }
    }

    /**
     * Resizes a icon according to preferences
     * @param icon icon that should be resized
     * @return a resized icon
     */
    public static ImageIcon resizeIcon(ImageIcon icon) {
        if (Main.pref.isIconResize()) {
            int width = Main.pref.getIconWidth();
            int height = Main.pref.getIconHeight();
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            bi.getGraphics().drawImage(icon.getImage(), 0, 0, width, height, null);
            return new ImageIcon(bi);
        } else return icon;
    }

    /**
     * Resizes a icon according to preferences
     * @param icon icon that should be resized
     * @return a resized icon
     */
    public static BufferedImage resizeIcon(ImageIcon icon, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bi.getGraphics().drawImage(icon.getImage(), 0, 0, width, height, null);
        return bi;
    }

    /**
     * @return Returns the default application icon
     */
    public static ImageIcon getDefaultIcon() {
        if (defaultIcon == null) {
            defaultIcon = resizeIcon(new ImageIcon(Main.theme.getDefaultGame()));
        }
        return defaultIcon;
    }
}
