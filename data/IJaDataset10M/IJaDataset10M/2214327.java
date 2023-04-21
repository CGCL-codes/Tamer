package com.g2d.display;

import java.awt.Graphics2D;

public abstract class Tip extends DisplayObjectContainer {

    public static int tip_offset_x = 20;

    public static int tip_offset_y = 20;

    public static int tip_opposite_x = -4;

    public static int tip_opposite_y = -4;

    public void setLocation(DisplayObjectContainer parent, int x, int y) {
        if ((x + getWidth() + tip_offset_x) > parent.getWidth()) {
            x = x - getWidth() + tip_opposite_x;
        } else {
            x = x + tip_offset_x;
        }
        if ((y + getHeight() + tip_offset_y) > parent.getHeight()) {
            y = y - getHeight() + tip_opposite_y;
        } else {
            y = y + tip_offset_y;
        }
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        super.setLocation(x, y);
    }

    @Override
    protected final boolean testCatchMouse(Graphics2D g) {
        return false;
    }

    @Override
    public final void added(DisplayObjectContainer parent) {
    }

    @Override
    public final void removed(DisplayObjectContainer parent) {
    }
}
