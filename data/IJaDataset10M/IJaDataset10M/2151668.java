package dynamic.shapes;

import math.geom2d.*;
import math.geom2d.line.*;
import dynamic.*;

/**
 * @author Legland
 */
public class PerpendicularLine2D extends DynamicShape2D {

    DynamicShape2D parent1;

    DynamicShape2D parent2;

    StraightLine2D line = new StraightLine2D();

    public PerpendicularLine2D(DynamicShape2D baseLine, DynamicShape2D point) {
        super();
        this.parent1 = baseLine;
        this.parent2 = point;
        parents.add(baseLine);
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
        if (!parent2.isDefined()) return;
        Shape2D shape;
        shape = parent1.getShape();
        if (!(shape instanceof LinearShape2D)) return;
        LinearShape2D baseLine = (LinearShape2D) shape;
        shape = parent2.getShape();
        if (!(shape instanceof Point2D)) return;
        Point2D point = (Point2D) shape;
        Vector2D vect = baseLine.getVector();
        double x0 = point.getX();
        double y0 = point.getY();
        line = new StraightLine2D(x0, y0, -vect.getY(), vect.getX());
        this.defined = true;
    }
}
