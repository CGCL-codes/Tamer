package net.sourceforge.plantuml.geom.kinetic;

import java.awt.geom.Point2D;

public class Point2DCharge extends Point2D.Double {

    private double charge = 1.0;

    private MoveObserver moveObserver = null;

    public Point2DCharge(double x, double y) {
        super(x, y);
    }

    public Point2DCharge(Point2D pt, double ch) {
        super(pt.getX(), pt.getY());
        this.charge = ch;
    }

    public void apply(VectorForce value) {
        System.err.println("Applying " + value);
        x += value.getX();
        y += value.getY();
        if (moveObserver != null) {
            moveObserver.pointMoved(this);
        }
    }

    @Override
    public final void setLocation(double x, double y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void setLocation(Point2D p) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return System.identityHashCode(this) + " " + String.format("[%8.2f %8.2f]", x, y);
    }

    public final double getCharge() {
        return charge;
    }

    public final void setCharge(double charge) {
        this.charge = charge;
    }

    private final int hash = System.identityHashCode(this);

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    public final void setMoveObserver(MoveObserver moveObserver) {
        this.moveObserver = moveObserver;
    }
}
