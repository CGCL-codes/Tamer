package org.apache.lucene.spatial.geometry;

/**
 * Represents lat/lngs as fixed point numbers translated so that all
 * world coordinates are in the first quadrant.  The same fixed point
 * scale as is used for FixedLatLng is employed.
 */
public class CartesianPoint {

    private int x;

    private int y;

    public CartesianPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point(" + x + "," + y + ")";
    }

    /**
   * Return a new point translated in the x and y dimensions
   * @param i
   * @param translation
   * @return
   */
    public CartesianPoint translate(int deltaX, int deltaY) {
        return new CartesianPoint(this.x + deltaX, this.y + deltaY);
    }
}
