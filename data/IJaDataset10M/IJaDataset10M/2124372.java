package com.ynhenc.gis.ui.resource;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;
import com.ynhenc.comm.GisComLib;

public class Resource extends GisComLib {

    private static Class<Resource> klass = Resource.class;

    public static URL getUrl(String src) {
        return klass.getResource(RESOURCE_FOLDER + src);
    }

    public static InputStream getStream(String src) {
        return klass.getResourceAsStream(RESOURCE_FOLDER + src);
    }

    public static Image getImage(String src) {
        return getImage(getUrl(src));
    }

    public static Image getImage(String dir, String src) {
        return getIcon(dir, src).getImage();
    }

    public static BufferedImage getBufferedImage(String dir, String src) {
        Image image = getImage(dir, src);
        if (image == null) {
            return null;
        }
        Frame f = new Frame();
        f.addNotify();
        int w = image.getWidth(f), h = image.getHeight(f);
        BufferedImage bImage;
        bImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        bImage.getGraphics().drawImage(image, 0, 0, f);
        return bImage;
    }

    public static ImageIcon getIcon(String dir, String src) {
        String urlText = dir + "/" + src;
        URL url = getUrl(urlText);
        if (url == null) {
            debug.println(klass, "NULL URL PATH = " + urlText);
            return null;
        } else {
            return getIcon(getUrl(dir + "/" + src));
        }
    }

    private static ImageIcon getIcon(java.net.URL imageURL) {
        try {
            return new javax.swing.ImageIcon(imageURL);
        } catch (RuntimeException e) {
            e.printStackTrace();
            debug.println(klass, "ImageUrl =" + imageURL);
            return null;
        }
    }

    public static Image getImage(URL url) {
        try {
            Image image = Toolkit.getDefaultToolkit().getImage(url);
            image = loadImage(image);
            return image;
        } catch (Exception e) {
            return getImageNotFound();
        }
    }

    public static Image getImageNotFound() {
        return new ImageIcon(getUrl(IMAGE_NOT_FOUND_RERSOURCE)).getImage();
    }

    public static Dimension getImageSize(String rscName) {
        Image img = getImage(rscName);
        Component com = new Component() {
        };
        return new Dimension(img.getWidth(com), img.getHeight(com));
    }

    private static Image loadImage(Image image) {
        Component comp = new Component() {
        };
        MediaTracker tracker = new MediaTracker(comp);
        int id = 0;
        tracker.addImage(image, id);
        try {
            tracker.waitForID(id, 0);
        } catch (InterruptedException e) {
            e.printStackTrace(System.out);
        }
        tracker.removeImage(image, id);
        return image;
    }

    private static final String RESOURCE_FOLDER = "";

    public static final String IMAGE_NOT_FOUND_RERSOURCE = "image_not_found.jpg";
}
