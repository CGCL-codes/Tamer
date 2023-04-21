package tools.docrobot.watermarks;

import org.pushingpixels.substance.api.watermark.SubstanceStripeWatermark;
import tools.docrobot.WatermarkRobot;

/**
 * Screenshot robot for {@link SubstanceStripeWatermark}.
 * 
 * @author Kirill Grouchnikov
 */
public class StripesWatermark extends WatermarkRobot {

    /**
	 * Creates the screenshot robot.
	 */
    public StripesWatermark() {
        super(new SubstanceStripeWatermark(), "/Users/kirillg/JProjects/substance/www/images/screenshots/watermarks/stripes.png");
    }
}
