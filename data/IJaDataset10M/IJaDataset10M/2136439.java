package dynamic.vectors;

import math.geom2d.*;
import dynamic.*;

public class Vector2Points2D extends DynamicVector2D {

    private DynamicShape2D parent1;

    private DynamicShape2D parent2;

    Vector2D vector = new Vector2D(0, 0);

    public Vector2Points2D(DynamicShape2D point1, DynamicShape2D point2) {
        super();
        parent1 = point1;
        parent2 = point2;
        parents.add(point1);
        parents.add(point2);
        update();
    }

    @Override
    public Vector2D getVector() {
        return vector;
    }

    @Override
    public void update() {
        this.defined = false;
        if (!parent1.isDefined()) return;
        if (!parent2.isDefined()) return;
        Shape2D shape;
        shape = parent1.getShape();
        if (!(shape instanceof Point2D)) return;
        Point2D point1 = (Point2D) shape;
        shape = parent2.getShape();
        if (!(shape instanceof Point2D)) return;
        Point2D point2 = (Point2D) shape;
        this.vector = new Vector2D(point1, point2);
        this.defined = true;
    }
}
