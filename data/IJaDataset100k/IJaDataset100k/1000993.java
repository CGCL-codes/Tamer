package edu.belmont.mth.visigraph.resources;

import java.awt.*;
import java.net.*;
import java.util.*;
import edu.belmont.mth.visigraph.utilities.*;

/**
 * @author Cameron Behar
 */
public class CursorBundle extends ResourceBundle {

    private static Map<String, Cursor> map = new HashMap<String, Cursor>();

    private static final ResourceBundle instance = ResourceBundle.getBundle("edu.belmont.mth.visigraph.resources.CursorBundle");

    public static Cursor get(String key) {
        try {
            return (Cursor) instance.getObject(key);
        } catch (MissingResourceException ex) {
            DebugUtilities.logException(String.format("An exception occurred while trying to load resource %s.", key), ex);
            return null;
        }
    }

    @Override
    public Enumeration<String> getKeys() {
        return (new Vector<String>(map.keySet())).elements();
    }

    @Override
    protected final Object handleGetObject(String key) {
        return this.loadCursor(key, ".gif");
    }

    private Cursor loadCursor(String filename, String extension) {
        String imageName = filename + extension;
        Cursor cursor = map.get(imageName);
        if (cursor != null) return cursor;
        URL url = this.getClass().getResource("cursors/" + imageName);
        if (url == null) return null;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        cursor = toolkit.createCustomCursor(toolkit.getImage(url), new Point(1, 1), "");
        map.put(imageName, cursor);
        return cursor;
    }
}
