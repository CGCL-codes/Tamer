package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * Provides miscellaneous text operations. Clients may subclass this class if
 * necessary.
 * 
 * @author crevells
 * @since 3.4
 */
public class TextUtilities {

    /**
     * a singleton default instance
     */
    public static TextUtilities INSTANCE = new TextUtilities();

    /**
     * Returns the Dimensions of <i>s</i> in Font <i>f</i>.
     * 
     * @param s
     *            the string
     * @param f
     *            the font
     * @return the dimensions of the given string
     */
    public Dimension getStringExtents(String s, Font f) {
        return FigureUtilities.getStringExtents(s, f);
    }

    /**
     * Returns the Dimensions of the given text, converting newlines and tabs
     * appropriately.
     * 
     * @param s
     *            the text
     * @param f
     *            the font
     * @return the dimensions of the given text
     */
    public Dimension getTextExtents(String s, Font f) {
        return FigureUtilities.getTextExtents(s, f);
    }

    /**
     * Gets the font's ascent.
     * 
     * @param font
     * @return the font's ascent
     */
    public int getAscent(Font font) {
        FontMetrics fm = FigureUtilities.getFontMetrics(font);
        return fm.getHeight() - fm.getDescent();
    }

    /**
     * Gets the font's descent.
     * 
     * @param font
     * @return the font's descent
     */
    public int getDescent(Font font) {
        return FigureUtilities.getFontMetrics(font).getDescent();
    }

    /**
     * Returns the largest substring of <i>s</i> in Font <i>f</i> that can be
     * confined to the number of pixels in <i>availableWidth<i>.
     * 
     * @param s
     *            the original string
     * @param f
     *            the font
     * @param availableWidth
     *            the available width
     * @return the largest substring that fits in the given width
     */
    public int getLargestSubstringConfinedTo(String s, Font f, int availableWidth) {
        FontMetrics metrics = FigureUtilities.getFontMetrics(f);
        int min, max;
        float avg = metrics.getAverageCharWidth();
        min = 0;
        max = s.length() + 1;
        int guess = 0, guessSize = 0;
        while ((max - min) > 1) {
            guess = guess + (int) ((availableWidth - guessSize) / avg);
            if (guess >= max) guess = max - 1;
            if (guess <= min) guess = min + 1;
            guessSize = getTextExtents(s.substring(0, guess), f).width;
            if (guessSize < availableWidth) min = guess; else max = guess;
        }
        return min;
    }
}
