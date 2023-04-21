package agentswarm.view;

/**
 * Represents a line in coordinate space.
 */
public class Line {

    private Coordinate one;

    private Coordinate two;

    private double m;

    private double b;

    private boolean isVertical;

    private boolean isHorisontal;

    /** Creates a line throught the given two points. */
    public Line(Coordinate one, Coordinate two) {
        this.one = one;
        this.two = two;
        this.isVertical = one.x == two.x;
        this.isHorisontal = one.y == two.y;
        this.m = (two.y - one.y) / (two.x - one.x);
        this.b = yAt(0);
    }

    /** Returns the point at the intersection of this line and the other line. Returns null if there
     *  is no intersection, or if the lines are coincident.
     */
    public Coordinate intersection(Line other) {
        Coordinate intersection = null;
        if (!((isVertical && other.isVertical) || (isHorisontal && other.isHorisontal))) {
            double x;
            double y;
            if (isVertical) {
                x = one.x;
                y = other.yAt(one.x);
            } else if (isHorisontal) {
                x = other.xAt(one.y);
                y = one.y;
            } else if (other.isVertical) {
                x = other.one.x;
                y = yAt(other.one.x);
            } else if (other.isHorisontal) {
                x = xAt(other.one.y);
                y = other.one.y;
            } else {
                x = (other.b - b) / (m - other.m);
                y = yAt(x);
            }
            intersection = new Coordinate(x, y);
        }
        return intersection;
    }

    /** The y coordinate at the point along this line at the given x coordinate. */
    private double yAt(double x) {
        return one.y - (one.x - x) * m;
    }

    /** The x coordinate at the point along this line at the given y coordinate. */
    private double xAt(double y) {
        return one.x - ((one.y - y) / m);
    }
}
