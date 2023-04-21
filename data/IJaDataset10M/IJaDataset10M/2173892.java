package jepe.font;

import java.awt.*;

/**
 * The superclass of all JEPE's font classes
 * 
 * @author Thomas Weyn
 *
 */
public abstract class AbstractFont {

    /**
	 * Text alignment constants
	 */
    public static final int LEFT = 1, RIGHT = 2, CENTER = 3, JUSTIFY = 4;

    /**
	 * The default letter sequence
	 */
    public static String defaultSequence = " !\"#$%&'()*+,-./0123456789:;<=>?@" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    /**
	 * Writes a one-line text
	 * (well, it wraps if the text exceeds 999999 pixels in width)
	 * 
	 * @param graphics - the graphics context
	 * @param s - the string to write
	 * @param x - the x coordinate to start writing at
	 * @param y - the y coordinate to start writing at
	 */
    public void write(Graphics graphics, String s, int x, int y) {
        this.write(graphics, s, LEFT, x, y, 999999);
    }

    /**
	 * Writes a text of a given width that can be aligned. If the text is
	 * wider than width, the text wraps to the next line
	 * 
	 * @param graphics - the graphics context
	 * @param s - the string to write
	 * @param alignment - text alignment: LEFT, RIGHT, CENTER or JUSTIFY
	 * @param x - the x coordinate to start writing at
	 * @param y - the y coordinate to start writing at
	 * @param width - the maximum width of one line of text
	 */
    public void write(Graphics graphics, String s, int alignment, int x, int y, int width) {
        this.write(graphics, s, alignment, x, y, width, 0, 0);
    }

    /**
	 * Writes a text of a given width, vspace and indentation that can be aligned.
	 * If the text is wider than width, the text wraps to the next line
	 * 
	 * @param graphics - the graphics context
	 * @param s - the string to write
	 * @param alignment - text alignment: LEFT, RIGHT, CENTER or JUSTIFY
	 * @param x - the x coordinate to start writing at
	 * @param y - the y coordinate to start writing at
	 * @param width - the maximum width of one line of text
	 * @param vspace - The extra whitespace to leave between lines.
	 *                 Can be positive or negative.
	 * @param indentation - The amount of pixels to indentate the text.
	 *                      Can be positive or negative.
	 * @return the number of written rows
	 */
    public abstract int write(Graphics graphics, String s, int alignment, int x, int y, int width, int vspace, int indentation);

    /**
     * Returns the width of a given string in pixels
     */
    public int getWidth(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            length += this.getWidth(s.charAt(i));
        }
        return length;
    }

    /**
     * Returns the width of the given character in pixels
     */
    public abstract int getWidth(char c);

    /**
     * Returns the width of the given character in pixels
     */
    public int getWidth(Character c) {
        return this.getWidth(c.charValue());
    }

    /**
     * Returns the current height of the font
     */
    public abstract int getHeight();

    /**
     * Tells us if the given caharcter is available in the font
     */
    public abstract boolean isAvailable(char c);

    /**
     * Tells us if the given caharcter is available in the font
     */
    public boolean isAvailable(Character c) {
        return this.isAvailable(c.charValue());
    }
}
