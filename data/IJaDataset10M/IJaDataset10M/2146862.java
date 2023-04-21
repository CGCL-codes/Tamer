package com.jachsoft.imagelib.algorithms;

import java.io.File;
import javax.imageio.ImageIO;
import com.jachsoft.imagelib.RGBImage;
import junit.framework.TestCase;

public class GraySlicingTest extends TestCase {

    public void testApply() {
        try {
            RGBImage img = new RGBImage(ImageIO.read(new File("data/jach-160.jpg")));
            GraySlicing operator = new GraySlicing(img);
            operator.setParameters(127, 127, 127, 127, 127, 127, 127, 127, 127);
            ImageIO.write(operator.apply().getBufferedImage(), "jpg", new File("tests/gray_slicing.jpg"));
        } catch (Exception e) {
            fail("Caught an exception");
            e.printStackTrace();
        }
    }
}
