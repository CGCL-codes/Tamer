package fr.inria.zvtm.engine;

/**
   * similar to java.awt.Point but uses long instead of int
   * @author Emmanuel Pietriga
   **/
public class LongPoint {

    /**X coordinate*/
    public long x;

    /**Y coordinate*/
    public long y;

    public LongPoint() {
        x = 0;
        y = 0;
    }

    public LongPoint(long xc, long yc) {
        x = xc;
        y = yc;
    }

    public LongPoint(double xc, double yc) {
        x = Math.round(xc);
        y = Math.round(yc);
    }

    public void setLocation(long xc, long yc) {
        this.x = xc;
        this.y = yc;
    }

    public void translate(long dx, long dy) {
        this.x += dx;
        this.y += dy;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
