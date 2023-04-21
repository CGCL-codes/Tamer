package org.pushingpixels.substance.api.skin;

import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceColorSchemeBundle;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.colorscheme.LightGrayColorScheme;
import org.pushingpixels.substance.api.colorscheme.MetallicColorScheme;
import org.pushingpixels.substance.api.colorscheme.SteelBlueColorScheme;
import org.pushingpixels.substance.api.painter.border.ClassicBorderPainter;
import org.pushingpixels.substance.api.painter.decoration.ClassicDecorationPainter;
import org.pushingpixels.substance.api.painter.fill.GlassFillPainter;
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter;
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper;

/**
 * <code>Moderate</code> skin. This class is part of officially supported API.
 * 
 * @author Kirill Grouchnikov
 * @since version 3.1
 */
public class ModerateSkin extends SubstanceSkin {

    /**
	 * Display name for <code>this</code> skin.
	 */
    public static final String NAME = "Moderate";

    /**
	 * Creates a new <code>Moderate</code> skin.
	 */
    public ModerateSkin() {
        SubstanceColorScheme activeScheme = new SteelBlueColorScheme();
        SubstanceColorScheme enabledScheme = new MetallicColorScheme();
        SubstanceColorScheme disabledScheme = new LightGrayColorScheme();
        SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, enabledScheme, disabledScheme);
        SubstanceSkin.ColorSchemes kitchenSinkSchemes = SubstanceSkin.getColorSchemes("org/pushingpixels/substance/api/skin/kitchen-sink.colorschemes");
        SubstanceColorScheme highlightColorScheme = kitchenSinkSchemes.get("Moderate Highlight");
        defaultSchemeBundle.registerHighlightColorScheme(highlightColorScheme);
        this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
        this.registerAsDecorationArea(activeScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE);
        this.registerAsDecorationArea(kitchenSinkSchemes.get("LightGray General Watermark"), DecorationAreaType.GENERAL, DecorationAreaType.HEADER);
        this.buttonShaper = new ClassicButtonShaper();
        this.fillPainter = new GlassFillPainter();
        this.decorationPainter = new ClassicDecorationPainter();
        this.borderPainter = new ClassicBorderPainter();
        this.highlightPainter = new ClassicHighlightPainter();
    }

    public String getDisplayName() {
        return NAME;
    }
}
