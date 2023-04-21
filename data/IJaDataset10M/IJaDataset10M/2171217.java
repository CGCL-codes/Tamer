package org.rosuda.javaGD.primitives;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class GDLine extends GDObject {

    private static final long serialVersionUID = -3218720078775754466L;

    double x1, y1, x2, y2;

    public GDLine(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void paint(Graphics graphics, GDState state) {
        if (state.col != null) {
            Graphics2D g = (Graphics2D) graphics;
            g.draw(new Line2D.Double(x1, y1, x2, y2));
        }
    }
}
