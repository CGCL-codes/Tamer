package jecl.engines;

/**
 * Discrete Velocity
 */
public class Velocity {

    Integer x;

    Integer y;

    public Velocity(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public static Integer add(Integer p, Velocity v) {
        if (v.x == p) return new Integer(v.y); else return new Integer(p);
    }

    public static Velocity add(Velocity a, Velocity b) {
        if (a.y == b.x) return new Velocity(a.x, b.y); else return new Velocity(a.x, a.y);
    }

    public static Velocity subtract(Integer p, Integer q) {
        return new Velocity(q, p);
    }

    public static Velocity times(double c, Velocity v) {
        if (c > 1) c = c - Math.floor(c);
        if (Math.random() <= c) return new Velocity(v.x, v.x); else return new Velocity(v.x, v.y);
    }
}
