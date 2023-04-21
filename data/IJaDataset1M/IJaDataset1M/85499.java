package net.sourceforge.j_snake;

/**
 * User: Ignat Alexeyenko
 * Date: Aug 15, 2008
 * Time: 11:40:05 PM
 */
public class Dot {

    private final int x;

    private final int y;

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dot dot = (Dot) o;
        if (x != dot.x) return false;
        if (y != dot.y) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = x;
        result = 31 * result + y;
        return result;
    }

    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + "]";
    }
}
