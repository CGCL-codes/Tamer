package de.enough.polish.ui.backgrounds;

import de.enough.polish.android.lcdui.Graphics;
import de.enough.polish.ui.Background;
import de.enough.polish.ui.Color;
import de.enough.polish.ui.Style;

/**
 * <p>Paints a background with rounded and straight corners CSS-type is "round-rect-cornered"</p>
 *
 * <p>Copyright (c) Enough Software 2008</p>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class RoundRectCorneredBackground extends Background {

    /**
	 * Constant for using rounded corners everywhere.
	 */
    public static final int CORNER_NONE = 0;

    /**
	 * Constant for using a straight corner at the top left.
	 */
    public static final int CORNER_LEFT_TOP = 1;

    /**
	 * Constant for using a straight corner at the top right.
	 */
    public static final int CORNER_RIGHT_TOP = 2;

    /**
	 * Constant for using a straight corner at the bottom left.
	 */
    public static final int CORNER_LEFT_BOTTOM = 4;

    /**
	 * Constant for using a straight corner at the bottom right.
	 */
    public static final int CORNER_RIGHT_BOTTOM = 8;

    /**
	 * Constant for using straight corners everywhere.
	 */
    public static final int CORNER_ALL = CORNER_LEFT_TOP | CORNER_RIGHT_TOP | CORNER_LEFT_BOTTOM | CORNER_RIGHT_BOTTOM;

    private int color;

    private int arcWidth;

    private int arcHeight;

    private final boolean leftTop;

    private final boolean rightTop;

    private final boolean leftBottom;

    private final boolean rightBottom;

    /**
	 * Creates a new round tab background.
	 * 
	 * @param color the color of the background
	 * @param arcWidth the horizontal diameter of the arc at the top corners
	 * @param arcHeight the vertical diameter of the arc at the top corners
	 * @param straightCorners a combination of CORNER constants that define the straight corners
	 * @see #CORNER_NONE
	 * @see #CORNER_LEFT_TOP
	 * @see #CORNER_RIGHT_TOP
	 * @see #CORNER_LEFT_BOTTOM
	 * @see #CORNER_RIGHT_BOTTOM
	 */
    public RoundRectCorneredBackground(int color, int arcWidth, int arcHeight, int straightCorners) {
        super();
        this.color = color;
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.leftTop = (straightCorners & CORNER_LEFT_TOP) == CORNER_LEFT_TOP;
        this.rightTop = (straightCorners & CORNER_RIGHT_TOP) == CORNER_RIGHT_TOP;
        this.leftBottom = (straightCorners & CORNER_LEFT_BOTTOM) == CORNER_LEFT_BOTTOM;
        this.rightBottom = (straightCorners & CORNER_RIGHT_BOTTOM) == CORNER_RIGHT_BOTTOM;
    }

    public void paint(int x, int y, int width, int height, Graphics g) {
        g.setColor(this.color);
        int aw = this.arcWidth;
        int ah = this.arcHeight;
        g.fillRoundRect(x, y, width, height, aw, ah);
        if (this.leftTop) {
            g.fillRect(x, y, aw, ah);
        }
        if (this.rightTop) {
            g.fillRect(x + width - aw, y, aw, ah);
        }
        if (this.leftBottom) {
            g.fillRect(x, y + height - ah, aw, ah);
        }
        if (this.rightBottom) {
            g.fillRect(x + width - aw, y + height - ah, aw, ah);
        }
    }
}
