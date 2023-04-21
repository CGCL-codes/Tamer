package net.sf.nanji.logic.math;

/**
 * Dimension class with double x and y.
 * 
 * @author Bernhard Grünewaldt
 *
 */
public class DoubleDimension {

    private double x;

    private double y;

    public DoubleDimension(double xi, double yi) {
        this.x = xi;
        this.y = yi;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
