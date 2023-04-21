package foucault.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImgUtils {

    public static int[] splitRGB(int c) {
        int rgb[] = new int[3];
        splitRGB(c, rgb);
        return rgb;
    }

    public static void splitRGB(int c, int[] rgb) {
        rgb[0] = (c >> 16) & 0xFF;
        rgb[1] = (c >> 8) & 0xFF;
        rgb[2] = (c >> 0) & 0xFF;
    }

    public static int brightness(int rgb) {
        return brightness3b(rgb) / 3;
    }

    public static int brightness3b(int rgb) {
        return (((rgb >> 16) & 0xFF) + ((rgb >> 8) & 0xFF) + ((rgb >> 0) & 0xFF));
    }

    public static int joinRGB(int[] rgb) {
        return joinRGB(rgb[0], rgb[1], rgb[2]);
    }

    public static int joinRGB(int r, int g, int b) {
        return r << 16 | g << 8 | b | ~0xffffff;
    }

    public static BufferedImage clone(BufferedImage image) {
        if (image == null) {
            return null;
        }
        BufferedImage clone = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = clone.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return clone;
    }

    public static BufferedImage mergeLight(BufferedImage a, BufferedImage b) {
        if (a == null && b == null) {
            return null;
        }
        if (a == null) {
            return clone(b);
        }
        if (b == null) {
            return clone(a);
        }
        BufferedImage img = clone(a);
        int width = img.getWidth();
        int height = img.getHeight();
        int[] ai = new int[width * height];
        int[] bi = new int[width * height];
        a.getRGB(0, 0, width, height, ai, 0, width);
        b.getRGB(0, 0, width, height, bi, 0, width);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * width + x;
                int[] rgb1 = splitRGB(ai[i]);
                int[] rgb2 = splitRGB(bi[i]);
                int[] rgb3 = new int[] { Math.max(rgb1[0], rgb2[0]), Math.max(rgb1[1], rgb2[1]), Math.max(rgb1[2], rgb2[2]) };
                ai[i] = joinRGB(rgb3);
            }
        }
        img.setRGB(0, 0, width, height, ai, 0, width);
        return img;
    }

    public static int brightness2rgb(int c) {
        return c << 16 | c << 8 | c | ~0xffffff;
    }

    static final int setRGBA_rgb[] = new int[3];

    public static void setRGBA(int[] pixel, int pindex, int r, int g, int b, int a) {
        splitRGB(pixel[pindex], setRGBA_rgb);
        setRGBA_rgb[0] = (setRGBA_rgb[0] * (255 - a) + r * a) / 255;
        setRGBA_rgb[1] = (setRGBA_rgb[1] * (255 - a) + g * a) / 255;
        setRGBA_rgb[2] = (setRGBA_rgb[2] * (255 - a) + b * a) / 255;
        pixel[pindex] = joinRGB(setRGBA_rgb);
    }

    public static void debugImage(int[] imgArr, int width) {
        int height = imgArr.length / width;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, width, height, imgArr, 0, width);
        debugImage(image);
    }

    public static void debugImage(BufferedImage image) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static int cp(int i) {
        return Math.max(0, Math.min(255, i));
    }

    public static int joinRGBA(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }
}
