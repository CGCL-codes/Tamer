package tools.docrobot.skins;

import org.pushingpixels.substance.api.skin.TwilightSkin;
import tools.docrobot.SkinRobot;

/**
 * Screenshot robot for {@link TwilightSkin}.
 * 
 * @author Kirill Grouchnikov
 */
public class Twilight extends SkinRobot {

    /**
	 * Creates the screenshot robot.
	 */
    public Twilight() {
        super(new TwilightSkin(), "/Users/kirillg/JProjects/substance/www/images/screenshots/skins/twilight");
    }
}
