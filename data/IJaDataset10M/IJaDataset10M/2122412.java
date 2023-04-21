package be.gnx.dukono.ogc.geometry;

public abstract class Curve extends Geometry {

    public abstract double length();

    public abstract Point startPoint();

    public abstract Point endPoint();

    public boolean isClosed() {
        return startPoint().equals(endPoint());
    }

    public boolean isRing() {
        return isClosed() && isSimple();
    }
}
