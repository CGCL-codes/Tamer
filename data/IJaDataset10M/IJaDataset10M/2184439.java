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
public abstract class ClosedShape extends ShapeObj {

    protected int x1, x2, y1, y2;

    protected int tmpx;

    protected int tmpy;

    public ClosedShape() {
    }

    public void draw(Point start, Point end) {
        this.start = start;
        this.end = end;
        setBounds(this.start, this.end);
        draw();
        lastS = new Point(start);
        lastE = new Point(end);
    }

    protected void drawSelection() {
        BasicStroke stroke = new BasicStroke(1.0f);
        float dash1[] = { 2.0f };
        BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
        g2D.setStroke(dashed);
        if (lastS != null && lastE != null) {
            if (!lastE.equals(end) || !lastS.equals(start)) {
                selection(lastS, lastE, true);
            }
        }
        selection(this.start, this.end, false);
        g2D.setStroke(stroke);
    }

    protected void selection(Point pt1, Point pt2, boolean xor) {
        g2D.setColor(Color.magenta);
        if (xor) {
            g2D.setXORMode(Color.white);
        }
        setBounds(pt1, pt2);
        g2D.drawRect(x1, y1, x2 - x1, y2 - y1);
        g2D.fillRect(x1 - 2, y1 - 2, 5, 5);
        g2D.fillRect(x2 - 2, y1 - 2, 5, 5);
        g2D.fillRect(x1 - 2, y2 - 2, 5, 5);
        g2D.fillRect(x2 - 2, y2 - 2, 5, 5);
        shapeSelection();
    }

    /**
   * shapeSelection
   */
    protected abstract void shapeSelection();

    /**
   *
   * @param pt Point
   * @param pt2 Point
   */
    public void move(Point pt, Point pt2) {
        tmpx = (pt.x - pt2.x);
        tmpy = (pt.y - pt2.y);
        if (select == 1) {
            x1 += tmpx;
            y1 += tmpy;
            if (x1 + 2 > x2) {
                select = 2;
            }
            if (y1 > y2) {
                select = 4;
            }
        }
        if (select == 2) {
            x2 += tmpx;
            y1 += tmpy;
            if (x2 < x1) {
                select = 1;
            }
            if (y1 > y2) {
                select = 3;
            }
        }
        if (select == 3) {
            x2 += tmpx;
            y2 += tmpy;
            if (x2 < x1) {
                select = 4;
            }
            if (y1 > y2) {
                select = 2;
            }
        }
        if (select == 4) {
            x1 += tmpx;
            y2 += tmpy;
            if (x2 < x1) {
                select = 3;
            }
            if (y1 > y2) {
                select = 1;
            }
        }
        if (select == 5) {
            x2 += tmpx;
            x1 += tmpx;
            y2 += tmpy;
            y1 += tmpy;
        }
        start.setLocation(x1, y1);
        end.setLocation(x2, y2);
        draw(start, end);
    }

    void setBounds(Point pt1, Point pt2) {
        x1 = (pt1.x <= pt2.x) ? pt1.x : pt2.x;
        x2 = (pt1.x >= pt2.x) ? pt1.x : pt2.x;
        y1 = (pt1.y <= pt2.y) ? pt1.y : pt2.y;
        y2 = (pt1.y >= pt2.y) ? pt1.y : pt2.y;
    }

    public boolean SelectShape(Point pt) {
        int px = pt.x;
        int py = pt.y;
        if (px - x1 < 5 && px - x1 > -5 && py - y1 < 5 && py - y1 > -5) {
            this.select = 1;
            return true;
        }
        if (px - x2 < 5 && px - x2 > -5 && py - y1 < 5 && py - y1 > -5) {
            select = 2;
            return true;
        }
        if (px - x2 < 5 && px - x2 > -5 && py - y2 < 5 && py - y2 > -5) {
            select = 3;
            return true;
        }
        if (px - x1 < 5 && px - x1 > -5 && py - y2 < 5 && py - y2 > -5) {
            select = 4;
            return true;
        }
        if (this.contains(pt)) {
            select = 5;
            return true;
        }
        return false;
    }

    public boolean contains(Point pt) {
        if ((pt.y < y1) || (pt.y > y2)) {
            return false;
        }
        if ((pt.x < x1) || (pt.x > x2)) {
            return false;
        }
        return true;
    }
}
