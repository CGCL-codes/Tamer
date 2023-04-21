package jf.exam.paint.graphics.obj;

import java.awt.Graphics;

public class CircleRect extends PaintLine {

    /**
 * CircleRect constructor comment.
 */
    public CircleRect() {
        super();
    }

    /**
 * Creation date: (2001-11-21 10:13:37)
 */
    public void draw(java.awt.Graphics g) {
        g.setColor(color);
        int ax1 = (startP.getX() > endP.getX()) ? startP.getX() : endP.getX();
        int ax2 = (startP.getX() < endP.getX()) ? startP.getX() : endP.getX();
        int ay1 = (startP.getY() > endP.getY()) ? startP.getY() : endP.getY();
        int ay2 = (startP.getY() < endP.getY()) ? startP.getY() : endP.getY();
        int w = ax1 - ax2;
        int h = ay1 - ay2;
        int arcLen = 16;
        drawCRect(g, ax2, ay2, ax1, ay1);
    }

    /**
 * Creation date: (2001-12-12 15:23:07)
 * @param x int
 * @param y int
 * @param w int
 * @param h int
 * @param startAngle int
 * @param endAngle int
 */
    private void drawCArc(Graphics g, int x, int y, int w, int h, int startAngle, int endAngle) {
        int myx1, myy1, myx2, myy2;
        for (int i = startAngle / 10; i < endAngle / 10; i++) {
            myx1 = x + w / 2 + (int) (((double) Math.abs(w) * Math.cos((3.14159265D * (double) i) / 18D)) / 2D);
            myy1 = y + h / 2 + (int) (((double) Math.abs(h) * Math.sin((3.14159265D * (double) i) / 18D)) / 2D);
            myx2 = x + w / 2 + (int) (((double) Math.abs(w) * Math.cos((3.14159265D * (double) (i + 1)) / 18D)) / 2D);
            myy2 = y + h / 2 + (int) (((double) Math.abs(h) * Math.sin((3.14159265D * (double) (i + 1)) / 18D)) / 2D);
            drawPenLine(g, myx1, myy1, myx2, myy2);
        }
    }

    /**
 * Creation date: (2001-11-23 14:44:41)
 * @param g java.awt.Graphics
 * @param ax1 int
 * @param ay1 int
 * @param ax2 int
 * @param ay2 int
 */
    private void drawCRect(java.awt.Graphics g, int ax2, int ay2, int ax1, int ay1) {
        int w = ax1 - ax2;
        int h = ay1 - ay2;
        int minArcLen = 16;
        int minHalfArcLen = minArcLen / 2;
        drawCArc(g, ax2, ay2, minArcLen, minArcLen, 180, 270);
        drawCArc(g, ax1 - minArcLen, ay2, minArcLen, minArcLen, 270, 360);
        drawCArc(g, ax1 - minArcLen, ay1 - minArcLen, minArcLen, minArcLen, 0, 90);
        drawCArc(g, ax2, ay1 - minArcLen, minArcLen, minArcLen, 90, 180);
        drawPenLine(g, ax2 + minHalfArcLen, ay2, ax1 - minHalfArcLen, ay2);
        drawPenLine(g, ax1, ay2 + minHalfArcLen, ax1, ay1 - minHalfArcLen);
        drawPenLine(g, ax2 + minHalfArcLen, ay1, ax1 - minHalfArcLen, ay1);
        drawPenLine(g, ax2, ay2 + minHalfArcLen, ax2, ay1 - minHalfArcLen);
    }

    /**
 * set end point
 * Creation date: (2001-11-20 10:39:44)
 * @return jf.exam.paint.graphics.obj.Point
 */
    public Point getEndP() {
        return endP;
    }

    /**
 * return maximum unmber of draw
 * Creation date: (2001-11-20 14:27:18)
 * @param num int
 */
    public int getMaxNumOfDraw() {
        return maxNumOfDraw;
    }

    /**
 * return mouse position during drawing 
 * Creation date: (2001-11-20 13:39:19)
 * @return jf.exam.paint.graphics.obj.Point
 */
    public Point getMiddleP() {
        return this.endP;
    }

    /**
 * Creation date: (2001-11-20 14:27:18)
 * @param num int
 */
    public int getNumOfDraw() {
        return currNumOfDraw;
    }

    /**
 * Creation date: (2001-11-20 10:39:44)
 * @return jf.exam.paint.graphics.obj.Point
 */
    public Point getStartP() {
        return startP;
    }

    /**
 * Creation date: (2001-11-20 10:39:44)
 * @param newEndP jf.exam.paint.graphics.obj.Point
 */
    public void setEndP(Point newEndP) {
        endP = newEndP;
    }

    /**
 * Creation date: (2001-11-20 13:39:50)
 * @param p java.awt.Point
 */
    public void setMiddleP(Point p) {
        endP = p;
    }

    /**
 * Creation date: (2001-11-20 14:27:18)
 * @param num int
 */
    public void setNumOfDraw(int num) {
        this.currNumOfDraw = num;
    }

    /**
 * Creation date: (2001-11-20 10:39:44)
 * @param newStartP jf.exam.paint.graphics.obj.Point
 */
    public void setStartP(Point newStartP) {
        dim = getRootNode().getResource().getPenDimension();
        color = getRootNode().getResource().getPaintColor();
        startP = newStartP;
    }
}
