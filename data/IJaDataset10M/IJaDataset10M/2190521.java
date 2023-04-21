package org.boblight4j.client.grabber;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.boblight4j.client.Client;

/**
 * This is a base class for a grabber implementation which passively grabs
 * pixels from a device such as a screen.<br>
 * <br>
 * The method {@link #frameToBoblight(BufferedImage)} accepts a
 * {@link BufferedImage} as argument which can be invoked by a passively acting
 * Grabber implementation.
 * 
 * @author agebauer
 * 
 */
public abstract class AbstractPassiveGrabber extends AbstractGrabber implements PassiveGrabber {

    public AbstractPassiveGrabber(final Client client, final boolean sync, final int width, final int height) {
        super(client, sync, width, height);
    }

    /**
	 * Use this method in your passive grabber implementation to let the passed
	 * BufferedImage get grabbed. Also puts pixels to the debug image if debug
	 * is set to true.
	 * 
	 * @param img
	 *            the image to grab
	 */
    @Override
    public void frameToBoblight(final BufferedImage img) {
        final double scaledX = (double) img.getWidth() / (double) this.width;
        final double scaledY = (double) img.getHeight() / (double) this.height;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                final int resX = (int) (scaledX * x + scaledX / 2);
                final int resY = (int) (scaledY * y + scaledY / 2);
                final int rgbInt = img.getRGB(resX, resY);
                final int[] rgb = new int[3];
                final Color color = new Color(rgbInt);
                rgb[0] = color.getBlue();
                rgb[1] = color.getGreen();
                rgb[2] = color.getRed();
                this.getClient().getLightsHolder().addPixel(this.width - x, y, rgb);
                if (this.debug) {
                    this.setDebugPixel(x, y, rgb);
                }
            }
        }
        if (this.debug) {
            this.drawDebugImage();
        }
    }
}
