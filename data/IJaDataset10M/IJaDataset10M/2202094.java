package org.das2.event;

import org.das2.graph.DasCanvasComponent;
import java.awt.*;

/**
 *
 * @author  eew
 */
public class HorizontalDragRangeRenderer implements DragRenderer {

    private Rectangle dirtyBounds;

    DasCanvasComponent parent;

    boolean updating;

    public HorizontalDragRangeRenderer(DasCanvasComponent parent) {
        this.parent = parent;
        dirtyBounds = new Rectangle();
        updating = true;
    }

    public HorizontalDragRangeRenderer(DasCanvasComponent parent, boolean updating) {
        this(parent);
        this.updating = updating;
    }

    public Rectangle[] renderDrag(Graphics g1, Point p1, Point p2) {
        Graphics2D g = (Graphics2D) g1;
        int x2 = p2.x;
        int x1 = p1.x;
        if (x2 < x1) {
            int t = x2;
            x2 = x1;
            x1 = t;
        }
        int width = x2 - x1;
        int y = p2.y;
        Color color0 = g.getColor();
        g.setColor(new Color(255, 255, 255, 100));
        g.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        if (width > 6) g.drawLine(x1 + 3, y, x2 - 3, y);
        g.drawLine(x1, y + 2, x1, y - 2);
        g.drawLine(x2, y + 2, x2, y - 2);
        g.setStroke(new BasicStroke());
        g.setColor(color0);
        if (width > 6) g.drawLine(x1 + 3, y, x2 - 3, y);
        g.drawLine(x1, y + 2, x1, y - 2);
        g.drawLine(x2, y + 2, x2, y - 2);
        dirtyBounds.setLocation(x1 - 2, y + 3);
        dirtyBounds.add(x2 + 2, y - 3);
        return new Rectangle[] { dirtyBounds };
    }

    public MouseDragEvent getMouseDragEvent(Object source, Point p1, Point p2, boolean isModified) {
        MouseRangeSelectionEvent me = new MouseRangeSelectionEvent(source, p1.x, p2.x, isModified);
        return me;
    }

    public void clear(Graphics g) {
        parent.paintImmediately(dirtyBounds);
    }

    public boolean isPointSelection() {
        return true;
    }

    public boolean isUpdatingDragSelection() {
        return updating;
    }
}
