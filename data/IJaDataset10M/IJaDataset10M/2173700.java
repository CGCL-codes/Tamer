package org.eoti.swing.panels;

import org.eoti.swing.paint.ShapePainter;
import org.eoti.swing.paint.FillShapePainter;
import org.eoti.swing.paint.StrokedShapeBorder;
import java.awt.*;

public abstract class ShapedPanel extends ShadowedPanel {

    protected ShapePainter compPainter, borderPainter;

    protected Shape shape = null;

    public ShapedPanel() {
        super();
        compPainter = new FillShapePainter();
        compPainter.setSource(this);
        borderPainter = new StrokedShapeBorder().withThickness(1f);
        borderPainter.setSource(this);
    }

    public void setShadowDepth(int depth) {
        super.setShadowDepth(depth);
        initShape();
    }

    public void setAlphaTransparency(int alpha) {
        super.setAlphaTransparency(alpha);
        initShape();
    }

    public void setComponentPainter(ShapePainter compPainter) {
        this.compPainter = compPainter;
    }

    public ShapePainter getComponentPainter() {
        return compPainter;
    }

    public void paintComponent(Graphics g) {
        if (shape == null) initShape();
        if (compPainter != null) compPainter.paint(g, shape);
    }

    public void setBorderPainter(ShapePainter borderPainter) {
        this.borderPainter = borderPainter;
    }

    public ShapePainter getBorderPainter() {
        return borderPainter;
    }

    public void paintBorder(Graphics g) {
        if (shape == null) initShape();
        if (borderPainter != null) borderPainter.paint(g, shape);
    }

    protected abstract Shape createShape(int x, int y, int width, int height);

    public Shape getShape() {
        return shape;
    }

    protected void initShape() {
        int offset = (hasShadow() ? depth : 0);
        shape = createShape(0, 0, getWidth() - offset - 1, getHeight() - offset - 1);
    }

    public void setBounds(Rectangle r) {
        super.setBounds(r);
        initShape();
    }

    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        initShape();
    }
}
