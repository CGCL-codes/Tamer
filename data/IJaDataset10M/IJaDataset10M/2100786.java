package com.trollworks.ttk.utility;

import java.awt.Color;

/** Provides standardized color access. */
public class Colors {

    /**
	 * @param primary Whether to return the primary or secondary banding color.
	 * @return The color.
	 */
    public static Color getBanding(boolean primary) {
        return primary ? Color.WHITE : new Color(232, 255, 232);
    }

    /**
	 * @param color The color being tested.
	 * @param threshold The percent of threshold (0 to 100).
	 * @return <code>true</code> if the specified color is above the threshold. For example, if you
	 *         pass in a dark color with a threshold of 50, it will return <code>false</code>
	 *         because black is 0.
	 */
    public static boolean threshold(Color color, int threshold) {
        if (threshold < 0 || threshold > 100) {
            return false;
        }
        return (int) ((color.getRed() + color.getGreen() + color.getBlue()) / 7.65) > threshold;
    }

    /**
	 * @param color1 The first color.
	 * @param color2 The second color.
	 * @param percentage How much of the second color to use.
	 * @return A color that is a blended version of the two passed in.
	 */
    public static final Color blend(Color color1, Color color2, int percentage) {
        int remaining = 100 - percentage;
        return new Color((color1.getRed() * remaining + color2.getRed() * percentage) / 100, (color1.getGreen() * remaining + color2.getGreen() * percentage) / 100, (color1.getBlue() * remaining + color2.getBlue() * percentage) / 100);
    }

    /**
	 * @param color Return an intensified version of this color.
	 * @return A color that is brighter than the color passed in.
	 */
    public static final Color brighten(Color color) {
        if (threshold(color, 50)) {
            return darker(color, 65).brighter();
        }
        return color;
    }

    /**
	 * @param color Return a darker version of this color.
	 * @param percentage How much darker.
	 * @return A color that is darker than the color passed in by the given percentage.
	 */
    public static final Color darker(Color color, int percentage) {
        return blend(color, Color.black, percentage);
    }

    /**
	 * @param color Return a lighter version of this color.
	 * @param percentage How much lighter.
	 * @return A color that is lighter than the color passed in by the given percentage.
	 */
    public static final Color lighter(Color color, int percentage) {
        return blend(color, Color.white, percentage);
    }
}
