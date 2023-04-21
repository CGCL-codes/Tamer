package org.openwar.victory.graphics.j2d;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.openwar.victory.graphics.Image;

/**
 * A Java2D Image implementation.
 * @author Bart van Heukelom
 */
public class JImage extends Image {

    private final BufferedImage image;

    /**
     * Create a new image from an existing BufferedImage.
     * @param bufImage The BufferedImage to wrap this image around.
     */
    public JImage(final BufferedImage bufImage) {
        image = bufImage;
    }

    /**
     * @return The buffered image around which this game image is wrapped.
     */
    public BufferedImage getImage() {
        return image;
    }

    /** {@inheritDoc} */
    @Override
    public int getWidth() {
        return image.getWidth();
    }

    /** {@inheritDoc} */
    @Override
    public int getHeight() {
        return image.getHeight();
    }

    /** {@inheritDoc} */
    @Override
    public JGC getGC() {
        return new JGC(image.createGraphics(), getWidth(), getHeight());
    }

    /**
     * @return A Java2D/AWT <code>Graphics2D</code> context which can be used
     *         for drawing on this image
     */
    public Graphics2D getGraphics2D() {
        return image.createGraphics();
    }

    /** {@inheritDoc} */
    @Override
    public int getTransparency() {
        return image.getTransparency();
    }
}
