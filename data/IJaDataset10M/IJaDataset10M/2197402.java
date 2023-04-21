package org.voidness.oje2d.gui.docks;

import org.voidness.oje2d.GLColor;
import org.voidness.oje2d.GLImage;
import org.voidness.oje2d.gui.Widget;

public class HorizontalDockEdge extends Widget implements DockEdge {

    private GLImage left = null;

    private GLImage right = null;

    private GLImage back = null;

    private GLColor color = null;

    public HorizontalDockEdge(int mWidth, int mHeight) {
        super(mWidth, mHeight);
        color = GLColor.WHITE;
    }

    public HorizontalDockEdge(int xPos, int yPos, int mWidth, int mHeight) {
        super(xPos, yPos, mWidth, mHeight);
        color = GLColor.WHITE;
    }

    public void setImages(GLImage[] mEdges, String mBack) {
        left = mEdges[0];
        right = mEdges[1];
        int lateralWidth = width - (left.getWidth() + right.getWidth());
        back = new GLImage(mBack, lateralWidth, left.getHeight(), false);
    }

    public void setColor(GLColor mColor) {
        color = mColor;
    }

    public void draw(GLColor mColor) {
        if (left == null || right == null || back == null) return;
        left.setColor(color);
        right.setColor(color);
        back.setColor(color);
        if (!visible) return;
        int drawX = xPos;
        int drawY = yPos;
        left.draw(drawX, drawY, color.getAlpha());
        drawX += left.getWidth();
        back.draw(drawX, drawY, color.getAlpha());
        drawX += back.getWidth();
        right.draw(drawX, drawY, color.getAlpha());
    }
}
