package org.pushingpixels.substance.internal.colorscheme;

import java.awt.Color;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.colorscheme.BaseColorScheme;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

/**
 * Implementation of negated color scheme. Negated color scheme is based on some
 * original color scheme, negating all the colors.
 * 
 * @author Kirill Grouchnikov
 */
public class NegatedColorScheme extends BaseColorScheme {

    /**
	 * The main ultra-light color.
	 */
    private Color mainUltraLightColor;

    /**
	 * The main extra-light color.
	 */
    private Color mainExtraLightColor;

    /**
	 * The main light color.
	 */
    private Color mainLightColor;

    /**
	 * The main medium color.
	 */
    private Color mainMidColor;

    /**
	 * The main dark color.
	 */
    private Color mainDarkColor;

    /**
	 * The main ultra-dark color.
	 */
    private Color mainUltraDarkColor;

    /**
	 * The foreground color.
	 */
    private Color foregroundColor;

    /**
	 * The original color scheme.
	 */
    private SubstanceColorScheme origScheme;

    /**
	 * Creates a new inverted scheme.
	 * 
	 * @param origScheme
	 *            The original color scheme.
	 */
    public NegatedColorScheme(SubstanceColorScheme origScheme) {
        super("Negated " + origScheme.getDisplayName(), !origScheme.isDark());
        this.origScheme = origScheme;
        this.foregroundColor = SubstanceColorUtilities.invertColor(origScheme.getForegroundColor());
        this.mainUltraDarkColor = SubstanceColorUtilities.invertColor(origScheme.getUltraDarkColor());
        this.mainDarkColor = SubstanceColorUtilities.invertColor(origScheme.getDarkColor());
        this.mainMidColor = SubstanceColorUtilities.invertColor(origScheme.getMidColor());
        this.mainLightColor = SubstanceColorUtilities.invertColor(origScheme.getLightColor());
        this.mainExtraLightColor = SubstanceColorUtilities.invertColor(origScheme.getExtraLightColor());
        this.mainUltraLightColor = SubstanceColorUtilities.invertColor(origScheme.getUltraLightColor());
    }

    public Color getForegroundColor() {
        return this.foregroundColor;
    }

    public Color getUltraLightColor() {
        return this.mainUltraLightColor;
    }

    public Color getExtraLightColor() {
        return this.mainExtraLightColor;
    }

    public Color getLightColor() {
        return this.mainLightColor;
    }

    public Color getMidColor() {
        return this.mainMidColor;
    }

    public Color getDarkColor() {
        return this.mainDarkColor;
    }

    public Color getUltraDarkColor() {
        return this.mainUltraDarkColor;
    }

    /**
	 * Returns the original color scheme.
	 * 
	 * @return The original color scheme.
	 */
    public SubstanceColorScheme getOrigScheme() {
        return this.origScheme;
    }
}
