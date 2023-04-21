package org.parsel.gui.util;

import java.net.URL;
import javax.swing.ImageIcon;
import org.parsel.log.Log;

public class IconUtil {

    static Log log = Log.getLogger(ImageIcon.class);

    private IconUtil() {
    }

    /**
     * @return the icon; <code>null</code> if an error occurred
     */
    public static ImageIcon getIcon(String iconTheme, String pixelSize, String fileName) {
        ImageIcon imageIcon = null;
        String path = null;
        try {
            path = "/org/parsel/gui/icons/" + iconTheme + "/" + pixelSize + "/" + fileName;
            URL imageUrl = IconUtil.class.getResource(path);
            imageIcon = new ImageIcon(imageUrl);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            log.entering("getImageIcon(" + iconTheme + "," + pixelSize + "," + fileName + ")");
            log.error("Error when creating icon. Error: " + ex + ", path: " + path);
        }
        return imageIcon;
    }
}
