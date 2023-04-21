package math.geom2d;

import java.awt.Graphics2D;

/**
 * A label is a text located at a given position. 
 * @author dlegland
 */
public class Label2D implements Shape2D {

    protected Point2D position;

    protected String label;

    public Label2D(Point2D pos) {
        this.position = pos;
        this.label = "Label";
    }

    public Label2D(Point2D pos, String text) {
        this.position = pos;
        this.label = text;
    }

    public String getText() {
        return label;
    }

    public void setText(String text) {
        label = text;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D point) {
        position = point;
    }

    public boolean contains(double x, double y) {
        return false;
    }

    public boolean contains(Point2D p) {
        return false;
    }

    /**
	 * get the distance of the shape to the given point, or the distance of point
	 * to the frontier of the shape in the case of a plain shape.
	 */
    public double getDistance(Point2D p) {
        return position.getDistance(p);
    }

    /**
	 * get the distance of the shape to the given point, specified by x and y, or 
	 * the distance of point to the frontier of the shape in the case of a plain 
	 * (i.e. fillable) shape.
	 */
    public double getDistance(double x, double y) {
        return position.getDistance(x, y);
    }

    /** 
	 * Returns true if the shape is bounded, that is if we can draw a finite rectangle
	 * enclosing the shape. For example, a straight line or a parabola are not bounded.
	 */
    public boolean isBounded() {
        return true;
    }

    public Shape2D clip(Box2D box) {
        return position.clip(box);
    }

    public boolean isEmpty() {
        return label == null;
    }

    /**
	 * return a bounding box centered on the point, with unit width and height.
	 */
    public Box2D getBoundingBox() {
        return position.getBoundingBox();
    }

    public Label2D transform(AffineTransform2D trans) {
        return new Label2D(position.transform(trans), new String(label));
    }

    public void draw(Graphics2D g2) {
        g2.drawString(label, (float) position.getX(), (float) position.getY());
    }

    public boolean almostEquals(GeometricObject2D obj, double eps) {
        return this == obj;
    }
}
