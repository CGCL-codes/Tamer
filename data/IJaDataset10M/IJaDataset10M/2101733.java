package com.ezware.oxbow.swingbits.graphics;

import java.awt.AWTException;
import java.awt.AWTPermission;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Screenshot {

    private final BufferedImage image;

    /**
     * Creates an image containing pixels read from the screen.  This image does
     * not include the mouse cursor.
	 * @param screenRect Rect to capture in screen coordinates
	 * @return captured screenshot
     * @throws 	IllegalArgumentException if <code>screenRect</code> width and height are not greater than zero
     * @throws 	SecurityException if <code>readDisplayPixels</code> permission is not granted
	 * @throws AWTException
     * @see     SecurityManager#checkPermission
     * @see 	AWTPermission
	 */
    public static final Screenshot capture(Rectangle screenRect) throws AWTException {
        return new Screenshot(getRobot().createScreenCapture(screenRect));
    }

    public static final Screenshot capture(int width, int height) throws AWTException {
        return capture(new Rectangle(width, height));
    }

    private static Robot robot;

    private static synchronized Robot getRobot() throws AWTException {
        if (robot == null) robot = new Robot();
        return robot;
    }

    private Screenshot(BufferedImage image) {
        this.image = image;
    }

    /**
	 * Returns the image screenshot is based on
	 */
    public Image getImage() {
        return image;
    }

    /**
     * Writes an image using an arbitrary <code>ImageWriter</code>
     * that supports the given format to a <code>File</code>.  If
     * there is already a <code>File</code> present, its contents are
     * discarded.
	 * @param formatName a <code>String</code> containg the informal name of the format.
	 * @param output a <code>File</code> to be written to
	 *
	 * if an error occurs during writing.
	 *
     * @exception IllegalArgumentException if any parameter is <code>null</code>.
	 * @throws IOException if an error occurs during writing.
	 */
    public boolean store(String formatName, File output) throws IOException {
        return ImageIO.write(image, formatName, output);
    }

    /**
	 * Retrieves screen information as a list of display modes, 
	 * which includes screen dimensions, bit depth and refresh rate
	 * @return
	 */
    public static final List<DisplayMode> getDisplayInfo() {
        List<DisplayMode> result = new ArrayList<DisplayMode>();
        for (GraphicsDevice gs : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            result.add(gs.getDisplayMode());
        }
        return result;
    }
}
