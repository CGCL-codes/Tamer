package org.jfonia.view.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * Credits to Ken (http://explodingpixels.wordpress.com/2009/08/23/creating-a-hud-style-slider/)
 * @author Rik Bauwens
 */
public class HudSliderUI extends BasicSliderUI {

    private static final int SLIDER_KNOB_WIDTH = 11;

    private static final int SLIDER_KNOB_HEIGHT_NO_TICKS = 11;

    private static final int SLIDER_KNOB_HEIGHT_WITH_TICKS = 13;

    private static final int TRACK_HEIGHT = 4;

    private static final Color TRACK_BACKGROUND_COLOR = new Color(143, 147, 144, 100);

    private static final Color TRACK_BORDER_COLOR = new Color(255, 255, 255, 200);

    private static final Color TOP_SLIDER_KNOB_COLOR = new Color(0x555555);

    private static final Color BOTTOM_SLIDER_KNOB_COLOR = new Color(0x393939);

    private static final Color TOP_SLIDER_KNOB_PRESSED_COLOR = new Color(0xb0b2b6);

    private static final Color BOTTOM_SLIDER_KNOB_PRESSED_COLOR = new Color(0x86888b);

    public static final Color BORDER_COLOR = new Color(0xc5c8cf);

    private static final Color LIGHT_SHADOW_COLOR = new Color(0, 0, 0, 145);

    private static final Color DARK_SHADOW_COLOR = new Color(0, 0, 0, 50);

    private static final ShapeProvider NO_TICKS_SHAPE_PROVIDER = createCircularSliderKnobShapeProvider();

    private static final ShapeProvider TICKS_SHAPE_PROVIDER = createPointedSliderKnobShapeProvider();

    public HudSliderUI(JSlider b) {
        super(b);
    }

    @Override
    protected void installDefaults(JSlider slider) {
        super.installDefaults(slider);
        slider.setOpaque(false);
    }

    @Override
    protected Dimension getThumbSize() {
        int sliderKnobHeight = slider.getPaintTicks() ? SLIDER_KNOB_HEIGHT_WITH_TICKS : SLIDER_KNOB_HEIGHT_NO_TICKS;
        return new Dimension(SLIDER_KNOB_WIDTH, sliderKnobHeight);
    }

    @Override
    public void paintThumb(Graphics graphics) {
        Paint paint = createSliderKnobButtonPaint(isDragging(), thumbRect.height);
        ShapeProvider shapeProvider = slider.getPaintTicks() ? TICKS_SHAPE_PROVIDER : NO_TICKS_SHAPE_PROVIDER;
        paintHudControlBackground((Graphics2D) graphics, thumbRect, shapeProvider, paint);
    }

    @Override
    public void paintTrack(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double trackY = slider.getHeight() / 2.0 - TRACK_HEIGHT / 2.0;
        RoundRectangle2D track = new RoundRectangle2D.Double(0, trackY, slider.getWidth() - 1, TRACK_HEIGHT - 1, 4, 2);
        graphics.setColor(TRACK_BACKGROUND_COLOR);
        graphics2d.fill(track);
        graphics2d.setColor(TRACK_BORDER_COLOR);
        graphics2d.draw(track);
    }

    @Override
    protected int getTickLength() {
        return 5;
    }

    @Override
    protected void calculateThumbLocation() {
        super.calculateThumbLocation();
        if (slider.getOrientation() == JSlider.HORIZONTAL && slider.getPaintTicks()) {
            thumbRect.y += 3;
        } else {
        }
    }

    @Override
    protected void calculateTickRect() {
        super.calculateTickRect();
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            tickRect.y += 1;
        } else {
        }
    }

    @Override
    protected void paintMajorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x) {
        g.setColor(Color.WHITE);
        super.paintMajorTickForHorizSlider(g, tickBounds, x);
    }

    @Override
    public void setThumbLocation(int x, int y) {
        super.setThumbLocation(x, y);
        slider.repaint();
    }

    @Override
    public void paintFocus(Graphics g) {
    }

    private static Paint createSliderKnobButtonPaint(boolean isPressed, int height) {
        Color topColor = isPressed ? TOP_SLIDER_KNOB_PRESSED_COLOR : TOP_SLIDER_KNOB_COLOR;
        Color bottomColor = isPressed ? BOTTOM_SLIDER_KNOB_PRESSED_COLOR : BOTTOM_SLIDER_KNOB_COLOR;
        int bottomY = height - 2;
        return new GradientPaint(0, 0, topColor, 0, bottomY, bottomColor);
    }

    /**
     * Creates a simple circle.
     */
    private static ShapeProvider createCircularSliderKnobShapeProvider() {
        return new ShapeProvider() {

            public Shape createShape(double x, double y, double width, double height) {
                return new Ellipse2D.Double(x, y, width, height);
            }
        };
    }

    /**
     * Cerates a pointy slider thumb shape that looks roughly like this:
     *     +----+
     *    /      \
     *    +      +
     *    |      |
     *    +      +
     *      \  /
     *       \/
     */
    private static ShapeProvider createPointedSliderKnobShapeProvider() {
        return new ShapeProvider() {

            public Shape createShape(double x, double y, double width, double height) {
                float xFloat = (float) x;
                float yFloat = (float) y;
                float widthFloat = (float) width;
                float heightFloat = (float) height;
                GeneralPath path = new GeneralPath();
                path.moveTo(xFloat + 2.0f, yFloat);
                path.curveTo(xFloat + 0.25f, yFloat + 0.25f, xFloat - 0.25f, yFloat + 2.0f, xFloat, yFloat + 2.0f);
                path.lineTo(xFloat, yFloat + heightFloat / 1.60f);
                path.lineTo(xFloat + widthFloat / 2, yFloat + heightFloat);
                path.lineTo(xFloat + widthFloat, yFloat + heightFloat / 1.60f);
                path.lineTo(xFloat + widthFloat, yFloat + 2.0f);
                path.curveTo(xFloat + widthFloat - 0.25f, yFloat + 2.0f, xFloat + widthFloat - 0.25f, yFloat + 0.25f, xFloat + widthFloat - 2.0f, yFloat);
                path.closePath();
                return path;
            }
        };
    }

    /**
     * Paints a HUD style background in the given shape. This includes a drop shadow
     * which will be drawn under the shape to be painted. The shadow will be draw
     * outside the given bounds.
     * @param graphics the {@code Graphics2D} context to draw in.
     * @param bounds the bounds to paint in.
     * @param shapeProvider the delegate to request the {@link Shape} from.
     * @param paint the {@link Paint} to use to fill the {@code Shape}.
     */
    public static void paintHudControlBackground(Graphics2D graphics, Rectangle bounds, ShapeProvider shapeProvider, Paint paint) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = bounds.height;
        graphics.setColor(LIGHT_SHADOW_COLOR);
        graphics.draw(shapeProvider.createShape(x, y, width - 1, height));
        graphics.setColor(DARK_SHADOW_COLOR);
        graphics.draw(shapeProvider.createShape(x, y, width - 1, height + 1));
        graphics.setPaint(paint);
        graphics.fill(shapeProvider.createShape(x, y + 1, width, height - 1));
        graphics.setColor(BORDER_COLOR);
        graphics.draw(shapeProvider.createShape(x, y, width - 1, height - 1));
    }

    /**
     * An interface for specifying a shape to paint and draw a drop shadown under.
     */
    public interface ShapeProvider {

        Shape createShape(double x, double y, double width, double height);
    }
}
