package dynamic.shapes;

import math.geom2d.Measure2D;
import math.geom2d.Shape2D;
import math.geom2d.domain.Domain2D;
import math.geom2d.polygon.Polygon2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;

/**
 * A point located on a curve, at a fixed parametric position.
 * @author Legland
 */
public class DomainToPolygon2D extends DynamicShape2D {

    DynamicShape2D parent1;

    DynamicMeasure2D parent2;

    private Polygon2D polygon = null;

    public DomainToPolygon2D(DynamicShape2D domain, DynamicMeasure2D nbSeg) {
        super();
        this.parent1 = domain;
        this.parent2 = nbSeg;
        parents.add(domain);
        parents.add(nbSeg);
        parents.trimToSize();
        update();
    }

    @Override
    public Shape2D getShape() {
        return polygon;
    }

    @Override
    public void update() {
        this.defined = false;
        if (!parent1.isDefined()) return;
        if (!parent2.isDefined()) return;
        Shape2D shape;
        Measure2D measure;
        shape = parent1.getShape();
        if (!(shape instanceof Domain2D)) return;
        Domain2D domain = (Domain2D) shape;
        measure = parent2.getMeasure();
        double value = measure.getValue();
        int nbSeg = (int) Math.round(value);
        polygon = domain.getAsPolygon(nbSeg);
        this.defined = true;
    }
}
