package edu.semaster.figurearea.model;

public class Sphere implements I3DFigure {

    public double m_radius;

    public Sphere(double radius) {
        if (radius <= 0.0) throw new IllegalArgumentException("Negative radius not Allowed.");
        m_radius = radius;
    }

    public double getRadius() {
        return m_radius;
    }

    public double calculateArea() {
        double result = 4 * Math.PI * Math.pow(m_radius, 2);
        return result;
    }
}
