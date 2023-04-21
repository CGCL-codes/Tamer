package connex.plugins.whiteboard;

import java.awt.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PolygonObj extends ShapeObj {

    private Polygon poly = new Polygon();

    public PolygonObj() {
        this.id = this.hashCode() + "";
    }

    /**
   * SelectGraphic
   *
   * @param pt Point
   * @return boolean
   * @todo Implement this connex.plugins.whiteboard.ShapeObj method
   */
    public boolean SelectShape(Point pt) {
        int n = poly.npoints;
        for (int i = 0; i < n; i++) {
            if (Math.abs(pt.x - poly.xpoints[i]) < 5 && Math.abs(pt.y - poly.ypoints[i]) < 5) {
                select = i + 1;
                return true;
            }
        }
        if (poly.contains(pt)) {
            select = 1000;
            return true;
        }
        return false;
    }

    /**
   * move
   *
   * @param pt Point
   * @param pt2 Point
   * @todo Implement this connex.plugins.whiteboard.ShapeObj method
   */
    public void move(Point pt, Point pt2) {
        int tmpx = pt.x - pt2.x;
        int tmpy = pt.y - pt2.y;
        if (select <= poly.npoints) {
            drawSelection(true);
            poly.xpoints[select - 1] = pt.x;
            poly.ypoints[select - 1] = pt.y;
            poly.invalidate();
            drawSelection(false);
        } else {
            drawSelection(true);
            poly.translate(tmpx, tmpy);
            drawSelection(false);
        }
    }

    protected void drawSelection(boolean xor) {
        BasicStroke stroke = new BasicStroke(1.0f);
        float dash1[] = { 2.0f };
        BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
        int n = poly.npoints;
        g2D.setStroke(dashed);
        g2D.setColor(Color.MAGENTA);
        if (xor) {
            g2D.setXORMode(Color.white);
        }
        g2D.draw(poly);
        for (int i = 0; i < n; i++) {
            g2D.fillRect(poly.xpoints[i] - 2, poly.ypoints[i] - 2, 5, 5);
        }
    }

    /**
   * draw
   *
   * @todo Implement this connex.plugins.whiteboard.ShapeObj method
   */
    public void draw() {
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (select != 0) {
            drawSelection(false);
        } else {
            g2D.setColor(drawColor);
            g2D.draw(poly);
        }
        if (fillColor != null) {
            fill(fillColor);
        }
    }

    /**
   * draw
   *
   * @param start Point
   * @param end Point
   * @todo Implement this connex.plugins.whiteboard.ShapeObj method
   */
    public void draw(Point start, Point end) {
        if (poly.npoints <= 1000) {
            poly.addPoint(start.x, start.y);
            draw();
        }
    }

    /**
   * fill
   *
   * @param fillColor Color
   * @todo Implement this connex.plugins.whiteboard.ShapeObj method
   */
    public void fill(Color fillColor) {
        this.fillColor = fillColor;
        g2D.setColor(fillColor);
        g2D.fill(poly);
    }
}
