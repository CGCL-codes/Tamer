package org.encog.util;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.concurrent.Semaphore;
import org.encog.EncogError;

/**
 * Simple class to determine the size of an image.
 * 
 * @author jheaton
 */
public class ImageSize implements ImageObserver {

    /**
	 * The width of the image.
	 */
    private int width;

    /**
	 * The height of the image.
	 */
    private int height;

    /**
	 * Wait for the values to be set.
	 */
    private final Semaphore wait;

    /**
	 * Determine the size of an image.
	 * 
	 * @param image
	 *            The image to be sized.
	 */
    public ImageSize(final Image image) {
        this.wait = new Semaphore(0);
        this.width = image.getWidth(this);
        this.height = image.getHeight(this);
        if ((this.width == -1) || (this.height == -1)) {
            try {
                this.wait.acquire();
            } catch (final InterruptedException e) {
                throw new EncogError(e);
            }
        }
    }

    /**
	 * @return the height
	 */
    public int getHeight() {
        return this.height;
    }

    /**
	 * @return the width
	 */
    public int getWidth() {
        return this.width;
    }

    /**
	 * The image has been updated.
	 * 
	 * @param img
	 *            The image.
	 * @param infoflags
	 *            Which data has been loaded.
	 * @param x
	 *            Not used.
	 * @param y
	 *            Not used.
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @return True if more data is still needed.
	 */
    public boolean imageUpdate(final Image img, final int infoflags, final int x, final int y, final int width, final int height) {
        final int c = ImageObserver.HEIGHT | ImageObserver.WIDTH;
        if ((infoflags & c) != c) {
            return true;
        }
        this.height = height;
        this.width = width;
        return false;
    }
}
