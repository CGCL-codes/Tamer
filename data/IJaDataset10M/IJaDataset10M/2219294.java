package a03.swing.plaf.style;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plaf.A03SliderDelegate;
import a03.swing.plaf.A03SwingUtilities;
import a03.swing.plugin.A03FadeTrackerPlugin;
import a03.swing.plugin.A03PluginManager;

public class A03StyledSliderDelegate implements A03SliderDelegate {

    private A03SliderStyle style;

    public A03StyledSliderDelegate(A03SliderStyle style) {
        this.style = style;
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        return insets;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    }

    public void paintFocus(Component c, Graphics g, Rectangle focusRect) {
        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = c.getWidth();
        int height = c.getHeight();
        A03StyledGraphicsUtilities.paintBorderShadow(graphics, 2, A03GraphicsUtilities.createRoundRectangle(1, 1, width - 3, height - 3, 3), style.getFocusColor(), UIManager.getColor("control"));
        graphics.dispose();
    }

    public void paintThumb(Component c, Graphics g, Rectangle trackRect, Rectangle thumbRect) {
        JSlider slider = (JSlider) c;
        int state = A03StyledSwingUtilities.getState(c);
        int orientation = slider.getOrientation();
        Paint background;
        Paint border;
        Shape shape;
        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float fadeLevel;
        if (slider.getPaintTrack()) {
            int x = 0;
            int y = 0;
            int width = 0;
            int height = 0;
            int trackSize = 6;
            if (orientation == JSlider.HORIZONTAL) {
                x = trackRect.x;
                y = trackRect.y + trackRect.height / 2 - 3;
                background = style.getTrackBackgroundPaint(state, orientation, x, y, trackSize - 1, trackSize);
                shape = A03GraphicsUtilities.createRoundRectangle(x, y, trackRect.width, trackSize, 3);
                graphics.setPaint(background);
                graphics.fill(shape);
                border = style.getTrackBorderPaint(state, orientation, x, y, trackSize - 1, trackSize - 1);
                shape = A03GraphicsUtilities.createRoundRectangle(x, y, trackRect.width - 1, trackSize - 1, 3);
                graphics.setPaint(border);
                graphics.draw(shape);
            } else {
                x = trackRect.x + trackRect.width / 2 - 3;
                y = trackRect.y;
                background = style.getTrackBackgroundPaint(state, orientation, x, y, trackSize, trackRect.height);
                shape = A03GraphicsUtilities.createRoundRectangle(x, y, trackSize, trackRect.height, 3);
                graphics.setPaint(background);
                graphics.fill(shape);
                border = style.getTrackBorderPaint(state, orientation, x, y, trackSize - 1, trackSize - 1);
                shape = A03GraphicsUtilities.createRoundRectangle(x, y, trackSize - 1, trackRect.height - 1, 3);
                graphics.setPaint(border);
                graphics.draw(shape);
            }
            if (slider.getValue() > slider.getMinimum()) {
                if (orientation == JSlider.HORIZONTAL) {
                    if (A03SwingUtilities.isLeftToRight(slider)) {
                        if (slider.getValue() == slider.getMaximum()) {
                            width = trackRect.width - 2;
                        } else {
                            width = thumbRect.x - trackRect.x + thumbRect.width / 2;
                        }
                        x = trackRect.x + 1;
                        y = trackRect.y + trackRect.height / 2 - 2;
                        height = 4;
                    } else {
                        if (slider.getValue() == slider.getMaximum()) {
                            x = trackRect.x + 1;
                            width = trackRect.width - 2;
                        } else {
                            x = thumbRect.x + thumbRect.width / 2;
                            width = trackRect.width - (thumbRect.x + thumbRect.width / 2 - trackRect.x) - 1;
                        }
                        y = trackRect.y + trackRect.height / 2 - 2;
                        height = 4;
                    }
                } else {
                    if (slider.getValue() == slider.getMaximum()) {
                        y = trackRect.y + 1;
                        height = trackRect.height - 2;
                    } else {
                        y = thumbRect.y + thumbRect.height / 2;
                        height = trackRect.height - (thumbRect.y + thumbRect.height / 2 - trackRect.y) - 1;
                    }
                    x = trackRect.x + trackRect.width / 2 - 2;
                    width = 4;
                }
                background = style.getThumbBackgroundPaint(state, orientation, x, y, width, height);
                shape = A03GraphicsUtilities.createRoundRectangle(x, y, width, height, width > 4 || height > 4 ? 3 : 2);
                graphics.setPaint(background);
                graphics.fill(shape);
                border = style.getThumbBorderPaint(state, orientation, x - 1, y - 1, width + 1, height + 1);
                shape = A03GraphicsUtilities.createRoundRectangle(x - 1, y - 1, width + 1, height + 1, width > 4 || height > 4 ? 3 : 2);
                graphics.setPaint(border);
                graphics.draw(shape);
            }
            fadeLevel = (float) A03PluginManager.getInstance().getPlugin(A03FadeTrackerPlugin.class).getFadeLevel(slider);
        } else {
            fadeLevel = 1.0f;
        }
        if (fadeLevel > 0) {
            Image image = A03GraphicsUtilities.createImage(c, thumbRect.width, thumbRect.height);
            Graphics2D imageGraphics = (Graphics2D) image.getGraphics();
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            if (fadeLevel < 1) {
                imageGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeLevel));
            }
            background = style.getThumbBackgroundPaint(state, orientation, 1, 1, thumbRect.width - 4, thumbRect.height - 4);
            border = style.getThumbBorderPaint(state, orientation, 1, 1, thumbRect.width - 4, thumbRect.height - 4);
            imageGraphics.setPaint(background);
            shape = A03GraphicsUtilities.createRoundRectangle(0, 0, thumbRect.width - 1, thumbRect.height - 1, 3);
            imageGraphics.fill(shape);
            imageGraphics.setPaint(border);
            shape = A03GraphicsUtilities.createRoundRectangle(0, 0, thumbRect.width - 1, thumbRect.height - 1, 3);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            imageGraphics.draw(shape);
            imageGraphics.dispose();
            graphics.drawImage(image, thumbRect.x, thumbRect.y, c);
        }
        graphics.dispose();
    }

    public void paintTrack(Component c, Graphics g, Rectangle trackRect, Rectangle thumbRect) {
        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        JSlider slider = (JSlider) c;
        int state = A03StyledSwingUtilities.getState(c);
        int orientation = slider.getOrientation();
        Shape shape;
        if (orientation == JSlider.HORIZONTAL) {
            graphics.setPaint(style.getTrackBackgroundPaint(state, orientation, trackRect.x + 1, trackRect.y + trackRect.height / 2 - 2, trackRect.x + 1, trackRect.y + trackRect.height / 2 + 1));
            shape = A03GraphicsUtilities.createRoundRectangle(trackRect.x, trackRect.y + trackRect.height / 2 - 3, trackRect.width - 1, 5, 3);
            graphics.fill(shape);
        } else {
            graphics.setPaint(style.getTrackBackgroundPaint(state, orientation, trackRect.x + trackRect.width / 2 - 2, trackRect.y + 1, trackRect.x + trackRect.width / 2 + 1, trackRect.y + 1));
            shape = A03GraphicsUtilities.createRoundRectangle(trackRect.x + trackRect.width / 2 - 3, trackRect.y, 5, trackRect.height - 1, 3);
            graphics.fill(shape);
        }
        graphics.dispose();
    }

    public FontUIResource getFont() {
        return new FontUIResource(style.getFont());
    }

    public ColorUIResource getForeground() {
        return new ColorUIResource(style.getForegroundColor());
    }

    public ColorUIResource getBackground() {
        return new ColorUIResource(style.getBackgroundColor());
    }
}
