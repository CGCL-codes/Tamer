package objects;

public class Trapezoid extends Quadrilateral {

    public double height;

    public Trapezoid(Point one, Point two, Point three, Point four) {
        super(one, two, three, four);
    }

    public double getHeight() {
        if (getPoint1().getY() == getPoint2().getY()) return Math.abs(getPoint2().getY() - getPoint3().getY()); else return Math.abs(getPoint1().getY() - getPoint2().getY());
    }

    public double getArea() {
        return getSumofSides() * getHeight() / 2.0;
    }

    public double getSumofSides() {
        if (getPoint1().getY() == getPoint2().getY()) return Math.abs(getPoint1().getX() - getPoint2().getX()) + Math.abs(getPoint3().getX() - getPoint4().getX()); else return Math.abs(getPoint2().getX() - getPoint3().getX()) + Math.abs(getPoint4().getX() - getPoint1().getX());
    }

    public String toString() {
        return String.format("\n%s:\n%s%s: %s\n%s: %s\n", "Coordinates of Trapezoid are", getCoordinatesAsString(), "Height is", getHeight(), "Area is", getArea());
    }
}
