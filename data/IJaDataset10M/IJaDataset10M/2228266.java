package net.sf.bbarena.view.util;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

public class BitmapManager {

    private static GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

    private static Map<String, GuiIcon> iconCache = new HashMap<String, GuiIcon>();

    public static GraphicsConfiguration getGC() {
        return gc;
    }

    public static BufferedImage loadImage(String f, boolean transparent) {
        return loadImage(gc, f, transparent);
    }

    public static BufferedImage loadImage(GraphicsConfiguration gc, String f, boolean transparent) {
        if (transparent) {
            return loadTransparentImage(gc, f);
        } else {
            return loadImage(gc, f);
        }
    }

    public static GuiIcon loadIcon(String f) {
        GuiIcon result = iconCache.get(f);
        if (result == null) {
            result = new GuiIcon(loadImage(f, true));
            iconCache.put(f, result);
        }
        return result;
    }

    public static GuiIcon loadIconPreserveAlpha(String f) {
        GuiIcon result = iconCache.get(f);
        if (result == null) {
            result = new GuiIcon(loadImagePreserveAlpha(gc, f));
            iconCache.put(f, result);
        }
        return result;
    }

    /**
     * @param gc
     * @param f
     * @return
     */
    private static BufferedImage loadImage(GraphicsConfiguration gc, String f) {
        ImageIcon ic = openImageIcon(f);
        int w = ic.getIconWidth();
        int h = ic.getIconHeight();
        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(ic.getImage(), 0, 0, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedImage bi = gc.createCompatibleImage(w, h);
        bi.setRGB(0, 0, w, h, pixels, 0, w);
        return bi;
    }

    private static BufferedImage loadImagePreserveAlpha(GraphicsConfiguration gc, String f) {
        ImageIcon ic = openImageIcon(f);
        int w = ic.getIconWidth();
        int h = ic.getIconHeight();
        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(ic.getImage(), 0, 0, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedImage bi = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        bi.setRGB(0, 0, w, h, pixels, 0, w);
        return bi;
    }

    /**
     * @param gc
     * @param f
     * @return
     */
    private static BufferedImage loadTransparentImage(GraphicsConfiguration gc, String f) {
        ImageIcon ic = openImageIcon(f);
        int w = ic.getIconWidth();
        int h = ic.getIconHeight();
        int[] pixels = new int[w * h];
        PixelGrabber pg = new PixelGrabber(ic.getImage(), 0, 0, w, h, pixels, 0, w);
        try {
            pg.grabPixels();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int bottomLeftP = pixels[(w - 1) + (h - 1) * w] & 0xffffff;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p = pixels[x + y * w] & 0xffffff;
                if (p == bottomLeftP) {
                    pixels[x + y * w] = p;
                } else {
                    pixels[x + y * w] = p | 0xff000000;
                }
            }
        }
        BufferedImage bi = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        bi.setRGB(0, 0, w, h, pixels, 0, w);
        return bi;
    }

    /**
     * @param f
     * @return
     */
    private static ImageIcon openImageIcon(String f) {
        return new ImageIcon(net.sf.bbarena.view.util.FileUtils.getAsURL(f));
    }
}
