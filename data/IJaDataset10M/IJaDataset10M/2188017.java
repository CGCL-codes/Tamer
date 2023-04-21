package tools.docrobot.schemes;

import org.pushingpixels.substance.api.colorscheme.OrangeColorScheme;
import tools.docrobot.ColorSchemeRobot;

/**
 * Screenshot robot for the {@link OrangeColorScheme}.
 * 
 * @author Kirill Grouchnikov
 */
public class OrangeScheme extends ColorSchemeRobot {

    /**
	 * Creates the screenshot robot.
	 */
    public OrangeScheme() {
        super(new OrangeColorScheme(), "/Users/kirillg/JProjects/substance/www/images/screenshots/colorschemes/orange.png");
    }
}
