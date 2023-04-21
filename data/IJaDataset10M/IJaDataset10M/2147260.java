package physics;

import java.io.Serializable;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

/** * Circle is an immutable abstract data type which models the * mathematical notion of a circle in cartesian space. */
public final class Circle implements Serializable {

    private final Vect centerPoint;

    private final double radius;

    /**   * @requires <code>r</code> >= 0 and <code>center</code> != null   *   * @effects Creates a new circle with the specified size and location.   *   * @param center the center point of the circle   * @param r the radius of the circle   */
    public Circle(Vect center, double r) {
        if ((r < 0) || (center == null)) {
            throw new IllegalArgumentException();
        }
        centerPoint = center;
        radius = r;
    }

    /**   * @requires <code>r</code> >= 0   *   * @effects Creates a new circle with the specified size and location.   *   * @param cx the x coordinate of the center point of the circle   * @param cy the y coordinate of the center point of the circle   * @param r the radius of the circle   */
    public Circle(double cx, double cy, double r) {
        this(new Vect(cx, cy), r);
    }

    /**   * @requires <code>r</code> >= 0 and <code>center</code> != null   *   * @effects Creates a new circle with the specified size and location.   *   * @param center the center point of the circle   * @param r the radius of the circle   */
    public Circle(Point2D center, double r) {
        this(new Vect(center), r);
    }

    /**   * @return the center point of this circle.   */
    public Vect getCenter() {
        return centerPoint;
    }

    /**   * @return the radius of this circle.   */
    public double getRadius() {
        return radius;
    }

    /**   * @return a new Ellipse2D which is the same as this circle   */
    public Ellipse2D toEllipse2D() {
        return new Ellipse2D.Double(centerPoint.x() - radius, centerPoint.y() - radius, 2 * radius, 2 * radius);
    }

    public boolean equals(Circle c) {
        if (c == null) return false;
        return (radius == c.radius) && centerPoint.equals(c.centerPoint);
    }

    public boolean equals(Object o) {
        if (o instanceof Circle) return equals((Circle) o); else return false;
    }

    public String toString() {
        return "[Circle center=" + centerPoint + " radius=" + radius + "]";
    }

    public int hashCode() {
        return centerPoint.hashCode() + 17 * (new Double(radius)).hashCode();
    }
}
