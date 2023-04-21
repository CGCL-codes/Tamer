package org.matsim.core.utils.charts;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.matsim.core.utils.charts.Demo;
import org.matsim.core.utils.charts.LineChart;
import org.matsim.testcases.MatsimTestCase;

/**
 * Test for {@link LineChart}
 *
 * @author mrieser
 */
public class LineChartTest extends MatsimTestCase {

    /**
	 * Test that a file was really generated, and that the image, when loaded, has the specified size.
	 * @throws IOException possible exception when reading the image for validation
	 */
    public void testLineChartDemo() throws IOException {
        String imageFilename = getOutputDirectory() + "linechart.png";
        Demo demo = new Demo();
        demo.createLineChart(imageFilename);
        File imagefile = new File(imageFilename);
        assertTrue(imagefile.exists());
        BufferedImage image = ImageIO.read(imagefile);
        assertEquals(800, image.getWidth(null));
        assertEquals(600, image.getHeight(null));
    }
}
