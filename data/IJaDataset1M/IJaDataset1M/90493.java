package dynamic.shapes;

import dynamic.*;
import math.geom2d.line.*;
import math.geom2d.*;

/**
 * @author dlegland
 */
public class StraightLinePointAngle2D extends DynamicShape2D {

    DynamicShape2D parent1;

    DynamicMeasure2D parent2;

    private StraightLine2D line = new StraightLine2D();

    /**
	 * By default, LineArc2Points2D with only 2 points is a line segment.
	 * This is equivalent to LineArc2Points2D(point1, point2, 0, 1).
	 * @param point1
	 * @param point2
	 */
    public StraightLinePointAngle2D(DynamicShape2D point, DynamicMeasure2D angle) {
        super();
        this.parent1 = point;
        this.parent2 = angle;
        parents.add(point);
        parents.add(angle);
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
        if (!parent2.isDefined()) return;
        Shape2D shape = parent1.getShape();
        if (!(shape instanceof Point2D)) return;
        Point2D point = (Point2D) shape;
        Measure2D measure = parent2.getMeasure();
        if (!(measure instanceof AngleMeasure2D)) return;
        double angle = ((AngleMeasure2D) measure).getAngle(AngleUnit.RADIAN);
        line = new StraightLine2D(point, angle);
        this.defined = true;
    }
}
