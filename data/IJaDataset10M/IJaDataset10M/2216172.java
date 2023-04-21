package dynamic.labels;

import math.geom2d.*;
import dynamic.*;

public class PredicateLabel2D extends DynamicLabel2D {

    DynamicShape2D parent1;

    DynamicPredicate2D parent2;

    Label2D label = new Label2D(new Point2D(), "");

    public PredicateLabel2D(DynamicShape2D position, DynamicPredicate2D predicate) {
        super();
        this.parent1 = position;
        this.parent2 = predicate;
        parents.add(position);
        parents.add(predicate);
        parents.trimToSize();
        update();
    }

    @Override
    public Label2D getLabel() {
        return this.label;
    }

    @Override
    public void update() {
        this.defined = false;
        if (!parent1.isDefined()) return;
        if (!parent2.isDefined()) return;
        Shape2D shape = parent1.getShape();
        if (!(shape instanceof Point2D)) return;
        Point2D point = (Point2D) shape;
        boolean result = parent2.getResult();
        this.label.setText(Boolean.toString(result));
        this.label.setPosition(point);
        this.defined = true;
    }
}
