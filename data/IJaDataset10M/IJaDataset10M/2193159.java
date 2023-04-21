package net.sealisland.swing.plaf.windows;

import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.UIManager;
import com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel;

public class XPToolkit {

    public static boolean isXP() {
        return !(UIManager.getLookAndFeel() instanceof WindowsClassicLookAndFeel);
    }

    public static BufferedImage[] getImages(String name) {
        return getImages(name, false);
    }

    public static BufferedImage[] getImages(String name, boolean trimEdges) {
        String imageName = getString(name + ".imagefile");
        Map imageMap = (Map) Toolkit.getDefaultToolkit().getDesktopProperty("win.xpstyle.resources.images");
        if (imageMap == null) {
            return null;
        }
        BufferedImage image = (BufferedImage) imageMap.get(imageName);
        int imageCount = getInt(name + ".imagecount");
        Insets insets = trimEdges ? getInsets(name + ".sizingmargins") : new Insets(0, 0, 0, 0);
        BufferedImage[] images = new BufferedImage[imageCount];
        for (int idx = 0; idx < images.length; idx++) {
            int imageHeight = image.getHeight() / imageCount;
            int x = insets.left;
            int y = (idx * imageHeight) + insets.top;
            int w = image.getWidth() - insets.left - insets.right;
            int h = imageHeight - insets.top - insets.bottom;
            images[idx] = image.getSubimage(x, y, w, h);
        }
        return images;
    }

    public static int getInt(String name) {
        return Integer.parseInt(getString(name));
    }

    public static Insets getInsets(String name) {
        String str = getString(name);
        String[] parts = str.split(", ");
        int left = Integer.parseInt(parts[0]);
        int right = Integer.parseInt(parts[1]);
        int top = Integer.parseInt(parts[2]);
        int bottom = Integer.parseInt(parts[3]);
        return new Insets(top, left, bottom, right);
    }

    public static String getString(String name) {
        Map stringMap = (Map) Toolkit.getDefaultToolkit().getDesktopProperty("win.xpstyle.resources.strings");
        if (stringMap == null) {
            return null;
        }
        return (String) stringMap.get(name);
    }
}
