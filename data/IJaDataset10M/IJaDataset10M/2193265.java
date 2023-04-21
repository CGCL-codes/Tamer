package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.util;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.*;

/**
 * Image processing methods.
 *
 * @author  Werner Randelshofer, Karl von Randow
 * @version 2.0 2006-12-24 by Karl von Randow: On the fly conversion from Aqua
 * Blue to Aqua Graphite appearance added.
 * <br>1.0.2 2005-09-12 Brought my work-around for Java 1.4.1 back to
 * live.
 * <br>1.0.1 2005-05-21 Accidentaly used bitmask transparency
 * instead of translucent transparency.
 * <br>1.0  13 March 2005  Created.
 */
public class Images {

    /** Prevent instance creation. */
    private Images() {
    }

    private static GraphiteFilter graphiteFilter = new GraphiteFilter();

    public static Image createImage(URL resource) {
        Image image = Toolkit.getDefaultToolkit().createImage(resource);
        return image;
    }

    private static Properties canGraphite;

    private static boolean canGraphite(URL resource) {
        if (canGraphite == null) {
            synchronized (Images.class) {
                if (canGraphite == null) {
                    Properties p = new Properties();
                    try {
                        p.load(Images.class.getResourceAsStream("graphiteable.properties"));
                    } catch (IOException e) {
                        System.err.println("Failed to load graphiteable.properties: " + e);
                    }
                    canGraphite = p;
                }
            }
        }
        String file = resource.getFile();
        int i = file.lastIndexOf(File.separatorChar);
        if (i != -1) {
            file = file.substring(i + 1);
        }
        return canGraphite.containsKey(file);
    }

    /**
     * This method returns a buffered image with the contents of an image.
     *
     * Code derived from the Java Developers Almanac 1.4
     * http://javaalmanac.com/egs/java.awt.image/Image2Buf.html?l=rel
     */
    private static Image toGraphite(Image image) {
        return Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), graphiteFilter));
    }

    /**
     * Based on a code example from:
     * http://tams-www.informatik.uni-hamburg.de/applets/hades/webdemos/00-intro/02-imageprocessing/saturation.html
     * @author karlvr
     *
     */
    public static class GraphiteFilter extends RGBImageFilter {

        private static final float saturationAdjust = 0.179f;

        private static float hueAdjust = 0.0052f;

        private static float brightnessAdjust = 0.09f;

        private float[] hsb = new float[3];

        public int filterRGB(int x, int y, int rgb) {
            int alpha = rgb & 0xff000000;
            int red = (rgb >> 16) & 0xff;
            int green = (rgb >> 8) & 0xff;
            int blue = rgb & 0xff;
            float RW = (1f - saturationAdjust) * 0.333f;
            float RG = (1f - saturationAdjust) * 0.333f;
            float RB = (1f - saturationAdjust) * 0.333f;
            float a = RW + saturationAdjust;
            float b = RW;
            float c = RW;
            float d = RG;
            float e = RG + saturationAdjust;
            float f = RG;
            float g = RB;
            float h = RB;
            float i = RB + saturationAdjust;
            int outputRed = (int) (a * red + d * green + g * blue);
            int outputGreen = (int) (b * red + e * green + h * blue);
            int outputBlue = (int) (c * red + f * green + i * blue);
            return alpha | (outputRed << 16) | (outputGreen << 8) | (outputBlue);
        }
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        if (System.getProperty("java.version").startsWith("1.4.1_")) {
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        } else {
            boolean hasAlpha;
            try {
                hasAlpha = hasAlpha(image);
            } catch (IllegalAccessError e) {
                hasAlpha = true;
            }
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            try {
                int transparency = Transparency.OPAQUE;
                if (hasAlpha) {
                    transparency = Transparency.TRANSLUCENT;
                }
                GraphicsDevice gs = ge.getDefaultScreenDevice();
                GraphicsConfiguration gc = gs.getDefaultConfiguration();
                bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
            } catch (Exception e) {
            }
            if (bimage == null) {
                int type = BufferedImage.TYPE_INT_RGB;
                if (hasAlpha) {
                    type = BufferedImage.TYPE_INT_ARGB;
                }
                bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
            }
        }
        Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    /**
     * This method returns true if the specified image has transparent pixels
     *
     * Code taken from the Java Developers Almanac 1.4
     * http://javaalmanac.com/egs/java.awt.image/HasAlpha.html
     */
    public static boolean hasAlpha(Image image) {
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    /**
     * Splits an image into count subimages.
     */
    public static BufferedImage[] split(Image image, int count, boolean isHorizontal) {
        BufferedImage src = Images.toBufferedImage(image);
        if (count == 1) {
            return new BufferedImage[] { src };
        }
        BufferedImage[] parts = new BufferedImage[count];
        for (int i = 0; i < count; i++) {
            if (isHorizontal) {
                parts[i] = src.getSubimage(src.getWidth() / count * i, 0, src.getWidth() / count, src.getHeight());
            } else {
                parts[i] = src.getSubimage(0, src.getHeight() / count * i, src.getWidth(), src.getHeight() / count);
            }
        }
        return parts;
    }
}
