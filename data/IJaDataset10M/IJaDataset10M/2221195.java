package org.jbubblebreaker.bubbles;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Provides the 3D Rect Bubbles
 * @author Sven Strickroth
 */
@SuppressWarnings("serial")
public class Bubble3DRect extends BubbleDefault {

    /**
	 * Stores the name of this bubble set
	 */
    public static String name = "3DRect";

    /**
	 * Create a Bubble on a specific position and size with a random color
	 * @param radius radius of Bubble
	 * @param row Row of Bubble
	 * @param col Column of Bubble
	 */
    public Bubble3DRect(int radius, int row, int col) {
        super(radius, row, col);
    }

    /**
	 * Create a Bubble on a specific position, size and color
	 * @param radius radius of Bubble
	 * @param row Row of Bubble
	 * @param col Column of Bubble
	 * @param colorIndex color index for new Bubble, if this colorIndex is not valid a random color is used
	 */
    public Bubble3DRect(int radius, int row, int col, int colorIndex) {
        super(radius, row, col, colorIndex);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(getColor(getColorIndex()));
        g.fill3DRect(0, 0, radius, radius, true);
        if (isMarked() == true) {
            g.setColor(Color.GRAY);
            g.fillOval(radius / 4, radius / 4, radius / 2, radius / 2);
        }
    }
}
