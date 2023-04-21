package com.swgman.util;

import java.awt.*;
import java.io.*;
import java.util.prefs.*;
import javax.swing.*;
import com.swgman.models.*;

public class PrefUtils {

    public static void readSelectedItem(Preferences pref, String name, ListFieldModel listFieldModel) {
        final int size = listFieldModel.getSize();
        if (size == 0) return;
        int prefIndex = pref.node(name).getInt("selectedIndex", -2);
        if (prefIndex >= 0 && prefIndex < size) listFieldModel.setSelectedItem(listFieldModel.getElementAt(prefIndex)); else if (prefIndex == -1) listFieldModel.setSelectedItem(null);
    }

    public static void writeSelectedItem(Preferences pref, String name, ListFieldModel listFieldModel) {
        final Preferences node = pref.node(name);
        final Object selectedItem = listFieldModel.getSelectedItem();
        if (selectedItem == null) node.putInt("selectedIndex", -1); else node.putInt("selectedIndex", listFieldModel.getIndexOf(selectedItem));
    }

    public static boolean readSplitPosition(Preferences pref, JSplitPane splitPane) {
        int dividerLocation = pref.getInt("dividerLocation", -1);
        if (dividerLocation != -1) {
            splitPane.setDividerLocation(dividerLocation);
            return true;
        }
        return false;
    }

    public static void writeSplitPosition(Preferences pref, JSplitPane splitPane) {
        pref.putInt("dividerLocation", splitPane.getDividerLocation());
    }

    public static File getFile(Preferences pref, String name) {
        String str = pref.get(name, null);
        return str == null ? null : new File(str);
    }

    public static void putFile(Preferences pref, String name, File file) {
        pref.put(name, file.getAbsolutePath());
    }

    public static void putRectangle(Preferences pref, Rectangle rect) {
        pref.putInt("x", rect.x);
        pref.putInt("y", rect.y);
        pref.putInt("width", rect.width);
        pref.putInt("height", rect.height);
    }

    public static Rectangle getRectangle(Preferences pref, Rectangle def) {
        def = def == null ? new Rectangle() : new Rectangle(def);
        def.x = pref.getInt("x", def.x);
        def.y = pref.getInt("y", def.y);
        def.width = pref.getInt("width", def.width);
        def.height = pref.getInt("height", def.height);
        return def;
    }
}
