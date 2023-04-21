package cn.edu.tsinghua.thss.alg.closestpair;

/**
 * The pair result to return.
 * 
 * @author
 * 
 */
public class PairResult {

    /** The coordinates of the two points */
    public double x1, y1;

    public double x2, y2;

    /** The distance between the two points */
    public double distance;

    /**
	 * Initializes the result
	 * 
	 * @param x1
	 *            the x coordinate of the first point
	 * @param y1
	 *            the y coordinate of the first point
	 * @param x2
	 *            the x coordinate of the second point
	 * @param y2
	 *            the y coordinate of the second point
	 * @param distance
	 */
    public PairResult(double x1, double y1, double x2, double y2, double distance) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.distance = distance;
    }

    public String toString() {
        return "Distance:" + distance + "\nPoint 1:(" + x1 + "," + y1 + ")\n" + "Point 2:(" + x2 + "," + y2 + ")";
    }
}
