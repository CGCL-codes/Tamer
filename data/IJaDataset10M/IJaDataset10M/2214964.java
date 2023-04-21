package tools.docrobot.schemes;

import org.pushingpixels.substance.api.colorscheme.CremeColorScheme;
import tools.docrobot.ColorSchemeRobot;

/**
 * Screenshot robot for the {@link CremeColorScheme}.
 * 
 * @author Kirill Grouchnikov
 */
public class CremeScheme extends ColorSchemeRobot {

    /**
	 * Creates the screenshot robot.
	 */
    public CremeScheme() {
        super(new CremeColorScheme(), "/Users/kirillg/JProjects/substance/www/images/screenshots/colorschemes/creme.png");
    }
}
