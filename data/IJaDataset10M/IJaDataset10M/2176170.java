package org.vizzini.example.kenken.ui;

import java.awt.Color;

/**
 * Provides the cell color palette for KenKen. X11 Light colors are from <a
 * href="http://en.wikipedia.org/wiki/Web_colors">here</a>.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public class ColorPalette {

    /** Red. */
    public static final Color RED = createColor("#FFCECE");

    /** Yellow. */
    public static final Color YELLOW = createColor("#F7F9D0");

    /** Blue. */
    public static final Color BLUE = createColor("#CEDEF4");

    /** Green. */
    public static final Color GREEN = createColor("#D6F8DE");

    /** Orange. */
    public static final Color ORANGE = new Color(255, 225, 128);

    /** Purple. */
    public static final Color PURPLE = createColor("#E1CAF9");

    /** Cyan. */
    public static final Color CYAN = createColor("#C0F7FE");

    /** Lime. */
    public static final Color LIME = new Color(202, 255, 127);

    /** Wheat. */
    public static final Color WHEAT = new Color(245, 222, 179);

    /** White. */
    public static final Color WHITE = Color.WHITE;

    /** X11 light coral. */
    public static final Color X11_LIGHT_CORAL = new Color(240, 128, 128);

    /** X11 light salmon. */
    public static final Color X11_LIGHT_SALMON = new Color(255, 160, 122);

    /** X11 light pink. */
    public static final Color X11_LIGHT_PINK = new Color(255, 182, 193);

    /** X11 light yellow. */
    public static final Color X11_LIGHT_YELLOW = new Color(255, 255, 224);

    /** X11 light goldenrod yellow. */
    public static final Color X11_LIGHT_GOLDENROD_YELLOW = new Color(250, 250, 210);

    /** X11 light green. */
    public static final Color X11_LIGHT_GREEN = new Color(144, 238, 144);

    /** X11 light sea green. */
    public static final Color X11_LIGHT_SEA_GREEN = new Color(32, 178, 170);

    /** X11 light cyan. */
    public static final Color X11_LIGHT_CYAN = new Color(224, 255, 255);

    /** X11 light steel blue. */
    public static final Color X11_LIGHT_STEEL_BLUE = new Color(176, 196, 222);

    /** X11 light blue. */
    public static final Color X11_LIGHT_BLUE = new Color(173, 216, 230);

    /** X11 light sky blue. */
    public static final Color X11_LIGHT_SKY_BLUE = new Color(135, 206, 250);

    /** X11 light grey. */
    public static final Color X11_LIGHT_GREY = new Color(211, 211, 211);

    /** X11 light slate gray. */
    public static final Color X11_LIGHT_SLATE_GRAY = new Color(119, 136, 153);

    /** Ordered colors. */
    public static final Color[] COLORS = { RED, YELLOW, BLUE, GREEN, ORANGE, PURPLE, CYAN, LIME, WHEAT, WHITE, X11_LIGHT_CORAL, X11_LIGHT_SALMON, X11_LIGHT_GREEN, X11_LIGHT_SEA_GREEN, X11_LIGHT_CYAN, X11_LIGHT_STEEL_BLUE, X11_LIGHT_BLUE, X11_LIGHT_SKY_BLUE, X11_LIGHT_GREY, X11_LIGHT_SLATE_GRAY };

    /** Ordered color names. */
    public static final String[] COLOR_NAMES = { "RED", "YELLOW", "BLUE", "GREEN", "ORANGE", "PURPLE", "CYAN", "LIME", "WHEAT", "WHITE", "LIGHT_CORAL", "LIGHT_SALMON", "LIGHT_GREEN", "LIGHT_SEA_GREEN", "LIGHT_CYAN", "LIGHT_STEEL_BLUE", "LIGHT_BLUE", "LIGHT_SKY_BLUE", "LIGHT_GREY", "LIGHT_SLATE_GRAY" };

    /**
     * @param   hexColorString  Hexadecimal color string.
     *
     * @return  a new color using the given parameter.
     *
     * @since   v0.4
     */
    private static final Color createColor(String hexColorString) {
        int red = parseColorComponent(hexColorString, 1, 3);
        int green = parseColorComponent(hexColorString, 3, 5);
        int blue = parseColorComponent(hexColorString, 5, 7);
        return new Color(red, green, blue);
    }

    /**
     * @param   hexColorString  Hexadecimal color string.
     * @param   beginIndex      Begin index.
     * @param   endIndex        End index.
     *
     * @return  a color component from the given parameters.
     *
     * @since   v0.4
     */
    private static final int parseColorComponent(String hexColorString, int beginIndex, int endIndex) {
        String hex = hexColorString.substring(beginIndex, endIndex);
        return Integer.parseInt(hex, 16);
    }
}
