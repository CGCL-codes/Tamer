package org.vikamine.swing.subgroup.visualization.graph;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.border.AbstractBorder;

/**
 * @author Tobias Vogele
 */
public class ArrowBorder extends AbstractBorder {

    /**
     * 
     */
    private static final long serialVersionUID = 4211082487895319704L;

    public static final int INSETS = 10;

    public static final int ARROWSIZE = 10;

    public ArrowBorder() {
        super();
    }

    @Override
    public void paintBorder(Component comp, Graphics g, int x, int y, int width, int height) {
        JComponent c = (JComponent) comp;
        Boolean b = (Boolean) c.getClientProperty(GraphLayout.EDGE_LEFTTORIGHT);
        boolean ltr = (b == null) || (b.booleanValue());
        Rectangle inside = getInteriorRectangle(c, x, y, width, height);
        double angle = Math.atan(inside.getHeight() / inside.getWidth());
        if (!ltr) {
            angle = Math.PI - angle;
        }
        Point edgeEnd = getEdgeEnd(c, ltr);
        Graphics2D g2d = (Graphics2D) g;
        Object oldHint = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(edgeEnd.x, edgeEnd.y);
        g2d.rotate(angle);
        int size = ARROWSIZE;
        if (comp instanceof EdgeComponent) {
            size += ((EdgeComponent) comp).getEdge().getWidth();
        }
        Polygon p = new Polygon();
        p.addPoint(size, 0);
        p.addPoint(0, size / 2);
        p.addPoint(0, -size / 2);
        g.setColor(c.getForeground());
        g.fillPolygon(p);
        g2d.rotate(-angle);
        g.translate(-edgeEnd.x, -edgeEnd.y);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
    }

    private Point getEdgeEnd(JComponent c, boolean ltr) {
        Insets insets = c.getInsets();
        int x = insets.left;
        int y = insets.top;
        int w = c.getWidth() - insets.left - insets.right - 1;
        int h = c.getHeight() - insets.top - insets.bottom - 1;
        int edgeX = ltr ? (x + w) : x;
        return new Point(edgeX, y + h);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int space = ARROWSIZE + INSETS;
        return new Insets(space, space, space, space);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = insets.left = insets.right = insets.bottom = ARROWSIZE + INSETS;
        return insets;
    }
}
