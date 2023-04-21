package net.sf.layoutdesigner.track;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public abstract class AbstractTrack implements Track {

    @Override
    public void translate(int dx, int dy) {
        translate((double) dx, (double) dy);
    }

    @Override
    public void rotate(double theta, int cx, int cy) {
        rotate(theta, (double) cx, (double) cy);
    }

    protected abstract void transform(AffineTransform at);

    @Override
    public void translate(double dx, double dy) {
        AffineTransform at = AffineTransform.getTranslateInstance(dx, dy);
        transform(at);
    }

    @Override
    public void rotate(double theta, double cx, double cy) {
        AffineTransform at = AffineTransform.getRotateInstance(theta, cx, cy);
        transform(at);
    }

    protected abstract void drawRoadbed(Graphics2D g2);

    protected abstract void drawTies(Graphics2D g2);

    protected abstract void drawRails(Graphics2D g2);

    @Override
    public void draw(Graphics2D g2) {
        Color saveColor = g2.getColor();
        Stroke saveStroke = g2.getStroke();
        Color color = new Color(96, 96, 96);
        g2.setColor(color);
        BasicStroke stroke = new BasicStroke(18.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
        g2.setStroke(stroke);
        drawRoadbed(g2);
        color = new Color(102, 51, 51);
        g2.setColor(color);
        stroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
        g2.setStroke(stroke);
        drawTies(g2);
        color = new Color(192, 192, 192);
        g2.setColor(color);
        stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
        g2.setStroke(stroke);
        drawRails(g2);
        g2.setStroke(saveStroke);
        g2.setColor(saveColor);
    }
}
