package org.jmeld.ui.util;

import javax.swing.*;
import java.awt.*;

public class EmptyIcon implements Icon {

    private int width;

    private int height;

    private Color color;

    public EmptyIcon(Color color, int width, int height) {
        this.color = color;
        this.width = width;
        this.height = height;
    }

    public EmptyIcon(int width, int height) {
        this(null, width, height);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (color != null) {
            g.setColor(color);
            g.fillRect(x, y, getIconWidth(), getIconHeight());
        }
    }
}
