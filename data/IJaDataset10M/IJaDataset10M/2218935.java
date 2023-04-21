package org.fudaa.dodico.hydraulique1d.metier.geometrie;

/**
 * Une classe point 2D
 */
public class MetierPoint2D {

    public double x = (double) 0;

    public double y = (double) 0;

    public double cx = (double) 0;

    public double cy = (double) 0;

    public MetierPoint2D() {
    }

    public MetierPoint2D(double _x, double _y) {
        x = _x;
        y = _y;
        cx = 0;
        cy = 0;
    }

    public MetierPoint2D(double _x, double _y, double _cx, double _cy) {
        x = _x;
        y = _y;
        cx = _cx;
        cy = _cy;
    }
}
