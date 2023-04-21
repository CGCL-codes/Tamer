package theba.core;

public class Vector3D {

    public double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(Point3D pt) {
        this.x = pt.x;
        this.y = pt.y;
        this.z = pt.z;
    }

    public String toString() {
        return (float) x + "," + (float) y + "," + (float) z;
    }

    public double distanceFrom(Point3D p2) {
        double a = x - p2.x;
        double b = y - p2.y;
        double c = z - p2.z;
        return (Math.sqrt(a * a + b * b + c * c));
    }

    public void normalize() {
        double length = distanceFrom(new Point3D(0, 0, 0));
        x /= length;
        y /= length;
        z /= length;
    }
}
