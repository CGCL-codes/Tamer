package br.ufrgs.f180.math;

public class Line {

    private Point p0;

    private Point p1;

    public Line(Point p0, Point p1) {
        this.p0 = p0;
        this.p1 = p1;
    }

    /**
	 * Linear interpolation to ax + by + c = 0
     * a = (y1 - y0) / (x1 - x0)
     * b = 1
     * c = y0 - (a * x0)
	 * @return a
	 */
    private double findLinearInterpolationCoheficientA() {
        return (p1.y - p0.y) / (p1.x - p0.x);
    }

    /**
	 * Linear interpolation to ax + by = c
     * a = (y1 - y0) / (x1 - x0)
     * b = 1
     * c = y0 - (a * x0)
	 * @return b
	 */
    private double findLinearInterpolationCoheficientB() {
        return 1;
    }

    /**
	 * Linear interpolation to ax + by = c
     * a = (y1 - y0) / (x1 - x0)
     * b = 1
     * c = y0 - (a * x0)
	 * @return c
	 */
    private double findLinearInterpolationCoheficientC() {
        return p0.y - (findLinearInterpolationCoheficientA() * p0.x);
    }

    /**
	 * Return the position where a perpendicular line from the position passed will intersect the wall
	 */
    public Point perpendicularProjection(Point position) {
        double a0 = findLinearInterpolationCoheficientA();
        double b0 = findLinearInterpolationCoheficientB();
        double c0 = findLinearInterpolationCoheficientC();
        double a1 = -1.0 / findLinearInterpolationCoheficientA();
        double b1 = 1;
        double c1 = position.getY() - (a1 * position.getX());
        double x;
        double y;
        if (a0 == 0) {
            x = position.getX();
            y = p0.y;
        } else if (a0 == Double.NEGATIVE_INFINITY || a0 == Double.POSITIVE_INFINITY) {
            x = p0.x;
            y = position.getY();
        } else {
            x = (-(-c1 / b1) + (-c0 / b0)) / ((-a1 / b1) - (-a0 / b0));
            y = (a1 * x + c1) / b1;
        }
        return new Point(x, y);
    }

    /**
	 * Calculates the 2D distance between a point and a line
	 * @param x
	 * @param y
	 * @return
	 */
    public double distanceFrom(Point p) {
        double p1 = (findLinearInterpolationCoheficientA() * p.x) + (findLinearInterpolationCoheficientB() * p.y) + findLinearInterpolationCoheficientC();
        double p2 = Math.pow(findLinearInterpolationCoheficientA(), 2) + Math.pow(findLinearInterpolationCoheficientB(), 2);
        double p3 = Math.abs(p1) / Math.sqrt(p2);
        return p3;
    }
}
