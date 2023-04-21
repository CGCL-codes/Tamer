package com.guanda.swidgex.internal;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class DropControl extends PaneControl {

    private static final long serialVersionUID = 1L;

    public DropControl() {
        this.setToolTipText("Menu");
    }

    @Override
    protected void paintMascot(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        Polygon p = new Polygon();
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        int x0 = 6;
        int y0 = 6;
        p.addPoint(x0 + 2, y);
        p.addPoint(getWidth() - x0 - 2, y);
        p.addPoint(x, getHeight() - y0 - 2);
        g.drawPolygon(p);
    }
}
