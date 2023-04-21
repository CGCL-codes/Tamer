package net.sourceforge.geom4j;

/**
 * A segment of a straight line in a plane.
 * A set of points of the line lying between start point and end point.
 *   
 * @author Sergey Khenkin
 * @since 1.0.0
 */
public final class Segment extends BasicFigure {

    private Point startPoint;

    private Point endPoint;

    public Segment(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Segment) {
            Segment s = (Segment) obj;
            return startPoint.equals(s.startPoint) && endPoint.equals(s.endPoint) || startPoint.equals(s.endPoint) && endPoint.equals(s.startPoint);
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getLeftPoint().hashCode();
        result = prime * result + getRightPoint().hashCode();
        return result;
    }

    /** Get the length of this segment */
    public double length() {
        return startPoint.distanceTo(endPoint);
    }

    @Override
    public String toString() {
        return "[" + startPoint + ", " + endPoint + "]";
    }

    @Override
    protected boolean containedIn(Figure f) {
        return f.contains(this);
    }

    /** Check if the segment contains segment s */
    public boolean contains(Segment s) {
        return contains(s.startPoint) && contains(s.endPoint);
    }

    /** Check if the segment contains point p */
    public boolean contains(Point p) {
        Line line = getLine();
        if (line.contains(p)) {
            double ax = startPoint.getX();
            double ay = startPoint.getY();
            double bx = endPoint.getX();
            double by = endPoint.getY();
            double px = p.getX();
            double py = p.getY();
            if (Config.equal(ay, by)) {
                return Config.between(px, ax, bx);
            } else {
                return Config.between(py, ay, by);
            }
        } else {
            return false;
        }
    }

    /** Segment can't contain a line */
    @Override
    protected boolean contains(Line line) {
        return false;
    }

    /** Segment can't contain a polygon */
    @Override
    protected boolean contains(Polygon p) {
        return false;
    }

    /** Get line passing through this segment */
    public Line getLine() {
        return new Line(startPoint, endPoint);
    }

    @Override
    protected Figure intersectsImpl(Figure f) {
        return f.intersects(this);
    }

    @Override
    protected Figure intersects(Point p) {
        return p.intersects(this);
    }

    @Override
    protected Figure intersects(Line line) {
        return line.intersects(this);
    }

    @Override
    protected Figure intersects(Segment s) {
        Line l1 = getLine();
        Line l2 = s.getLine();
        if (l1.equals(l2)) {
            double Ax = this.getStartPoint().getX();
            double Ay = this.getStartPoint().getY();
            double Bx = this.getEndPoint().getX();
            double By = this.getEndPoint().getY();
            double Cx = s.getStartPoint().getX();
            double Cy = s.getStartPoint().getY();
            double Dx = s.getEndPoint().getX();
            double Dy = s.getEndPoint().getY();
            if (Ax == Bx) {
                if (By < Ay) {
                    double t = Ay;
                    Ay = By;
                    By = t;
                }
                if (Dy < Cy) {
                    double t = Cy;
                    Cy = Dy;
                    Dy = t;
                }
                if (Ay <= Cy) {
                    if (By < Cy) {
                        return EMPTY;
                    } else if (By == Cy) {
                        return new Point(Bx, By);
                    } else {
                        return new Segment(new Point(Cx, Cy), new Point(Bx, Math.min(By, Dy)));
                    }
                } else {
                    if (Dy < Ay) {
                        return EMPTY;
                    } else if (Dy == Ay) {
                        return new Point(Dx, Dy);
                    } else {
                        return new Segment(new Point(Ax, Ay), new Point(Bx, Math.min(By, Dy)));
                    }
                }
            } else {
                if (Bx < Ax) {
                    double t = Ax;
                    Ax = Bx;
                    Bx = t;
                    t = Ay;
                    Ay = By;
                    By = t;
                }
                if (Dx < Cx) {
                    double t = Cx;
                    Cx = Dx;
                    Dx = t;
                    t = Cy;
                    Cy = Dy;
                    Dy = t;
                }
                if (Ax <= Cx) {
                    if (Bx < Cx) {
                        return EMPTY;
                    } else if (Bx == Cx) {
                        return new Point(Bx, By);
                    } else {
                        return new Segment(new Point(Cx, Cy), Bx <= Dx ? new Point(Bx, By) : new Point(Dx, Dy));
                    }
                } else {
                    if (Dx < Ax) {
                        return EMPTY;
                    } else if (Dx == Ax) {
                        return new Point(Dx, Dy);
                    } else {
                        return new Segment(new Point(Ax, Ay), Bx <= Dx ? new Point(Bx, By) : new Point(Dx, Dy));
                    }
                }
            }
        } else {
            Figure lineIntersection = l1.intersects(l2);
            if (contains(lineIntersection) && s.contains(lineIntersection)) {
                return lineIntersection;
            } else {
                return EMPTY;
            }
        }
    }

    @Override
    protected Figure intersects(Polygon p) {
        return intersects(getLine().intersects(p));
    }

    @Override
    public double distanceToImpl(Figure f) {
        return f.distanceTo(this);
    }

    @Override
    protected double distanceTo(Point p) {
        return p.distanceTo(this);
    }

    @Override
    protected double distanceTo(Line line) {
        return line.distanceTo(this);
    }

    @Override
    protected double distanceTo(Segment s) {
        if (EMPTY.equals(intersects(s))) {
            double min = Double.POSITIVE_INFINITY;
            min = Math.min(min, startPoint.distanceTo(s));
            min = Math.min(min, endPoint.distanceTo(s));
            min = Math.min(min, s.startPoint.distanceTo(this));
            min = Math.min(min, s.endPoint.distanceTo(this));
            return min;
        } else {
            return 0;
        }
    }

    /**
     * Get left endpoint (endpoint with the smaller x coordinate).
     * If x coordinates of the endpoints are the same, return the
     * endpoint with the smaller y coordinate. 
     */
    public Point getLeftPoint() {
        Point p1 = startPoint;
        Point p2 = endPoint;
        if (Config.lessThan(p1.getX(), p2.getX())) {
            return p1;
        } else if (Config.lessThan(p2.getX(), p1.getX())) {
            return p2;
        } else {
            if (Config.lessThan(p2.getY(), p1.getY())) {
                return p2;
            } else {
                return p1;
            }
        }
    }

    /**
     * Get left endpoint (endpoint with the smaller x coordinate).
     * If x coordinates of the endpoints are the same, return the
     * endpoint with the smaller y coordinate. 
     */
    public Point getRightPoint() {
        Point p1 = startPoint;
        Point p2 = endPoint;
        if (Config.lessThan(p1.getX(), p2.getX())) {
            return p2;
        } else if (Config.lessThan(p2.getX(), p1.getX())) {
            return p1;
        } else {
            if (Config.lessThan(p1.getY(), p2.getY())) {
                return p2;
            } else {
                return p1;
            }
        }
    }

    /**
     * Get middle point of the segment
     */
    public Point getMiddlePoint() {
        return new Point((startPoint.getX() + endPoint.getX()) / 2, (startPoint.getY() + endPoint.getY()) / 2);
    }
}
