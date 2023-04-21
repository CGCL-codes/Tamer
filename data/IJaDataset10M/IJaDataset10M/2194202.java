package mujmail.html;

import javax.microedition.lcdui.Graphics;

/**
 * <p>Marks class as drawable - we can draw it.</p>
 * <p>
 * This could be confusing for example for br tag, but think about it a little.
 * br tag also needs to be drawn, at least coordinates have to be moved
 * to the next line.
 * </p>
 * 
 * @author Betlista
 */
public interface Drawable {

    /**
	 * <p>Enables to draw object that implements this interface.</p>
	 * <p>
	 * The returned position should be the position where the next element
	 * (correctly Drawable) can be drawn. We are using positioning to the upper
	 * left corner of drawn rectangle.
	 * </p>
	 * 
	 * @param g graphics reference to be used for drawing
	 * @param x horizontal coordinate
	 * @param y vertical coordinate
	 * @return new position where to start drawing of next {@link Drawable}
	 */
    public Point draw(final Graphics g, int x, int y);

    /**
	 * Implements crate design pattern simply to enable returning
	 * the new position in {@link Drawable#draw(Graphics, int, int)} method.
	 * 
	 * @author Betlista
	 */
    public class Point {

        /** Represents horizontal coordinate. */
        public int x;

        /** Represents vertical coordinate. */
        public int y;

        /**
		 * Constructor to build complete (in meaning of set fields) instance.
		 * 
		 * @param x horizontal coordinate (see {@link #x})
		 * @param y vertical coordinate (see {@link #y})
		 */
        public Point(final int x, final int y) {
            this.x = x;
            this.y = y;
        }
    }
}
