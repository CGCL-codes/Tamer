package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.conic.*;
import math.geom2d.line.*;
import dynamic.*;

/**
 * Construct an ellipse from 3 points: the center, the point marking the end
 * of the major semiaxis, and a third point defining the length of the minor
 * semiaxis. Last point also specifies orientation of ellipse.
 * @author Legland
 */
public class Ellipse3Points2D extends DynamicShape2D {

    DynamicShape2D parent1;

    DynamicShape2D parent2;

    DynamicShape2D parent3;

    private Ellipse2D ellipse = new Ellipse2D();

    public Ellipse3Points2D(DynamicShape2D point1, DynamicShape2D point2, DynamicShape2D point3) {
        super();
        parent1 = point1;
        parent2 = point2;
        parent3 = point3;
        parents.add(point1);
        parents.add(point2);
        parents.add(point3);
        parents.trimToSize();
        update();
    }

    @Override
    public Shape2D getShape() {
        return ellipse;
    }

    @Override
    public void update() {
        this.defined = false;
        if (!parent1.isDefined()) return;
        if (!parent2.isDefined()) return;
        if (!parent3.isDefined()) return;
        Shape2D shape;
        shape = parent1.getShape();
        if (!(shape instanceof Point2D)) return;
        Point2D point1 = (Point2D) shape;
        shape = parent2.getShape();
        if (!(shape instanceof Point2D)) return;
        Point2D point2 = (Point2D) shape;
        shape = parent3.getShape();
        if (!(shape instanceof Point2D)) return;
        Point2D point3 = (Point2D) shape;
        if (Point2D.isColinear(point1, point2, point3)) return;
        double xc = point1.getX();
        double yc = point1.getY();
        StraightLine2D line1 = new StraightLine2D(point1, point2);
        StraightLine2D line2 = StraightLine2D.createPerpendicular(line1, point1);
        double r1 = point2.getDistance(point1);
        double r2 = line2.getProjectedPoint(point3).getDistance(point1);
        double theta = line1.getHorizontalAngle();
        boolean direct = line1.isInside(point3);
        ellipse = new Ellipse2D(xc, yc, r1, r2, theta, direct);
        this.defined = true;
    }
}
