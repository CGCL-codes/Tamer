package org.jdesktop.jxlayer.plaf.effect;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.lang.ref.SoftReference;

/**
 * The base class for {@link org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect}
 */
public class AbstractBufferedImageOpEffect extends AbstractLayerEffect {

    private transient SoftReference<BufferedImage> cachedSubImage;

    private static final BufferedImageOp[] emptyOpsArray = new BufferedImageOp[0];

    /**
     * {@inheritDoc}
     * <p/>
     * Filters the passed image using {@code clip} and 
     * {@code BufferedImageOp}s provided by {@link #getBufferedImageOps()}
     */
    public void apply(BufferedImage buffer, Shape clip) {
        if (buffer == null) {
            throw new IllegalArgumentException("BufferedImage is null");
        }
        Rectangle bufferSize = new Rectangle(buffer.getWidth(), buffer.getHeight());
        if (clip == null) {
            clip = bufferSize;
        }
        Rectangle clipBounds = clip.getBounds().intersection(bufferSize);
        if (clipBounds.isEmpty() || buffer.getWidth() <= clipBounds.x || buffer.getHeight() <= clipBounds.y) {
            return;
        }
        int x = clipBounds.x;
        int y = clipBounds.y;
        int width = clipBounds.width;
        int height = clipBounds.height;
        if (buffer.getWidth() < x + width) {
            width = buffer.getWidth() - x;
        }
        if (buffer.getHeight() < y + height) {
            height = buffer.getHeight() - y;
        }
        BufferedImage subImage = cachedSubImage == null ? null : cachedSubImage.get();
        if (subImage == null || subImage.getWidth() != width || subImage.getHeight() != height) {
            subImage = new BufferedImage(width, height, buffer.getType());
            cachedSubImage = new SoftReference<BufferedImage>(subImage);
        }
        Graphics2D bufg = buffer.createGraphics();
        bufg.setClip(clip);
        Graphics2D subg = subImage.createGraphics();
        for (BufferedImageOp op : getBufferedImageOps()) {
            subg.drawImage(buffer, 0, 0, width, height, x, y, x + width, y + height, null);
            bufg.drawImage(subImage, op, x, y);
        }
        subg.dispose();
        bufg.dispose();
    }

    /**
     * Returns the array of {@code BufferedImageOp}s 
     * specified for this {@code LayerEffect}.
     *
     * @return the array of {@code BufferedImageOp}s 
     * specified for this {@code LayerEffect}
     */
    protected BufferedImageOp[] getBufferedImageOps() {
        return emptyOpsArray;
    }
}
