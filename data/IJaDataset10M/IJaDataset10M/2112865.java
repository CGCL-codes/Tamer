package org.taddei.jemv.gui.img;

import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconLoader {

    private IconLoader() {
    }

    public static final Image loadImage(String s) {
        Image image = null;
        URL url = IconLoader.class.getResource(s);
        image = new ImageIcon(url).getImage();
        return image;
    }

    public static final Image loadTransparentImage(String s) {
        return loadTransparentImage(s, 0);
    }

    public static final Image loadTransparentImage(String s, int transpValue) {
        Image icon = loadImage(s);
        ImageFilter filter = new IconLoader.GreyishToClearFilter();
        ImageProducer filt_ip = new FilteredImageSource(icon.getSource(), filter);
        Image newImage = java.awt.Toolkit.getDefaultToolkit().createImage(filt_ip);
        return newImage;
    }

    static class GreyishToClearFilter extends RGBImageFilter {

        GreyishToClearFilter() {
            this.canFilterIndexColorModel = true;
        }

        @Override
        public int filterRGB(int x, int y, int a_rgb) {
            int r = (a_rgb & 0x00ff0000) >> 16;
            int g = (a_rgb & 0x0000ff00) >> 8;
            int b = a_rgb & 0x000000ff;
            if (r > 90 && r < 195 && g > 90 && g < 195 && b > 90 && b < 195) {
                a_rgb &= 0x00ffffff;
            }
            return a_rgb;
        }
    }

    public static Icon loadImageIcon(String string) {
        return new ImageIcon(loadImage(string));
    }

    public static Icon loadTransparentImageIcon(String string) {
        return new ImageIcon(loadImage(string));
    }
}
