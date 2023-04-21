package com.nullfish.lib.ui.grid;

import java.awt.Graphics;

/**
 * @author shunji
 * 
 */
public class CrossLineGrid extends LineGrid {

    public boolean showUp;

    public boolean showRight;

    public boolean showDown;

    public boolean showLeft;

    public CrossLineGrid() {
    }

    public CrossLineGrid(Object groupKey) {
        super(groupKey);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        if (doubleLine) {
            if (showUp) {
                g.drawLine(centerX - 2, 0, centerX - 2, centerY - 2);
                g.drawLine(centerX + 2, 0, centerX + 2, centerY - 2);
            } else {
                g.drawLine(centerX - 2, centerY - 2, centerX + 2, centerY - 2);
            }
            if (showDown) {
                g.drawLine(centerX - 2, centerY + 2, centerX - 2, getHeight());
                g.drawLine(centerX + 2, centerY + 2, centerX + 2, getHeight());
            } else {
                g.drawLine(centerX - 2, centerY + 2, centerX + 2, centerY + 2);
            }
            if (showLeft) {
                g.drawLine(0, centerY - 2, centerX - 2, centerY - 2);
                g.drawLine(0, centerY + 2, centerX - 2, centerY + 2);
            } else {
                g.drawLine(centerX - 2, centerY - 2, centerX - 2, centerY + 2);
            }
            if (showRight) {
                g.drawLine(getWidth(), centerY - 2, centerX + 2, centerY - 2);
                g.drawLine(getWidth(), centerY + 2, centerX + 2, centerY + 2);
            } else {
                g.drawLine(centerX + 2, centerY - 2, centerX + 2, centerY + 2);
            }
        } else {
            if (showUp) {
                g.drawLine(centerX, 0, centerX, centerY);
            }
            if (showDown) {
                g.drawLine(centerX, centerY, centerX, getHeight());
            }
            if (showLeft) {
                g.drawLine(0, centerY, centerX, centerY);
            }
            if (showRight) {
                g.drawLine(getWidth(), centerY, centerX, centerY);
            }
        }
    }

    /**
	 * @param showDown
	 *            The showDown to set.
	 */
    public void setShowDown(boolean showDown) {
        this.showDown = showDown;
    }

    /**
	 * @param showLeft
	 *            The showLeft to set.
	 */
    public void setShowLeft(boolean showLeft) {
        this.showLeft = showLeft;
    }

    /**
	 * @param showRight
	 *            The showRight to set.
	 */
    public void setShowRight(boolean showRight) {
        this.showRight = showRight;
    }

    /**
	 * @param showUp
	 *            The showUp to set.
	 */
    public void setShowUp(boolean showUp) {
        this.showUp = showUp;
    }

    public void setDirections(boolean up, boolean right, boolean down, boolean left) {
        showUp = up;
        showRight = right;
        showDown = down;
        showLeft = left;
    }
}
