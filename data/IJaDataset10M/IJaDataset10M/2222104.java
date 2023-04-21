package org.neuroph.contrib.tileclassification.screentools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * This class uses a given Robot object to sample images from the screen at an 
 * arbitrary sampling resolution.  The coordinates of the scanning rectangle 
 * is (x, y) expressed as a fraction of the screen resolution, with the width also being
 * a fraction.
 * 
 * For example, when the screen is 800x600 resolution, to scan a rectangle with
 * xy coordinates (400,300) to xy (600, 400), use the following rectangle as an argument:
 * 
 * new Rectangle2D.Double(0.5, 0.5, 0.25, 0.16667)
 * 
 * Scanning using sampling is faster than scanning using a screenshot, but is
 * prone to introduce tearing and shearing into the scanned image.  Scanning
 * from a screenshot has no tearing or shearing at the cost of speed.
 * 
 * @author Jon Tait
 *
 */
public class ScreenSampler {

    /**
	 * 
	 * @param robot
	 * @param rectangleAsDecimalPercent
	 * @param samplingResolution
	 * @return
	 */
    public static BufferedImage scanLocationUsingScreenshot(Robot robot, Rectangle2D.Double rectangleAsDecimalPercent, Dimension samplingResolution) {
        return scanLocationUsingScreenshot(robot, rectangleAsDecimalPercent, samplingResolution, BufferedImage.TYPE_INT_RGB);
    }

    /**
	 * 
	 * @param robot
	 * @param rectangleAsDecimalPercent
	 * @param samplingResolution
	 * @param imageType
	 * @return
	 */
    public static BufferedImage scanLocationUsingScreenshot(Robot robot, Rectangle2D.Double rectangleAsDecimalPercent, Dimension samplingResolution, int imageType) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int inspectX = (int) Math.round((screenSize.width * rectangleAsDecimalPercent.x));
        int inspectY = (int) Math.round((screenSize.height * rectangleAsDecimalPercent.y));
        int inspectWidth = (int) Math.round((screenSize.width * rectangleAsDecimalPercent.width));
        int inspectHeight = (int) Math.round((screenSize.height * rectangleAsDecimalPercent.height));
        Rectangle screenRect = new Rectangle(inspectX, inspectY, inspectWidth, inspectHeight);
        BufferedImage screenCapture = robot.createScreenCapture(screenRect);
        return downSampleImage(samplingResolution, screenCapture, imageType);
    }

    public static BufferedImage downSampleImage(Dimension samplingResolution, BufferedImage bigImg) {
        return downSampleImage(samplingResolution, bigImg, BufferedImage.TYPE_INT_RGB);
    }

    public static BufferedImage downSampleImage(Dimension samplingResolution, BufferedImage bigImg, int returnImageType) {
        int inspectX;
        int inspectY;
        int numberOfSamplesAcross = samplingResolution.width;
        int numberOfSamplesDown = samplingResolution.height;
        if (bigImg.getWidth() <= numberOfSamplesAcross || bigImg.getHeight() <= numberOfSamplesDown) {
            return bigImg;
        }
        BufferedImage img = new BufferedImage(numberOfSamplesAcross, numberOfSamplesDown, returnImageType);
        double samplingIncrementX = bigImg.getWidth() / (samplingResolution.getWidth() - 1);
        double samplingIncrementY = bigImg.getHeight() / (samplingResolution.getHeight() - 1);
        double sampleX = 0;
        double sampleY = 0;
        for (int y = 0; y < numberOfSamplesDown; y++) {
            for (int x = 0; x < numberOfSamplesAcross; x++) {
                inspectX = (int) Math.round(sampleX);
                inspectY = (int) Math.round(sampleY);
                if (inspectX >= bigImg.getWidth()) {
                    inspectX = bigImg.getWidth() - 1;
                }
                if (inspectY >= bigImg.getHeight()) {
                    inspectY = bigImg.getHeight() - 1;
                }
                int color = bigImg.getRGB(inspectX, inspectY);
                img.setRGB(x, y, color);
                sampleX += samplingIncrementX;
            }
            sampleX = 0;
            sampleY += samplingIncrementY;
        }
        return img;
    }

    /**
	 * 
	 * @param robot
	 * @param rectangleAsDecimalPercent
	 * @param samplingResolution
	 * @return
	 */
    public static BufferedImage scanLocationUsingSampling(Robot robot, Rectangle2D.Double rectangleAsDecimalPercent, Dimension samplingResolution) {
        return scanLocationUsingSampling(robot, rectangleAsDecimalPercent, samplingResolution, BufferedImage.TYPE_INT_RGB);
    }

    /**
	 * 
	 * @param robot
	 * @param rectangleAsDecimalPercent
	 * @param samplingResolution
	 * @param imageType
	 * @return
	 */
    public static BufferedImage scanLocationUsingSampling(Robot robot, Rectangle2D.Double rectangleAsDecimalPercent, Dimension samplingResolution, int imageType) {
        double slotULXAsDecimalPercent = rectangleAsDecimalPercent.x;
        double slotULYAsDecimalPercent = rectangleAsDecimalPercent.y;
        double sampleIncrementAcrossAsDecimalPercent = rectangleAsDecimalPercent.width / (samplingResolution.getWidth() - 1);
        double sampleIncrementDownAsDecimalPercent = rectangleAsDecimalPercent.height / (samplingResolution.getHeight() - 1);
        int numberOfSamplesAcross = samplingResolution.width;
        int numberOfSamplesDown = samplingResolution.height;
        BufferedImage img = new BufferedImage(numberOfSamplesAcross, numberOfSamplesDown, imageType);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        waitForVerticalScreenRefresh();
        double inspectXAsDecimalPercent = slotULXAsDecimalPercent;
        double inspectYAsDecimalPercent = slotULYAsDecimalPercent;
        for (int y = 0; y < numberOfSamplesDown; y++) {
            for (int x = 0; x < numberOfSamplesAcross; x++) {
                int inspectX = (int) Math.round((screenSize.width * inspectXAsDecimalPercent));
                int inspectY = (int) Math.round((screenSize.height * inspectYAsDecimalPercent));
                Color color = robot.getPixelColor(inspectX, inspectY);
                img.setRGB(x, y, color.getRGB());
                inspectXAsDecimalPercent += sampleIncrementAcrossAsDecimalPercent;
            }
            inspectXAsDecimalPercent = slotULXAsDecimalPercent;
            inspectYAsDecimalPercent += sampleIncrementDownAsDecimalPercent;
        }
        return img;
    }

    /**
	 * Another name for this is "Vertical Sync".  Minimizes frame shearing.
	 */
    private static void waitForVerticalScreenRefresh() {
        Toolkit.getDefaultToolkit().sync();
    }
}
