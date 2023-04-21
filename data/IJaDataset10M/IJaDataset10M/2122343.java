package com.g2d.java2d.impl;

import java.text.AttributedString;
import com.g2d.Graphics2D;
import com.g2d.geom.Rectangle;
import com.g2d.geom.Shape;
import com.g2d.text.TextLayout;

public class AwtTextLayout implements TextLayout {

    protected AwtTextLayout(AttributedString text) {
    }

    @Override
    public Shape getCaretShape() {
        return null;
    }

    @Override
    public void draw(Graphics2D g, int x, int y) {
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    public int getCharacterCount() {
        return 0;
    }

    @Override
    public int getInsertionIndex() {
        return 0;
    }
}
