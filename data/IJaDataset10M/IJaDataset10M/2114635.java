package org.jfree.workbook;

/**
 * A style modifier that changes the foreground, background or pattern color for a style, but leaves
 * all other settings unchanged.
 */
public class ColorStyleModifier implements StyleModifier {

    /** Foreground, background or pattern color. */
    protected int which;

    /** The new color. */
    protected Color color;

    /**
     * Standard constructor.
     * 
     * @param which  indicates which color to change (use constants BACKGROUND_COLOR,
     *               FOREGROUND_COLOR and PATTERN_COLOR in the StyleColor class).
     * @param color  the new color.
     */
    public ColorStyleModifier(int which, Color color) {
        this.which = which;
        this.color = color;
    }

    /**
     * Returns a new style with the same settings as the style passed in, except with a different
     * foreground, background or pattern color.
     * 
     * @param style  the style to be modified.
     * 
     * @return A new style with the same settings as the style passed in, except with a different
     *         foreground, background or pattern color.
     */
    public Style getModifiedStyle(Style style) {
        switch(which) {
            case Color.BACKGROUND_COLOR:
                return Style.applyBackgroundColor(style, color);
            case Color.FOREGROUND_COLOR:
                return Style.applyForegroundColor(style, color);
            case Color.PATTERN_COLOR:
                return Style.applyPatternColor(style, color);
            default:
                return null;
        }
    }
}
