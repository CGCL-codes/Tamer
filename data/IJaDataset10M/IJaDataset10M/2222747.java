package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;

/**
 * <b>Steel Blue</b> color scheme.
 * 
 * @author Kirill Grouchnikov
 */
public class SteelBlueColorScheme extends BaseLightColorScheme {

    /**
	 * The main ultra-light color.
	 */
    private static final Color mainUltraLightColor = new Color(149, 193, 219);

    /**
	 * The main extra-light color.
	 */
    private static final Color mainExtraLightColor = new Color(130, 181, 212);

    /**
	 * The main light color.
	 */
    private static final Color mainLightColor = new Color(118, 165, 195);

    /**
	 * The main medium color.
	 */
    private static final Color mainMidColor = new Color(108, 149, 178);

    /**
	 * The main dark color.
	 */
    private static final Color mainDarkColor = new Color(38, 79, 111);

    /**
	 * The main ultra-dark color.
	 */
    private static final Color mainUltraDarkColor = new Color(47, 75, 99);

    /**
	 * The foreground color.
	 */
    private static final Color foregroundColor = Color.black;

    /**
	 * Creates a new <code>Steel Blue</code> color scheme.
	 */
    public SteelBlueColorScheme() {
        super("Steel Blue");
    }

    public Color getForegroundColor() {
        return SteelBlueColorScheme.foregroundColor;
    }

    public Color getUltraLightColor() {
        return SteelBlueColorScheme.mainUltraLightColor;
    }

    public Color getExtraLightColor() {
        return SteelBlueColorScheme.mainExtraLightColor;
    }

    public Color getLightColor() {
        return SteelBlueColorScheme.mainLightColor;
    }

    public Color getMidColor() {
        return SteelBlueColorScheme.mainMidColor;
    }

    public Color getDarkColor() {
        return SteelBlueColorScheme.mainDarkColor;
    }

    public Color getUltraDarkColor() {
        return SteelBlueColorScheme.mainUltraDarkColor;
    }
}
