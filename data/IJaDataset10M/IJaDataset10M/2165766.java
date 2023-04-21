package inertial.math.points;

import inertial.math.numbers.DecimalNumber;
import inertial.math.numbers.RealNumber;

public class CylindricalPoint extends Point3D {

    private RealNumber alpha;

    private RealNumber radius;

    private RealNumber base;

    public CylindricalPoint() {
        this(0, 0, 0);
    }

    public CylindricalPoint(double alpha, double radius, double base) {
        this(new DecimalNumber(alpha), new DecimalNumber(radius), new DecimalNumber(base));
    }

    public CylindricalPoint(RealNumber alpha, RealNumber radius, RealNumber base) {
        setAlpha(alpha);
        setRadius(radius);
        setBase(base);
        Point3D cartesianCoordinates = CoordinateTransformer.toCartesianCoordinates(this);
        setX(cartesianCoordinates.getX());
        setY(cartesianCoordinates.getY());
        setZ(cartesianCoordinates.getZ());
    }

    public CylindricalPoint(RealNumber alpha, RealNumber radius, RealNumber base, RealNumber x, RealNumber y) {
        setAlpha(alpha);
        setRadius(radius);
        setBase(base);
        setX(x);
        setY(y);
        setZ(base);
    }

    public RealNumber getAlpha() {
        return alpha;
    }

    public void setAlpha(RealNumber alpha) {
        this.alpha = alpha.copy();
    }

    public void setAlpha(double alpha) {
        this.alpha = new DecimalNumber(alpha);
    }

    public RealNumber getRadius() {
        return radius;
    }

    public void setRadius(RealNumber radius) {
        this.radius = radius.copy();
    }

    public void setRadius(double radius) {
        this.radius = new DecimalNumber(radius);
    }

    public RealNumber getBase() {
        return base;
    }

    public void setBase(RealNumber base) {
        this.base = base.copy();
    }

    public void setBase(double base) {
        this.base = new DecimalNumber(base);
    }
}
