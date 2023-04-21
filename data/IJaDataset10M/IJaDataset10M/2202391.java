package org.ladybug.gui.toolbox.renderers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.List;
import org.ladybug.gui.toolbox.ImageEffects;

/**
 * @author Aurelian Pop
 */
public final class FillingRenderer extends AbstractRenderer {

    private Color fillingColor;

    private float transparencyAlpha;

    public FillingRenderer() {
        super(1);
    }

    public void setFillingColor(final Color newFillingColor) {
        fillingColor = newFillingColor;
        invalidateCache();
    }

    public void setTransparencyAlpha(final float newTransparencyAlpha) {
        transparencyAlpha = newTransparencyAlpha;
        invalidateCache();
    }

    @Override
    protected BufferedImage renderImage(final List<BufferedImage> images) {
        final BufferedImage image = images.get(0);
        final BufferedImage filledImage = ImageEffects.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
        final Graphics2D g2d = filledImage.createGraphics();
        g2d.setColor(fillingColor);
        g2d.drawImage(image, 0, 0, null);
        g2d.setComposite(AlphaComposite.SrcIn.derive(transparencyAlpha));
        g2d.fillRect(0, 0, filledImage.getWidth(), filledImage.getHeight());
        g2d.dispose();
        return filledImage;
    }
}
