package org.parsel.gui.util;

import java.awt.Component;
import javax.swing.JOptionPane;

public class InputUtil {

    private InputUtil() {
    }

    public static Integer getInt(Component parent, String string, int min, int max) throws NumberFormatException {
        return getInt(parent, string, min, max, ((max - min) / 2));
    }

    public static Integer getInt(Component parent, String string, int min, int max, int defaultValue) throws NumberFormatException {
        Integer n = null;
        if (max <= min) throw new IllegalArgumentException("max <= min");
        String s = JOptionPane.showInputDialog(parent, string + "  [" + min + ";" + max + "]", "" + defaultValue);
        if (s != null) {
            n = new Integer(Integer.parseInt(s));
            if (n < min || n > max) throw new NumberFormatException("please enter an integer between " + min + " and " + max + " inclusive");
        }
        return n;
    }
}
