package tools.docrobot.schemes;

import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.colorscheme.PurpleColorScheme;
import tools.docrobot.ColorSchemeRobot;

/**
 * Screenshot robot for the {@link SubstanceColorScheme#invert()}.
 * 
 * @author Kirill Grouchnikov
 */
public class DerivedInvertedScheme extends ColorSchemeRobot {

    /**
	 * Creates the screenshot robot.
	 */
    public DerivedInvertedScheme() {
        super(new PurpleColorScheme().invert(), "/Users/kirillg/JProjects/substance/www/images/screenshots/colorschemes/derived-invert.png");
    }
}
