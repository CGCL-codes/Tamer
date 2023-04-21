package dynamic.shapes;

import java.util.ArrayList;
import math.geom2d.CountMeasure2D;
import math.geom2d.Measure2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.circulinear.CirculinearCurve2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.point.PointArray2D;
import math.geom2d.point.PointSet2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;

/**
 * Distributes N points on the curve, with respect to their geodesic length.
 * @author Legland
 */
public class GeodesicPointsOnCurve2D extends DynamicShape2D {

    /** The parent curve */
    DynamicShape2D parent1;

    /** The number of points on the curve */
    DynamicMeasure2D parent2;

    /** The resulting point set */
    private PointSet2D pointSet = new PointArray2D();

    public GeodesicPointsOnCurve2D(DynamicShape2D curve, DynamicMeasure2D number) {
        super();
        this.parent1 = curve;
        this.parent2 = number;
        parents.add(curve);
        parents.add(number);
        parents.trimToSize();
        update();
    }

    @Override
    public PointSet2D getShape() {
        return pointSet;
    }

    @Override
    public boolean isDefined() {
        return this.defined;
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
        if (!(measure instanceof CountMeasure2D)) return;
        int number = ((CountMeasure2D) measure).getCount();
        if (!curve.isBounded()) return;
        if (!(curve instanceof CirculinearCurve2D)) return;
        CirculinearCurve2D circu = (CirculinearCurve2D) curve;
        boolean closed = false;
        if (curve instanceof ContinuousCurve2D) {
            if (((ContinuousCurve2D) curve).isClosed()) closed = true;
        }
        double incr = circu.getLength() / (closed ? number : number - 1);
        ArrayList<Point2D> points = new ArrayList<Point2D>(number);
        for (int i = 0; i < number; i++) {
            double pos = incr * (i + 1);
            points.add(circu.getPoint(circu.getPosition(pos)));
        }
        pointSet = new PointArray2D(points);
        this.defined = true;
    }
}
