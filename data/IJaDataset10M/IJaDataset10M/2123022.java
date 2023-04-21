package com.objetdirect.gwt.umlapi.client.gfx;

import java.util.Arrays;
import java.util.List;

/**
 * @author Florian Mounier (mounier-dot-florian.at.gmail'dot'com)
 */
public class GfxColor {

    /**
	 * The aqua color in RGB
	 */
    public static final GfxColor AQUA = new GfxColor(0, 255, 255, 255);

    /**
	 * The balck color in RGB
	 */
    public static final GfxColor BLACK = new GfxColor(0, 0, 0, 255);

    /**
	 * The blue color in RGB
	 */
    public static final GfxColor BLUE = new GfxColor(0, 0, 255, 255);

    /**
	 * The fuchsia color in RGB
	 */
    public static final GfxColor FUCHSIA = new GfxColor(255, 0, 255, 255);

    /**
	 * The gray color in RGB
	 */
    public static final GfxColor GRAY = new GfxColor(128, 128, 128, 255);

    /**
	 * The green color in RGB
	 */
    public static final GfxColor GREEN = new GfxColor(0, 128, 0, 255);

    /**
	 * The lime color in RGB
	 */
    public static final GfxColor LIME = new GfxColor(0, 255, 0, 255);

    /**
	 * The maroon color in RGB
	 */
    public static final GfxColor MAROON = new GfxColor(128, 0, 0, 255);

    /**
	 * The navy color in RGB
	 */
    public static final GfxColor NAVY = new GfxColor(0, 0, 128, 255);

    /**
	 * The olive color in RGB
	 */
    public static final GfxColor OLIVE = new GfxColor(128, 128, 0, 255);

    /**
	 * The purpble color in RGB
	 */
    public static final GfxColor PURPLE = new GfxColor(128, 0, 128, 255);

    /**
	 * The red color in RGB
	 */
    public static final GfxColor RED = new GfxColor(255, 0, 0, 255);

    /**
	 * The silver color inRGB
	 */
    public static final GfxColor SILVER = new GfxColor(192, 192, 192, 255);

    /**
	 * The teal color in RGB
	 */
    public static final GfxColor TEAL = new GfxColor(0, 128, 128, 255);

    /**
	 * The white color in RGB
	 */
    public static final GfxColor WHITE = new GfxColor(255, 255, 255, 255);

    /**
	 * The yellow color in RGB
	 */
    public static final GfxColor YELLOW = new GfxColor(255, 255, 0, 255);

    int a;

    int b;

    int g;

    int r;

    /**
	 * Constructor of a color
	 * 
	 * @param r
	 *            Red value (0-255)
	 * @param g
	 *            Green value (0-255)
	 * @param b
	 *            Blue value (0-255)
	 */
    public GfxColor(final int r, final int g, final int b) {
        this(r, g, b, 255);
    }

    /**
	 * Constructor of a color
	 * 
	 * @param r
	 *            Red value (0-255)
	 * @param g
	 *            Green value (0-255)
	 * @param b
	 *            Blue value (0-255)
	 * @param a
	 *            Alpha (transparency) value (0-255)
	 */
    public GfxColor(final int r, final int g, final int b, final int a) {
        super();
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
	 * Constructor of a color
	 * 
	 * @param hexString
	 *            A standard hex {@link String} for a color (supports : aaa, #bbb, abcd, #abcd, 123456, #123456,
	 *            12345678, #12345678)
	 */
    @SuppressWarnings("fallthrough")
    public GfxColor(final String hexString) {
        super();
        String hexColor;
        if (hexString.startsWith("#")) {
            hexColor = hexString.substring(1);
        } else {
            hexColor = hexString;
        }
        a = 255;
        switch(hexColor.length()) {
            case 4:
                a = Integer.decode("#" + hexColor.substring(3, 4) + hexColor.substring(3, 4));
            case 3:
                r = Integer.decode("#" + hexColor.substring(0, 1) + hexColor.substring(0, 1));
                g = Integer.decode("#" + hexColor.substring(1, 2) + hexColor.substring(1, 2));
                b = Integer.decode("#" + hexColor.substring(2, 3) + hexColor.substring(2, 3));
                break;
            case 8:
                a = Integer.decode("#" + hexColor.substring(6, 8));
            case 6:
                r = Integer.decode("#" + hexColor.substring(0, 2));
                g = Integer.decode("#" + hexColor.substring(2, 4));
                b = Integer.decode("#" + hexColor.substring(4, 6));
        }
    }

    /**
	 * Getter for the Alpha value
	 * 
	 * @return The Alpha value
	 */
    public int getAlpha() {
        return a;
    }

    /**
	 * Getter for the Blue value
	 * 
	 * @return The Blue value
	 */
    public int getBlue() {
        return b;
    }

    /**
	 * Getter for the Green value
	 * 
	 * @return The Green value
	 */
    public int getGreen() {
        return g;
    }

    /**
	 * Getter for the Red value
	 * 
	 * @return The Red value
	 */
    public int getRed() {
        return r;
    }

    /**
	 * Setter for the Alpha value
	 * 
	 * @param a
	 *            The Alpha value
	 */
    public void setAlpha(final int a) {
        this.a = a;
    }

    /**
	 * Setter for the Blue value
	 * 
	 * @param b
	 *            The Blue value
	 */
    public void setBlue(final int b) {
        this.b = b;
    }

    /**
	 * Setter for the Green value
	 * 
	 * @param g
	 *            The Green value
	 */
    public void setGreen(final int g) {
        this.g = g;
    }

    /**
	 * Setter for the Red value
	 * 
	 * @param r
	 *            The Red value
	 */
    public void setRed(final int r) {
        this.r = r;
    }

    /**
	 * Convert r g b values in h s v values and returns it as a list of {@link Integer}
	 * 
	 * @return The hsv Integers {@link List}
	 */
    public List<Integer> toHSV() {
        double h = 0;
        double s = 0;
        double v = 0;
        final double red = (r / 255);
        final double green = (g / 255);
        final double blue = (b / 255);
        final double min = Math.min(Math.min(red, green), blue);
        final double max = Math.max(Math.max(red, green), blue);
        final double delta = max - min;
        v = max;
        if (delta == 0) {
            h = 0;
            s = 0;
        } else {
            s = delta / max;
            final double deltaRed = (((max - red) / 6) + (delta / 2)) / delta;
            final double deltaGreen = (((max - green) / 6) + (delta / 2)) / delta;
            final double deltaBlue = (((max - blue) / 6) + (delta / 2)) / delta;
            if (red == max) {
                h = deltaBlue - deltaGreen;
            } else if (green == max) {
                h = (1 / 3) + deltaRed - deltaBlue;
            } else if (blue == max) {
                h = (2 / 3) + deltaGreen - deltaRed;
            }
            if (h < 0) {
                h += 1;
            }
            if (h > 1) {
                h -= 1;
            }
        }
        return Arrays.asList((int) Math.round(h * 360), (int) Math.round(s * 100), (int) Math.round(v * 100));
    }

    @Override
    public String toString() {
        return "#" + (r < 15 ? "0" : "") + Integer.toHexString(r) + (g < 15 ? "0" : "") + Integer.toHexString(g) + (b < 15 ? "0" : "") + Integer.toHexString(b);
    }
}
