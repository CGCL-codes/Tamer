package util;

/**
 * Bresenham's famous line drawing algorithm. Works for 2D.
 */
public final class BresenhamLine {

    /** The start and end of the line */
    private int x1, y1, x2, y2;

    /** Used for calculation */
    private int dx, dy, error, x_inc, y_inc, xx, yy, length, count;

    /** General case algorithm */
    private static final BresenhamLine bresenham = new BresenhamLine();

    /**
	 * Construct a Bresenham algorithm.
	 */
    public BresenhamLine() {
    }

    /**
	 * Plot a line between (x1,y1) and (x2,y2). To step through the line use next().
	 * @return the length of the line (which will be 1 more than you are expecting).
	 */
    public int plot(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        dx = x2 - x1;
        dy = y2 - y1;
        if (dx >= 0) {
            x_inc = 1;
        } else {
            x_inc = -1;
            dx = -dx;
        }
        if (dy >= 0) {
            y_inc = 1;
        } else {
            y_inc = -1;
            dy = -dy;
        }
        xx = x1;
        yy = y1;
        if (dx > dy) {
            error = dx >> 1;
            length = dx + 1;
        } else {
            error = dy >> 1;
            length = dy + 1;
        }
        count = 0;
        return length;
    }

    /**
	 * Get the next point in the line. You must not call next() if the
	 * previous invocation of next() returned false.
	 * 
	 * Retrieve the X and Y coordinates of the line with getX() and getY().
	 * 
	 * @return true if there is another point to come.
	 */
    public boolean next() {
        if (dx > dy) {
            error += dy;
            if (error >= dx) {
                error -= dx;
                yy += y_inc;
            }
            xx += x_inc;
        } else {
            error += dx;
            if (error >= dy) {
                error -= dy;
                xx += x_inc;
            }
            yy += y_inc;
        }
        count++;
        return count < length;
    }

    /**
	 * @return the current X coordinate
	 */
    public int getX() {
        return xx;
    }

    /**
	 * @return the current Y coordinate
	 */
    public int getY() {
        return yy;
    }

    /**
	 * Plot a line between (x1,y1) and (x2,y2). The results are placed in x[] and y[], which must be large enough.
	 * @return the length of the line or the length of x[]/y[], whichever is smaller
	 */
    public static final int plot(final int x1, final int y1, final int x2, final int y2, final int x[], final int y[]) {
        int length = Math.min(x.length, Math.min(y.length, bresenham.plot(x1, y1, x2, y2)));
        for (int i = 0; i < length; i++) {
            x[i] = bresenham.getX();
            y[i] = bresenham.getY();
            bresenham.next();
        }
        return length;
    }
}
