package org.jfge.spi.render;

import org.jfge.spi.graphics.Graphics;
import org.jfge.spi.graphics.Image;

/**
 * The Interface ArenaRenderer.
 */
public interface ArenaRenderer {

    public static final int ALIGN_RIGHT = 0;

    public static final int ALIGN_LEFT = 1;

    /**
	 * Draw background.
	 *
	 * @param backGround the back ground
	 */
    public void drawBackground(Graphics graphics, Image backGround, int x, int y);

    public void drawClock(Graphics graphics, int time);

    public void drawLifebar(Graphics graphics, String name, int health, Image portrait, int wins, int align);

    public void drawRound(Graphics graphics, int round);

    public void drawWinner(Graphics graphics, String winner);
}
