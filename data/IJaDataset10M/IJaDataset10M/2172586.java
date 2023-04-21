package org.isakiev.wic.core.surface;

import java.io.Serializable;

/**
 * Represents coordinates of the point on sufrace
 * 
 * @author Ruslan Isakiev
 */
class Point implements Serializable {

    private static final long serialVersionUID = 1L;

    private int x;

    private int y;

    public Point(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Point) {
            Point other = (Point) obj;
            return (x == other.x) && (y == other.y);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return x * y;
    }
}
