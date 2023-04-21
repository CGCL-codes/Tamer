package org.tastefuljava.minica;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JComponent;

public class Util {

    /** Private constructor to disallow instanciation */
    private Util() {
    }

    public static void adjustHeight(JComponent comp, int height) {
        Dimension size = comp.getMinimumSize();
        size.height = height;
        comp.setMinimumSize(size);
        size = comp.getPreferredSize();
        size.height = height;
        comp.setPreferredSize(size);
    }

    public static void clearWidth(JComponent comp) {
        Dimension size = comp.getMinimumSize();
        size.width = 0;
        comp.setMinimumSize(size);
        size = comp.getPreferredSize();
        size.width = 0;
        comp.setPreferredSize(size);
    }

    public static void clearWidthAll(Container cont, Class clazz) {
        Component children[] = cont.getComponents();
        for (int i = 0; i < children.length; ++i) {
            Component child = children[i];
            if (clazz.isInstance(child)) {
                clearWidth((JComponent) child);
            }
            if (child instanceof Container) {
                clearWidthAll((Container) child, clazz);
            }
        }
    }
}
