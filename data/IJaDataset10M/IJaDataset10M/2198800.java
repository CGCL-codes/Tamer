package tools.docrobot.watermarks;

import org.pushingpixels.substance.api.watermark.SubstanceNullWatermark;
import tools.docrobot.WatermarkRobot;

/**
 * Screenshot robot for {@link SubstanceNullWatermark}.
 * 
 * @author Kirill Grouchnikov
 */
public class NullWatermark extends WatermarkRobot {

    /**
	 * Creates the screenshot robot.
	 */
    public NullWatermark() {
        super(new SubstanceNullWatermark(), "/Users/kirillg/JProjects/substance/www/images/screenshots/watermarks/null.png");
    }
}
