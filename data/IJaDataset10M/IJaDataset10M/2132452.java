package org.jdesktop.swingx.icon;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.Icon;

/**
 * An empty icon with arbitrary width and height.
 */
public final class EmptyIcon implements Icon, Serializable {

    private int width;

    private int height;

    public EmptyIcon() {
        this(0, 0);
    }

    public EmptyIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
    }
}
