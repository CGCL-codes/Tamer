package org.jbudget.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import org.jbudget.util.PaintUtils;

public class ScalableArrowButtonUI extends ButtonUIBase {

    /** Is button orientation horizontal or vertical? */
    private int orientation;

    /** Image buffer for normal button state. */
    private BufferedImage normalButtonImage = null;

    /** Image buffer for rollover button state. */
    private BufferedImage rolloverButtonImage = null;

    /** Image buffer for pressed button state. */
    private BufferedImage pressedButtonImage = null;

    /** Geometry constants used to draw the button. */
    private Sizes sizes = new Sizes();

    /** Desktop hints to detect antialiasing. */
    private Map desktopHints = null;

    public static ComponentUI createUI(JComponent c) {
        return new ScalableArrowButtonUI();
    }

    /** Sets the button orientation.
     * 
     * @param orientation can be either SwingConstants.HORIZONTAL or 
     * SwingConstants.VERTICAL.
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    @Override
    public Dimension getMinimumSize(JComponent c) {
        return new Dimension(10, 10);
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(15, 15);
    }

    @Override
    public Dimension getMaximumSize(JComponent c) {
        return new Dimension(10000, 10000);
    }

    /** Main Painting method. */
    @Override
    public void paint(Graphics g, JComponent c) {
        assert button == c;
        Graphics2D g2d = (Graphics2D) g;
        if (desktopHints == null) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            desktopHints = (Map) (tk.getDesktopProperty("awt.font.desktophints"));
        }
        if (desktopHints != null) g2d.addRenderingHints(desktopHints);
        if (c.getWidth() != sizes.totalWidth || c.getHeight() != sizes.totalHeight) {
            updateBuffers();
        }
        if (isPressed) {
            g.drawImage(pressedButtonImage, 0, 0, sizes.totalWidth, sizes.totalHeight, c);
        } else if (isInRollover) {
            g.drawImage(rolloverButtonImage, 0, 0, sizes.totalWidth, sizes.totalHeight, c);
        } else {
            g.drawImage(normalButtonImage, 0, 0, sizes.totalWidth, sizes.totalHeight, c);
        }
    }

    /** Recreates and updates all buffered images. */
    private void updateBuffers() {
        updateGeometry();
        Path2D outline = this.generateOutline();
        allocateBuffers();
        updateNormalBuffer(outline);
        updateRolloverBuffer(outline);
        updatePressedBuffer(outline);
    }

    /** Allocates internal buffers. */
    private void allocateBuffers() {
        normalButtonImage = PaintUtils.createCompatibleImage(sizes.totalWidth, sizes.totalHeight);
        rolloverButtonImage = PaintUtils.createCompatibleImage(sizes.totalWidth, sizes.totalHeight);
        pressedButtonImage = PaintUtils.createCompatibleImage(sizes.totalWidth, sizes.totalHeight);
    }

    /** Paints normal button to the normalButtonImage. */
    private void updateNormalBuffer(Path2D outline) {
        Graphics2D g = (Graphics2D) normalButtonImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(button.getBackground());
        g.fillRect(0, 0, sizes.totalWidth, sizes.totalHeight);
        paintShadow(g, outline);
        g.setColor(foregroundColor);
        g.fill(outline);
        paintHighlight(normalButtonImage, outline, foregroundColor);
        g.setColor(new Color(0, 0, 0, 150));
        g.draw(outline);
        g.setColor(Color.BLACK);
        g.fill(generateArrow());
    }

    /** Paints rollover button to the rolloverButtonImage. */
    private void updateRolloverBuffer(Path2D outline) {
        Graphics2D g = (Graphics2D) rolloverButtonImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(button.getBackground());
        g.fillRect(0, 0, sizes.totalWidth, sizes.totalHeight);
        paintShadow(g, outline);
        g.setColor(highlightColor);
        g.fill(outline);
        paintHighlight(rolloverButtonImage, outline, highlightColor);
        g.setColor(new Color(0, 0, 0, 150));
        g.draw(outline);
        g.setColor(Color.BLACK);
        g.fill(generateArrow());
    }

    /** Paints pressed button to the pressedButtonImage. */
    private void updatePressedBuffer(Path2D outline) {
        Graphics2D g = (Graphics2D) pressedButtonImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(button.getBackground());
        g.fillRect(0, 0, sizes.totalWidth, sizes.totalHeight);
        g.setColor(shadowColor);
        g.fill(outline);
        paintPressedHighlight(pressedButtonImage, outline, shadowColor);
        g.setColor(new Color(0, 0, 0, 150));
        g.draw(outline);
        double tx = 0.0;
        double ty = 0.0;
        if (orientation == SwingConstants.EAST || orientation == SwingConstants.WEST) {
            tx = sizes.x / 3.0;
            if (tx < 1.0) tx = 1.0;
        } else {
            ty = sizes.y / 3.0;
            if (ty < 1.0) ty = 1.0;
        }
        g.setColor(Color.BLACK);
        g.translate(tx, ty);
        g.fill(generateArrow());
    }

    /** Draws the shadow for the button. */
    private void paintShadow(Graphics2D g, Path2D outline) {
        double tx = 0.0;
        double ty = 0.0;
        g.setColor(new Color(0, 0, 0, 50));
        ty = sizes.y / 3;
        tx = sizes.x / 3;
        g.translate(tx, ty);
        g.fill(outline);
        g.translate(-tx, -ty);
        g.setColor(new Color(0, 0, 0, 100));
        ty = sizes.y / 2;
        tx = sizes.x / 2;
        g.translate(tx, ty);
        g.fill(outline);
        g.translate(-tx, -ty);
    }

    /** draws the highlight. */
    private void paintHighlight(BufferedImage image, Path2D outline, Color baseColor) {
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.clip(outline);
        int maxComponent = baseColor.getRed() > baseColor.getGreen() ? baseColor.getRed() : baseColor.getGreen();
        if (baseColor.getBlue() > maxComponent) {
            maxComponent = baseColor.getBlue();
        }
        int shade = (int) (Math.sqrt(maxComponent / 255f) * 200);
        if (orientation == SwingConstants.EAST || orientation == SwingConstants.WEST) {
            GradientPaint highlight = new GradientPaint(new Point2D.Float(0, sizes.y), new Color(255, 255, 255, shade), new Point2D.Float(0, sizes.height / 4), new Color(255, 255, 255, 0));
            g.setPaint(highlight);
            fillRect(g, 0, 0, sizes.totalWidth, sizes.height / 3);
            drawRect(g, 0, 0, sizes.totalWidth, sizes.height / 2);
            highlight = new GradientPaint(new Point2D.Float(0, sizes.y + sizes.height * 3 / 4), new Color(0, 0, 0, 0), new Point2D.Float(0, sizes.y + sizes.height), new Color(0, 0, 0, 255 - shade));
            g.setPaint(highlight);
            fillRect(g, 0, sizes.y + sizes.height * 2 / 3, sizes.totalWidth, sizes.height / 3);
            drawRect(g, 0, sizes.y + sizes.height * 2 / 3, sizes.totalWidth, sizes.height / 3);
        } else {
            GradientPaint highlight = new GradientPaint(new Point2D.Float(sizes.x, 0), new Color(255, 255, 255, shade), new Point2D.Float(sizes.width / 4, 0), new Color(255, 255, 255, 0));
            g.setPaint(highlight);
            fillRect(g, 0, 0, sizes.width / 3, sizes.totalHeight);
            highlight = new GradientPaint(new Point2D.Float(sizes.x + sizes.width * 3 / 4, 0), new Color(0, 0, 0, 0), new Point2D.Float(sizes.x + sizes.width, 0), new Color(0, 0, 0, 255 - shade));
            g.setPaint(highlight);
            fillRect(g, sizes.x + sizes.width * 2 / 3, 0, sizes.width / 3, sizes.totalHeight);
        }
    }

    /** draws the highlight. */
    private void paintPressedHighlight(BufferedImage image, Path2D outline, Color baseColor) {
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.clip(outline);
        int maxComponent = baseColor.getRed() > baseColor.getGreen() ? baseColor.getRed() : baseColor.getGreen();
        if (baseColor.getBlue() > maxComponent) {
            maxComponent = baseColor.getBlue();
        }
        int shade = (int) (Math.sqrt(maxComponent / 255f) * 150);
        if (orientation == SwingConstants.EAST || orientation == SwingConstants.WEST) {
            GradientPaint highlight = new GradientPaint(new Point2D.Float(0, sizes.y), new Color(0, 0, 0, shade), new Point2D.Float(0, sizes.height * 3 / 4), new Color(0, 0, 0, 0));
            g.setPaint(highlight);
            fillRect(g, 0, 0, sizes.totalWidth, sizes.totalHeight * 4 / 5);
            highlight = new GradientPaint(new Point2D.Float(0, sizes.y + sizes.height * 3 / 4), new Color(255, 255, 255, 0), new Point2D.Float(0, sizes.y + sizes.height), new Color(255, 255, 255, 255 - shade));
            g.setPaint(highlight);
            fillRect(g, 0, sizes.totalHeight * 2 / 3, sizes.totalWidth, sizes.totalHeight / 3);
        } else {
            GradientPaint highlight = new GradientPaint(new Point2D.Float(sizes.x, 0), new Color(0, 0, 0, shade), new Point2D.Float(sizes.width * 3 / 4, 0), new Color(0, 0, 0, 0));
            g.setPaint(highlight);
            fillRect(g, 0, 0, sizes.totalWidth * 4 / 5, sizes.totalHeight);
            highlight = new GradientPaint(new Point2D.Float(sizes.x + sizes.width * 3 / 4, 0), new Color(255, 255, 255, 0), new Point2D.Float(sizes.x + sizes.width, 0), new Color(255, 255, 255, 255 - shade));
            g.setPaint(highlight);
            fillRect(g, sizes.totalWidth * 2 / 3, 0, sizes.totalWidth / 3, sizes.totalHeight);
        }
    }

    /** Updates the geometry to match the current size of the component. */
    private void updateGeometry() {
        sizes.totalWidth = button.getWidth();
        sizes.totalHeight = button.getHeight();
        sizes.scale = sizes.totalWidth * 1.0f / sizes.totalHeight;
        if (sizes.scale > 1.0) sizes.scale = 1.0f / sizes.scale;
        sizes.x = 0.1f * sizes.totalWidth;
        if (sizes.x > 5f) sizes.x = 5f;
        if (sizes.x < 2f) sizes.x = 2f;
        sizes.width = sizes.totalWidth - 2 * sizes.x;
        sizes.y = 0.1f * sizes.totalHeight;
        if (sizes.y > 5f) sizes.y = 5f;
        if (sizes.y < 2f) sizes.y = 2f;
        sizes.height = sizes.totalHeight - 2 * sizes.y;
        sizes.archWidth = (int) (sizes.width / 3f);
        if (sizes.archWidth > 30f) sizes.archWidth = 30f;
        sizes.archHeight = (int) (sizes.height / 3f);
        if (sizes.archHeight > 30f) sizes.archHeight = 30f;
    }

    /** Generates the outline of the button. */
    Path2D generateOutline() {
        Path2D path = new Path2D.Float();
        float x = sizes.x;
        float y = sizes.y;
        float w = sizes.width;
        float h = sizes.height;
        float ah = sizes.archHeight;
        float aw = sizes.archWidth;
        int tw = sizes.totalWidth - 1;
        int th = sizes.totalHeight - 1;
        if (orientation == SwingConstants.EAST) {
            path.moveTo(0.0, y);
            path.lineTo(w - aw, y);
            path.quadTo(w, y, w, y + ah);
            path.lineTo(w, y + h - ah);
            path.quadTo(w, y + h, w - aw, y + h);
            path.lineTo(0.0, y + h);
            path.closePath();
        } else if (orientation == SwingConstants.WEST) {
            path.moveTo(tw, y);
            path.lineTo(tw - w + aw, y);
            path.quadTo(tw - w, y, tw - w, y + ah);
            path.lineTo(tw - w, y + h - ah);
            path.quadTo(tw - w, y + h, tw - w + aw, y + h);
            path.lineTo(tw, y + h);
            path.closePath();
        } else if (orientation == SwingConstants.SOUTH) {
            path.moveTo(x, 0.0);
            path.lineTo(x, h - ah);
            path.quadTo(x, h, x + aw, h);
            path.lineTo(x + w - aw, h);
            path.quadTo(x + w, h, x + w, h - ah);
            path.lineTo(x + w, 0.0);
            path.closePath();
        } else if (orientation == SwingConstants.NORTH) {
            path.moveTo(x, th);
            path.lineTo(x, th - h + ah);
            path.quadTo(x, th - h, x + aw, th - h);
            path.lineTo(x + w - aw, th - h);
            path.quadTo(x + w, th - h, x + w, th - h + ah);
            path.lineTo(x + w, th);
            path.closePath();
        }
        return path;
    }

    Path2D generateArrow() {
        Path2D path = new Path2D.Float();
        float x = sizes.x;
        float y = sizes.y;
        float w = sizes.width;
        float h = sizes.height;
        float cy = sizes.height / 2f;
        float cx = sizes.width / 2f;
        float s = w < h ? w : h;
        float ah = 0f;
        float aw = 0f;
        switch(orientation) {
            case SwingConstants.EAST:
                cy += sizes.y;
                ah = s / 2.2f;
                aw = s / 1.8f;
                path.moveTo(cx - aw / 2, cy - ah / 2);
                path.lineTo(cx + aw / 2, cy);
                path.lineTo(cx - aw / 2, cy + ah / 2);
                path.closePath();
                break;
            case SwingConstants.WEST:
                cy += sizes.y;
                cx += sizes.totalWidth - sizes.width;
                ah = s / 2.2f;
                aw = s / 1.8f;
                path.moveTo(cx + aw / 2, cy - ah / 2);
                path.lineTo(cx - aw / 2, cy);
                path.lineTo(cx + aw / 2, cy + ah / 2);
                path.closePath();
                break;
            case SwingConstants.SOUTH:
                cx += sizes.x;
                ah = s / 1.8f;
                aw = s / 2.2f;
                path.moveTo(cx - aw / 2, cy - ah / 2);
                path.lineTo(cx, cy + ah / 2);
                path.lineTo(cx + aw / 2, cy - ah / 2);
                path.closePath();
                break;
            case SwingConstants.NORTH:
                cx += sizes.x;
                cy += sizes.totalHeight - sizes.height;
                ah = s / 1.8f;
                aw = s / 2.2f;
                path.moveTo(cx - aw / 2, cy + ah / 2);
                path.lineTo(cx, cy - ah / 2);
                path.lineTo(cx + aw / 2, cy + ah / 2);
                path.closePath();
        }
        return path;
    }
}
