package dynamic.vectors;

import math.geom2d.Measure2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.curve.Curve2D;
import util.CurveUtils;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;
import dynamic.DynamicVector2D;

/**
 * @author dlegland
 */
public class TangentVectorCurvePosition2D extends DynamicVector2D {

    DynamicShape2D parent1;

    DynamicMeasure2D parent2;

    private Vector2D vector = new Vector2D();

    /**
	 * Tangent to a curve at a given point. 
	 * @param curve
	 * @param point
	 */
    public TangentVectorCurvePosition2D(DynamicShape2D curve, DynamicMeasure2D position) {
        this.parent1 = curve;
        this.parent2 = position;
        parents.add(curve);
        parents.add(position);
        parents.trimToSize();
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
        Shape2D shape = parent1.getShape();
        if (!(shape instanceof Curve2D)) return;
        Curve2D curve = (Curve2D) shape;
        Measure2D measure = parent2.getMeasure();
        double pos = measure.getValue();
        vector = CurveUtils.getTangentVector(curve, pos);
        this.defined = true;
    }
}
