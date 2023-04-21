package com.pcmsolutions.gui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author  pmeehan
 */
public class EmptyIcon implements Icon {

    private static EmptyIcon INSTANCE = new EmptyIcon(0, 0);

    int w, h;

    public EmptyIcon(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public static EmptyIcon getInstance() {
        return INSTANCE;
    }

    public void paintIcon(Component component, Graphics graphics, int x, int y) {
    }

    public int getIconWidth() {
        return w;
    }

    public int getIconHeight() {
        return h;
    }
}
