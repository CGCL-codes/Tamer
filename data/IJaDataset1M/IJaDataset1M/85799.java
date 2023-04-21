package com.sts.webmeet.client;

import java.awt.*;
import java.applet.Applet;
import java.net.URL;

public class ImageUtil {

    public static Image getImage(String strRelativePath, java.awt.Component comp) {
        Image img = null;
        if (strRelativePath.charAt(0) == '/') {
            strRelativePath = strRelativePath.substring(1);
        }
        if (null != strRelativePath) {
            img = getImageFromArchive(strRelativePath, comp);
        } else {
            return null;
        }
        if (null == img) {
            img = getImageFromApplet(strRelativePath, comp);
        } else {
        }
        if (null == img) {
            return null;
        } else {
            return waitForImage(img, comp);
        }
    }

    private static Image getImageFromArchive(String strRelativePath, Component comp) {
        Image img = null;
        ClassLoader cl = comp.getClass().getClassLoader();
        Toolkit kit = comp.getToolkit();
        URL urlImage = cl.getResource(strRelativePath);
        if (null != urlImage) {
            img = kit.getImage(urlImage);
        }
        return img;
    }

    public static Image waitForImage(Image image, Component comp) {
        MediaTracker tracker = new MediaTracker(comp);
        tracker.addImage(image, 0);
        try {
            tracker.waitForAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (tracker.isErrorAny()) {
            System.out.println("error loading image: " + image);
            return null;
        }
        tracker.removeImage(image);
        return image;
    }

    public static Image getImageFromApplet(String strRelativePath, Component comp) {
        Applet applet = findContainingApplet(comp);
        URL urlImage = null;
        Image image = null;
        if (null != applet) {
            try {
                urlImage = new URL(applet.getCodeBase(), strRelativePath);
                image = applet.getImage(urlImage);
                return image;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("ImageUtil: findContainingApplet gave null");
            return null;
        }
    }

    private static Applet findContainingApplet(java.awt.Component comp) {
        while (comp != null) {
            if (comp instanceof java.applet.Applet) {
                return (Applet) comp;
            } else {
                comp = comp.getParent();
            }
        }
        return null;
    }
}
