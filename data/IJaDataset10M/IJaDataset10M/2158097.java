package org.redpin.screen;

import javax.microedition.lcdui.Graphics;

/**
 * Explicit Refinement Indicator.
 * 
 * @author Simon Tobler (simon.p.tobler@gmx.ch)
 * @author Davide Spena (davide.spena@gmail.com)
 * @version 0.1
 */
public class RefinementIndicator extends Indicator {

    protected static final int DARKER_BLUE = 0x000099;

    protected static final int BLUE = 0x0000ff;

    private static final int CROSSHAIR_HALF = 10;

    /**
	 * {@inheritDoc}
	 */
    public void draw(Graphics g, int x, int y) {
        this.drawCrosshair(g, x, y);
    }

    /**
	 * Draws a blue crosshair used when scrolling on the map
	 */
    private void drawCrosshair(Graphics g, int x, int y) {
        g.setColor(RefinementIndicator.BLUE);
        g.setStrokeStyle(Graphics.SOLID);
        g.drawLine(x - RefinementIndicator.CROSSHAIR_HALF, y, x + RefinementIndicator.CROSSHAIR_HALF, y);
        g.drawLine(x, y - RefinementIndicator.CROSSHAIR_HALF, x, y + RefinementIndicator.CROSSHAIR_HALF);
        int flagLength = 1;
        g.drawLine(x - RefinementIndicator.CROSSHAIR_HALF, y + 1, x - RefinementIndicator.CROSSHAIR_HALF + flagLength, y + 1);
        g.drawLine(x - RefinementIndicator.CROSSHAIR_HALF, y - 1, x - RefinementIndicator.CROSSHAIR_HALF + flagLength, y - 1);
        g.drawLine(x + RefinementIndicator.CROSSHAIR_HALF, y + 1, x + RefinementIndicator.CROSSHAIR_HALF - flagLength, y + 1);
        g.drawLine(x + RefinementIndicator.CROSSHAIR_HALF, y - 1, x + RefinementIndicator.CROSSHAIR_HALF - flagLength, y - 1);
        g.drawLine(x - 1, y + RefinementIndicator.CROSSHAIR_HALF, x - 1, y + RefinementIndicator.CROSSHAIR_HALF - flagLength);
        g.drawLine(x + 1, y + RefinementIndicator.CROSSHAIR_HALF, x + 1, y + RefinementIndicator.CROSSHAIR_HALF - flagLength);
        g.drawLine(x - 1, y - RefinementIndicator.CROSSHAIR_HALF, x - 1, y - RefinementIndicator.CROSSHAIR_HALF + flagLength);
        g.drawLine(x + 1, y - RefinementIndicator.CROSSHAIR_HALF, x + 1, y - RefinementIndicator.CROSSHAIR_HALF + flagLength);
    }
}
