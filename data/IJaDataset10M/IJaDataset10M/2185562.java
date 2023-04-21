package fr.amille.animebrowser.model.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

/**
 * @author amille
 * 
 */
public abstract class EmptyImageGenerator {

    private static HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>(2);

    public static synchronized BufferedImage buildImage(final String text, final Dimension imageSize, final Color textColor, final String buttonImagePath, final int xModifier, final int yModifier) {
        if ((imageSize == null) || (text == null) || (textColor == null) || (buttonImagePath == null)) {
            return null;
        }
        final File buttonImageFile = new File(buttonImagePath);
        if (!buttonImageFile.exists()) {
            return null;
        }
        final BufferedImage existingImage = EmptyImageGenerator.images.get(text);
        if (existingImage != null) {
            return existingImage;
        }
        final RenderingHints renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        renderHints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        final BufferedImage scratchImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        final Graphics2D scratchG2 = scratchImage.createGraphics();
        scratchG2.setRenderingHints(renderHints);
        final Font font = UIManager.getDefaults().getFont("Button.font");
        final FontRenderContext frc = scratchG2.getFontRenderContext();
        final TextLayout tl = new TextLayout(text, font, frc);
        final Rectangle2D textBounds = tl.getBounds();
        final int textWidth = (int) Math.ceil(textBounds.getWidth());
        final ImageIcon emptySerieButton = new ImageIcon(buttonImagePath);
        final BufferedImage image = EmptyImageGenerator.convertImageToBufferedImage(emptySerieButton.getImage());
        final Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setColor(textColor);
        tl.draw(g2, ((float) imageSize.getWidth() / 2f) - (textWidth / 2f) + xModifier, ((float) imageSize.getHeight() / 2f) + yModifier);
        scratchG2.dispose();
        scratchImage.flush();
        g2.dispose();
        EmptyImageGenerator.images.put(text, image);
        return image;
    }

    private static BufferedImage convertImageToBufferedImage(final Image imageToBuffer) {
        final Image image = new ImageIcon(imageToBuffer).getImage();
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice gs = ge.getDefaultScreenDevice();
        final GraphicsConfiguration gc = gs.getDefaultConfiguration();
        BufferedImage bufferedImage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.OPAQUE);
        if (bufferedImage == null) {
            bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        }
        final Graphics g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bufferedImage;
    }
}
