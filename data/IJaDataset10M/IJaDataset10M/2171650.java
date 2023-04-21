package meraner81.jets.shared.model;

public class Point {

    private double x = 0;

    private double y = 0;

    private double z = 0;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getZ() {
        return z;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    @Override
    public String toString() {
        return "Point(" + x + "," + y + "," + z + ")";
    }

    public String toXML() {
        return "<point x=\"" + x + "\" y=\"" + y + "\" z=\"" + z + "\"/>";
    }
}
