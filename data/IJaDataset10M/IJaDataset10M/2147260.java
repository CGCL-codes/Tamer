package physics;

import java.io.Serializable;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

/**
public final class Circle implements Serializable {

    private final Vect centerPoint;

    private final double radius;

    /**
    public Circle(Vect center, double r) {
        if ((r < 0) || (center == null)) {
            throw new IllegalArgumentException();
        }
        centerPoint = center;
        radius = r;
    }

    /**
    public Circle(double cx, double cy, double r) {
        this(new Vect(cx, cy), r);
    }

    /**
    public Circle(Point2D center, double r) {
        this(new Vect(center), r);
    }

    /**
    public Vect getCenter() {
        return centerPoint;
    }

    /**
    public double getRadius() {
        return radius;
    }

    /**
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