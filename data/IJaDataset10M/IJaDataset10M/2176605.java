package org.eoti.swing.paint;

import org.eoti.awt.ColorUtil;
import org.eoti.awt.WebColor;
import java.awt.*;

public abstract class ShapePainter {

    protected Component source;

    protected Shape shape;

    protected Color foreground, background, highlight, shadow;

    public ShapePainter() {
    }

    public void setSource(Component source) {
        this.source = source;
    }

    public Component getSource() {
        return source;
    }

    public ShapePainter withSource(Component source) {
        setSource(source);
        return this;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Shape getShape() {
        return shape;
    }

    public ShapePainter withShape(Shape shape) {
        setShape(shape);
        return this;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getForeground() {
        if (foreground != null) return foreground;
        if (source != null) return source.getForeground();
        return WebColor.Black.getColor();
    }

    public ShapePainter withForeground(Color foreground) {
        setForeground(foreground);
        return this;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getBackground() {
        if (background != null) return background;
        if (source != null) return source.getBackground();
        return WebColor.CornSilk.getColor();
    }

    public ShapePainter withBackground(Color background) {
        setBackground(background);
        return this;
    }

    public void setHighlight(Color highlight) {
        this.highlight = highlight;
    }

    public Color getHighlight() {
        if (highlight != null) return highlight;
        return getBackground().brighter();
    }

    public ShapePainter withHighlight(Color highlight) {
        setHighlight(highlight);
        return this;
    }

    public void setShadow(Color shadow) {
        this.shadow = shadow;
    }

    public Color getShadow() {
        if (shadow != null) return shadow;
        return getBackground().darker();
    }

    public ShapePainter withShadow(Color shadow) {
        setShadow(shadow);
        return this;
    }

    public void paint(Graphics g) {
        if (shape == null) return;
        paint(g, shape, source, shape.getBounds().x, shape.getBounds().y, shape.getBounds().width, shape.getBounds().height);
    }

    public void paint(Graphics g, int x, int y) {
        if (shape == null) return;
        paint(g, shape, source, x, y, shape.getBounds().width, shape.getBounds().height);
    }

    public void paint(Graphics g, int x, int y, int width, int height) {
        paint(g, shape, source, x, y, width, height);
    }

    public void paint(Graphics g, Shape shape) {
        if (shape == null) return;
        paint(g, shape, source, shape.getBounds().x, shape.getBounds().y, shape.getBounds().width, shape.getBounds().height);
    }

    public void paint(Graphics g, Shape shape, int x, int y) {
        if (shape == null) return;
        paint(g, shape, source, x, y, shape.getBounds().width, shape.getBounds().height);
    }

    public void paint(Graphics g, Shape shape, int x, int y, int width, int height) {
        paint(g, shape, source, x, y, width, height);
    }

    public void paint(Graphics g, Component source) {
        if (shape == null) return;
        paint(g, shape, source, shape.getBounds().x, shape.getBounds().y, shape.getBounds().width, shape.getBounds().height);
    }

    public void paint(Graphics g, Component source, int x, int y) {
        if (shape == null) return;
        paint(g, shape, source, x, y, shape.getBounds().width, shape.getBounds().height);
    }

    public void paint(Graphics g, Component source, Shape shape) {
        if (shape == null) return;
        paint(g, shape, source, shape.getBounds().x, shape.getBounds().y, shape.getBounds().width, shape.getBounds().height);
    }

    public void paint(Graphics g, Component source, int x, int y, int width, int height) {
        paint(g, shape, source, x, y, width, height);
    }

    public void paint(Graphics g, Component source, Shape shape, int x, int y) {
        if (shape == null) return;
        paint(g, shape, source, x, y, shape.getBounds().width, shape.getBounds().height);
    }

    public abstract void paint(Graphics g, Shape shape, Component source, int x, int y, int width, int height);
}
