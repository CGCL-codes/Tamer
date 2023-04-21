package org.ladybug.gui.toolbox.renderers;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.List;
import org.ladybug.gui.toolbox.ImageEffects;

/**
 * Creates chromed text images.
 * 
 * @author Aurelian Pop
 */
public class ChromedTextRenderer extends AbstractRenderer {

    private static final Color ALMOST_BLACK = new Color(15, 15, 15);

    private static final Color MEDIUM_DARK_GRAY = new Color(95, 95, 95);

    private String text;

    private FontMetrics fontMetrics;

    private Color color;

    /**
     * Constructs a new instance
     */
    public ChromedTextRenderer() {
        super(0);
    }

    public void setColor(final Color newColor) {
        color = newColor;
        invalidateCache();
    }

    public void setFont(final FontMetrics newFontMetrics) {
        fontMetrics = newFontMetrics;
        invalidateCache();
    }

    public void setText(final String newText) {
        text = newText;
        invalidateCache();
    }

    @Override
    protected BufferedImage renderImage(final List<BufferedImage> images) {
        int imageWidth = fontMetrics.stringWidth(text);
        int imageHeight = 1 + fontMetrics.getAscent() + fontMetrics.getDescent();
        final int outlineThickness = imageHeight / 100 + 1;
        imageWidth += 2 * outlineThickness;
        imageHeight += 2 * outlineThickness;
        final BufferedImage image = ImageEffects.createCompatibleImage(imageWidth, imageHeight, Transparency.TRANSLUCENT);
        final Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setFont(fontMetrics.getFont());
        g2d.setColor(color);
        {
            final float gradEmbossPos1 = 0.10f;
            final float gradEmbossPos2 = 0.55f;
            final float gradEmbossPos3 = 0.85f;
            final float gradEmbossPos4 = 1.00f;
            final Color gradEmbossLitColor1 = Color.LIGHT_GRAY;
            final Color gradEmbossLitColor2 = Color.DARK_GRAY;
            final Color gradEmbossLitColor3 = Color.BLACK;
            final Color gradEmbossLitColor4 = Color.DARK_GRAY;
            final Paint origPaint = g2d.getPaint();
            final Paint lgpEmbossLit = new LinearGradientPaint(0, 0, 0, imageHeight, new float[] { gradEmbossPos1, gradEmbossPos2, gradEmbossPos3, gradEmbossPos4 }, new Color[] { gradEmbossLitColor1, gradEmbossLitColor2, gradEmbossLitColor3, gradEmbossLitColor4 });
            g2d.setPaint(lgpEmbossLit);
            ImageEffects.outlineText(g2d, text, fontMetrics, outlineThickness, new Point(0, 0));
            g2d.setPaint(origPaint);
        }
        {
            final float gradTextPos1 = 0.25f;
            final float gradTextPos2 = 0.55f;
            final float gradTextPos3 = 0.60f;
            final float gradTextPos4 = 0.85f;
            final float gradTextPos5 = 0.90f;
            final float gradTextPos6 = 0.98f;
            final Color gradTextColor1 = Color.DARK_GRAY;
            final Color gradTextColor2 = Color.WHITE;
            final Color gradTextColor3 = ALMOST_BLACK;
            final Color gradTextColor4 = MEDIUM_DARK_GRAY;
            final Color gradTextColor5 = MEDIUM_DARK_GRAY;
            final Color gradTextColor6 = Color.BLACK;
            final Paint lgpText = new LinearGradientPaint(0, 0, 0, imageHeight, new float[] { gradTextPos1, gradTextPos2, gradTextPos3, gradTextPos4, gradTextPos5, gradTextPos6 }, new Color[] { gradTextColor1, gradTextColor2, gradTextColor3, gradTextColor4, gradTextColor5, gradTextColor6 });
            g2d.setPaint(lgpText);
        }
        g2d.drawString(text, outlineThickness, outlineThickness + fontMetrics.getMaxAscent() - 1);
        g2d.dispose();
        return image;
    }
}
