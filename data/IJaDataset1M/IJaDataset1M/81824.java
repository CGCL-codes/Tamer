package com.peterhi.persist.beans.shapes;

/**
 *
 * @author YUN TAO
 */
public class Polygon extends Shape {

    public int[] xs;

    public int[] ys;

    @Override
    protected Shape cloneNewInstance() {
        Polygon polygon = new Polygon();
        if (xs != null) {
            polygon.xs = new int[xs.length];
            System.arraycopy(xs, 0, polygon.xs, 0, polygon.xs.length);
        }
        if (ys != null) {
            polygon.ys = new int[ys.length];
            System.arraycopy(ys, 0, polygon.ys, 0, polygon.ys.length);
        }
        return polygon;
    }
}
