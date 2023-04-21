package dynamic.shapes;

import dynamic.*;
import math.geom2d.line.*;
import math.geom2d.*;

/**
 * @author dlegland
 */
public class VerticalLine2D extends DynamicShape2D {

    DynamicShape2D parent1;

    private StraightLine2D line = new StraightLine2D();

    /**
	 * By default, LineArc2Points2D with only 2 points is a line segment.
	 * This is equivalent to LineArc2Points2D(point1, point2, 0, 1).
	 * @param point1
	 * @param point2
	 */
    public VerticalLine2D(DynamicShape2D point) {
        super();
        this.parent1 = point;
        parents.add(point);
        parents.trimToSize();
        update();
    }

    @Override
    public Shape2D getShape() {
        return line;
    }

    @Override
    public void update() {
        this.defined = false;
        if (!parent1.isDefined()) return;
        Shape2D shape = parent1.getShape();
        if (!(shape instanceof Point2D)) return;
        Point2D point = (Point2D) shape;
        double angle = Math.PI / 2;
        line = new StraightLine2D(point, angle);
        this.defined = true;
    }
}
