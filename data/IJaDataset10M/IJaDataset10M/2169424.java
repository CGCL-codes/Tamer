package com.mscg.jmp3.util;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Util {

    public static Integer panelHeight;

    public static Dimension maxSmallIconButtonSize = new Dimension(27, 27);

    public static int getPanelHeightForFont(Font font) {
        if (panelHeight == null) {
            JPanel panel = new JPanel();
            JTextField field = new JTextField();
            panel.add(field);
            panelHeight = (int) field.getPreferredSize().getHeight();
        }
        return panelHeight;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isEmptyOrWhiteSpaceOnly(String string) {
        return isEmpty(string) || string.trim().length() == 0;
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static boolean isNotEmptyOrWhiteSpaceOnly(String string) {
        return !isEmptyOrWhiteSpaceOnly(string);
    }

    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }
}
