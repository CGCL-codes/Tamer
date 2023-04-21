package org.pushingpixels.substance.api.colorscheme;

import java.awt.Color;

/**
 * <b>Ultramarine</b> color scheme.
 * 
 * @author Kirill Grouchnikov
 */
public class UltramarineColorScheme extends BaseDarkColorScheme {

    /**
	 * The main ultra-light color.
	 */
    private static final Color mainUltraLightColor = new Color(46, 22, 124);

    /**
	 * The main extra-light color.
	 */
    private static final Color mainExtraLightColor = new Color(33, 19, 113);

    /**
	 * The main light color.
	 */
    private static final Color mainLightColor = new Color(31, 17, 104);

    /**
	 * The main medium color.
	 */
    private static final Color mainMidColor = new Color(47, 6, 53);

    /**
	 * The main dark color.
	 */
    private static final Color mainDarkColor = new Color(11, 1, 38);

    /**
	 * The main ultra-dark color.
	 */
    private static final Color mainUltraDarkColor = new Color(2, 1, 23);

    /**
	 * The foreground color.
	 */
    private static final Color foregroundColor = Color.white;

    /**
	 * Creates a new <code>Ultramarine</code> color scheme.
	 */
    public UltramarineColorScheme() {
        super("Ultramarine");
    }

    public Color getForegroundColor() {
        return UltramarineColorScheme.foregroundColor;
    }

    public Color getUltraLightColor() {
        return UltramarineColorScheme.mainUltraLightColor;
    }

    public Color getExtraLightColor() {
        return UltramarineColorScheme.mainExtraLightColor;
    }

    public Color getLightColor() {
        return UltramarineColorScheme.mainLightColor;
    }

    public Color getMidColor() {
        return UltramarineColorScheme.mainMidColor;
    }

    public Color getDarkColor() {
        return UltramarineColorScheme.mainDarkColor;
    }

    public Color getUltraDarkColor() {
        return UltramarineColorScheme.mainUltraDarkColor;
    }
}
