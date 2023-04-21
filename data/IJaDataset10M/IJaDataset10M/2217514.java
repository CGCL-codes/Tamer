package org.openscience.jmol;

import java.io.PrintStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.text.*;
import java.util.*;
import java.net.*;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

class JmolResourceHandler {

    private static ResourceBundle rb;

    private String baseKey;

    static boolean initialized = false;

    public static void initialize(String resources) {
        if (initialized) return;
        JmolResourceHandler.rb = ResourceBundle.getBundle(resources);
        initialized = true;
    }

    JmolResourceHandler(String string) {
        Jmol.initialize();
        baseKey = string;
    }

    synchronized ImageIcon getIcon(String key) {
        String iname = null;
        String resourceName = null;
        try {
            resourceName = rb.getString(getQualifiedKey(key));
            iname = "org/openscience/jmol/images/" + resourceName;
        } catch (MissingResourceException e) {
        }
        if (iname != null) {
            URL imageUrl = ClassLoader.getSystemResource(iname);
            if (imageUrl != null) {
                return new ImageIcon(imageUrl);
            } else {
                System.err.println("Warning: unable to load " + resourceName + " for icon " + key + ".");
            }
        }
        return null;
    }

    synchronized String getString(String string) {
        String ret = null;
        try {
            ret = rb.getString(getQualifiedKey(string));
        } catch (MissingResourceException e) {
        }
        if (ret != null) return ret;
        return null;
    }

    synchronized Object getObject(String string) {
        Object o = null;
        try {
            o = rb.getObject(getQualifiedKey(string));
        } catch (MissingResourceException e) {
        }
        if (o != null) return o;
        return null;
    }

    synchronized String getQualifiedKey(String string) {
        return baseKey + "." + string;
    }
}
