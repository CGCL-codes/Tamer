package org.scilab.forge.jlatexmath;

import java.awt.Graphics2D;
import java.util.ListIterator;

/**
 * A box composed of other boxes, put one above the other.
 */
class VerticalBox extends Box {

    private float leftMostPos = Float.MAX_VALUE;

    private float rightMostPos = Float.MIN_VALUE;

    public VerticalBox() {
    }

    public VerticalBox(Box b, float rest, int alignment) {
        this();
        add(b);
        if (alignment == TeXConstants.ALIGN_CENTER) {
            StrutBox s = new StrutBox(0, rest / 2, 0, 0);
            super.add(0, s);
            height += rest / 2;
            depth += rest / 2;
            super.add(s);
        } else if (alignment == TeXConstants.ALIGN_TOP) {
            depth += rest;
            super.add(new StrutBox(0, rest, 0, 0));
        } else if (alignment == TeXConstants.ALIGN_BOTTOM) {
            height += rest;
            super.add(0, new StrutBox(0, rest, 0, 0));
        }
    }

    public final void add(Box b) {
        super.add(b);
        if (children.size() == 1) {
            height = b.height;
            depth = b.depth;
        } else depth += b.height + b.depth;
        recalculateWidth(b);
    }

    private void recalculateWidth(Box b) {
        leftMostPos = Math.min(leftMostPos, b.shift);
        rightMostPos = Math.max(rightMostPos, b.shift + (b.width > 0 ? b.width : 0));
        width = rightMostPos - leftMostPos;
    }

    public void add(int pos, Box b) {
        super.add(pos, b);
        if (pos == 0) {
            depth += b.depth + height;
            height = b.height;
        } else depth += b.height + b.depth;
        recalculateWidth(b);
    }

    public void draw(Graphics2D g2, float x, float y) {
        float yPos = y - height;
        for (Box b : children) {
            yPos += b.getHeight();
            b.draw(g2, x + b.getShift() - leftMostPos, yPos);
            yPos += b.getDepth();
        }
    }

    public int getSize() {
        return children.size();
    }

    public int getLastFontId() {
        int fontId = TeXFont.NO_FONT;
        for (ListIterator it = children.listIterator(children.size()); fontId == TeXFont.NO_FONT && it.hasPrevious(); ) fontId = ((Box) it.previous()).getLastFontId();
        return fontId;
    }
}
