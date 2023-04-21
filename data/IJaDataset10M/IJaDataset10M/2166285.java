package org.dlib.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

public class FlexBorder implements Border {

    public static final int NONE = 0;

    public static final int TOP_LINE = 1;

    public static final int BOTTOM_LINE = 2;

    public static final int RECT = 3;

    private Insets insets = new Insets(0, 0, 0, 0);

    private Color color = Color.blue;

    private int type = RECT;

    public FlexBorder() {
    }

    public FlexBorder(Color c, int t) {
        color = c;
        type = t;
    }

    public void setColor(Color c) {
        color = c;
    }

    public void setType(int t) {
        type = t;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        switch(type) {
            case TOP_LINE:
                g.drawLine(x, y, x + width - 1, y);
                break;
            case BOTTOM_LINE:
                g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
                break;
            case RECT:
                g.drawRect(x, y, width - 1, height - 1);
                break;
        }
    }

    public Insets getBorderInsets(Component c) {
        return insets;
    }

    public boolean isBorderOpaque() {
        return false;
    }
}
