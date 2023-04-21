package connex.plugins.whiteboard;

import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.BasicStroke;

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
class LineObj extends ShapeObj {

    public void draw() {
        if (start == null || end == null) {
            return;
        }
        draw(start, end);
    }

    public void draw(Point start, Point end) {
        this.start = start;
        this.end = end;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (select != 0) {
            drawSelection();
        } else {
            g2D.setColor(drawColor);
            g2D.drawLine(start.x, start.y, end.x, end.y);
        }
        lastS = new Point(start);
        lastE = new Point(end);
    }

    public void fill(Color fillColor) {
        drawColor = fillColor;
        g2D.setColor(fillColor);
        draw();
    }

    protected void drawSelection() {
        BasicStroke stroke = new BasicStroke(1.0f);
        float dash1[] = { 2.0f };
        BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
        g2D.setStroke(dashed);
        if (lastS != null) {
            if (!lastE.equals(end) || !lastS.equals(start)) {
                g2D.setColor(Color.MAGENTA);
                g2D.setXORMode(Color.white);
                g2D.drawLine(lastS.x, lastS.y, lastE.x, lastE.y);
                g2D.fillRect(lastS.x - 2, lastS.y - 2, 5, 5);
                g2D.fillRect(lastE.x - 2, lastE.y - 2, 5, 5);
            }
        }
        g2D.setColor(Color.MAGENTA);
        g2D.drawLine(start.x, start.y, end.x, end.y);
        g2D.fillRect(start.x - 2, start.y - 2, 5, 5);
        g2D.fillRect(end.x - 2, end.y - 2, 5, 5);
    }

    public boolean SelectShape(Point pt) {
        double px = pt.x;
        double py = pt.y;
        if (px - start.x < 5 && px - start.x > -5 && py - start.y < 5 && py - start.y > -5) {
            this.select = 1;
            return true;
        }
        if (px - end.x < 5 && px - end.x > -5 && py - end.y < 5 && py - end.y > -5) {
            select = 2;
            return true;
        }
        if (new Line2D.Double(start, end).ptLineDist(pt) <= 2) {
            select = 3;
            return true;
        }
        return false;
    }

    public void move(Point pt, Point pt2) {
        int tmpx = pt.x - pt2.x;
        int tmpy = pt.y - pt2.y;
        if (select == 1) {
            start.setLocation(pt);
        }
        if (select == 2) {
            end.setLocation(pt);
        }
        if (select == 3) {
            start.x += tmpx;
            start.y += tmpy;
            end.x += tmpx;
            end.y += tmpy;
        }
        draw(start, end);
    }
}
