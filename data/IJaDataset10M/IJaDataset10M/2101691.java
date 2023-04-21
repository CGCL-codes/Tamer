package com.horstmann.violet.workspace.editorpart;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.product.diagram.abstracts.IGraph;

/**
 * A grid to which points and rectangles can be "snapped". The snapping operation moves a point to the nearest grid point.
 */
public class PlainGrid implements IGrid {

    /**
     * Constructs a grid with its grid size.
     * 
     * @param snappingWidth the grid point distance in x-direction
     * @param snappingHeight the grid point distance in y-direction
     */
    public PlainGrid(IEditorPart editorPart) {
        this.editorPart = editorPart;
        this.snappingWidth = DEFAULT_GRID_SIZE;
        this.snappingHeight = DEFAULT_GRID_SIZE;
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public boolean isVisible() {
        return this.isVisible;
    }

    @Override
    public void changeGridSize(int steps) {
        final double FACTOR = Math.sqrt(Math.sqrt(2));
        for (int i = 1; i <= steps; i++) {
            snappingWidth *= FACTOR;
            snappingHeight *= FACTOR;
        }
        for (int i = 1; i <= -steps; i++) {
            snappingWidth /= FACTOR;
            snappingHeight /= FACTOR;
        }
    }

    /**
     * @return the grid size according to the graph size, the editor size and its zoom factor (scaleX and scaleY)
     */
    private Rectangle2D.Double getBounds(Graphics2D g2) {
        Component editorPartComponent = editorPart.getSwingComponent();
        IGraph graph = editorPart.getGraph();
        Rectangle2D bounds = editorPartComponent.getBounds();
        Rectangle2D graphBounds = graph.getClipBounds();
        double scaleX = 1;
        double scaleY = 1;
        AffineTransform transform = g2.getTransform();
        if (transform != null) {
            scaleX = transform.getScaleX();
            scaleY = transform.getScaleY();
        }
        return new Rectangle2D.Double(0, 0, Math.max(bounds.getMaxX() / scaleX, graphBounds.getMaxX()), Math.max(bounds.getMaxY() / scaleY, graphBounds.getMaxY()));
    }

    @Override
    public void paint(Graphics2D g2) {
        if (snappingWidth == 0 || snappingHeight == 0) return;
        Color oldColor = g2.getColor();
        g2.setColor(ThemeManager.getInstance().getTheme().getGridColor());
        Stroke oldStroke = g2.getStroke();
        Rectangle2D.Double bounds = getBounds(g2);
        for (double x = bounds.getX(); x < bounds.getMaxX(); x += snappingWidth) g2.draw(new Line2D.Double(x, bounds.getY(), x, bounds.getMaxY()));
        for (double y = bounds.getY(); y < bounds.getMaxY(); y += snappingHeight) g2.draw(new Line2D.Double(bounds.getX(), y, bounds.getMaxX(), y));
        g2.setStroke(oldStroke);
        g2.setColor(oldColor);
    }

    @Override
    public Point2D snap(Point2D p) {
        double x;
        if (snappingWidth == 0) x = p.getX(); else x = Math.round(p.getX() / snappingWidth) * snappingWidth;
        double y;
        if (snappingHeight == 0) y = p.getY(); else y = Math.round(p.getY() / snappingHeight) * snappingHeight;
        p.setLocation(x, y);
        return p;
    }

    @Override
    public Rectangle2D snap(Rectangle2D r) {
        double x;
        double w;
        if (snappingWidth == 0) {
            x = r.getX();
            w = r.getWidth();
        } else {
            x = Math.round(r.getX() / snappingWidth) * snappingWidth;
            w = Math.ceil(r.getWidth() / (2 * snappingWidth)) * (2 * snappingWidth);
        }
        double y;
        double h;
        if (snappingHeight == 0) {
            y = r.getY();
            h = r.getHeight();
        } else {
            y = Math.round(r.getY() / snappingHeight) * snappingHeight;
            h = Math.ceil(r.getHeight() / (2 * snappingHeight)) * (2 * snappingHeight);
        }
        r.setFrame(x, y, w, h);
        return r;
    }

    @Override
    public double getSnappingWidth() {
        return snappingWidth;
    }

    @Override
    public double getSnappingHeight() {
        return snappingHeight;
    }

    private double snappingWidth;

    private double snappingHeight;

    private boolean isVisible = true;

    public static final int DEFAULT_GRID_SIZE = 10;

    private IEditorPart editorPart;
}
