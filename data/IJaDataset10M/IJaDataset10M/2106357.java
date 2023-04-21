package org.ladybug.gui.toolbox.renderers;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author Aurelian Pop
 */
public final class ImageRenderer extends AbstractRenderer {

    private BufferedImage image;

    public ImageRenderer() {
        super(0);
    }

    public ImageRenderer(final BufferedImage image) {
        this();
        setImage(image);
    }

    public void setImage(final BufferedImage newImage) {
        image = newImage;
        invalidateCache();
    }

    @Override
    protected BufferedImage renderImage(final List<BufferedImage> images) {
        return image;
    }
}
