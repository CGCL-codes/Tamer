package de.enough.polish.ui.borders;

import de.enough.polish.ui.Border;
import de.enough.polish.android.lcdui.Graphics;

/**
 * <p>Paints a border which is like shadow, which is seen on the bottom and on the right of the bordered Item.</p>
 *
 * <p>Copyright Enough Software 2004 - 2009</p>
 * @author Robert Virkus, robert@enough.de
 */
public class BottomRightShadowBorder extends Border {

    private final int color;

    private final int offset;

    /**
	 * Creates a new border which is like a shadow which is seen on the bottom and on the right of the Item.
	 * 
	 * @param color the color of this border in RGB, e.g. 0xFFDD12
	 * @param borderWidth the width of this border
	 * @param offset the offset of the shadow
	 */
    public BottomRightShadowBorder(int color, int borderWidth, int offset) {
        super(0, borderWidth, 0, borderWidth);
        this.color = color;
        this.offset = offset;
    }

    public void paint(int x, int y, int width, int height, Graphics g) {
        g.setColor(this.color);
        int bottom = y + height;
        int right = x + width;
        int xOffset = x + this.offset;
        int yOffset = y + this.offset;
        g.drawLine(xOffset, bottom, right, bottom);
        g.drawLine(right, yOffset, right, bottom);
        if (this.borderWidthLeft > 1) {
            int border = this.borderWidthLeft - 1;
            while (border > 0) {
                g.drawLine(xOffset, bottom - border, right, bottom - border);
                g.drawLine(right - border, yOffset, right - border, bottom);
                border--;
            }
        }
    }
}
