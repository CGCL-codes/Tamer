package tools.docrobot.schemes;

import org.pushingpixels.substance.api.colorscheme.UltramarineColorScheme;
import tools.docrobot.ColorSchemeRobot;

/**
 * Screenshot robot for the {@link UltramarineColorScheme}.
 * 
 * @author Kirill Grouchnikov
 */
public class UltramarineScheme extends ColorSchemeRobot {

    /**
	 * Creates the screenshot robot.
	 */
    public UltramarineScheme() {
        super(new UltramarineColorScheme(), "/Users/kirillg/JProjects/substance/www/images/screenshots/colorschemes/ultramarine.png");
    }
}
