package objects.twodimensional;

import com.dinim.matrix.twodimensional.IPoint2D;

public class Rectangle2D extends Object2D {

    private IPoint2D coordinates;

    private IPoint2D pointOfReference;

    private IShape2D shape;

    public Rectangle2D(double x, double y, double width, double height) {
        Point2D[] points = new Point2D[] { new Point2D(x, y), new Point2D(x + width, y), new Point2D(x + width, y + height), new Point2D(x, y + height) };
        int[] connections = new int[] { 0, 1, 2, 3 };
        shape = new Shape2D(points, connections);
        pointOfReference = new Point2D(x, y);
    }

    public IPoint2D getCoordinates() {
        return coordinates;
    }

    public IPoint2D getPointOfReference() {
        return pointOfReference;
    }

    public IShape2D getShape() {
        return shape;
    }

    public void setCoordinates(Point2D coordinates) {
        this.coordinates = coordinates;
    }

    public void setPointOfReference(Point2D pointOfReference) {
        this.pointOfReference = pointOfReference;
    }

    public void setShape(IShape2D shape) {
        this.shape = shape;
    }
}
