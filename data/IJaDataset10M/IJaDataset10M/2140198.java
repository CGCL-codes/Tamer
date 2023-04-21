package dynamic.shapes;

import java.util.Collection;
import java.util.Iterator;
import math.geom2d.Angle2D;
import math.geom2d.Measure2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.Vector2D;
import math.geom2d.circulinear.CirculinearContinuousCurve2D;
import math.geom2d.circulinear.CirculinearCurve2D;
import math.geom2d.circulinear.CirculinearElement2D;
import math.geom2d.conic.CircleArc2D;
import math.geom2d.curve.ContinuousCurve2D;
import math.geom2d.curve.Curve2D;
import math.geom2d.curve.CurveArray2D;
import math.geom2d.curve.PolyCurve2D;
import math.geom2d.curve.SmoothCurve2D;
import math.geom2d.polygon.Polyline2D;
import dynamic.DynamicMeasure2D;
import dynamic.DynamicShape2D;

/**
 * Build a curve parallel to another curve, at a given distance.
 * @author Legland
 */
public class ParallelCurveDistance2D extends DynamicShape2D {

    DynamicShape2D parent1;

    DynamicMeasure2D parent2;

    Curve2D parallel = null;

    public ParallelCurveDistance2D(DynamicShape2D line, DynamicMeasure2D distanceMeasure) {
        super();
        this.parent1 = line;
        this.parent2 = distanceMeasure;
        parents.add(line);
        parents.add(distanceMeasure);
        parents.trimToSize();
        update();
    }

    @Override
    public Shape2D getShape() {
        return parallel;
    }

    @Override
    public void update() {
        this.defined = false;
        if (!parent1.isDefined()) return;
        if (!parent2.isDefined()) return;
        Shape2D shape;
        shape = parent1.getShape();
        if (!(shape instanceof Curve2D)) return;
        Curve2D curve = (Curve2D) shape;
        Measure2D measure;
        measure = parent2.getMeasure();
        double distance = measure.getValue();
        CirculinearCurve2D circlin = null;
        if (curve instanceof CirculinearCurve2D) {
            circlin = (CirculinearCurve2D) curve;
            parallel = circlin.getParallel(distance);
        } else {
            parallel = computeParallel(curve, distance);
        }
        if (parallel == null) return;
        this.defined = true;
    }

    private static final Curve2D computeParallel(Curve2D curve, double dist) {
        if (curve instanceof ContinuousCurve2D) {
            return computeParallelContinuous((ContinuousCurve2D) curve, dist);
        }
        CurveArray2D<Curve2D> parallels = new CurveArray2D<Curve2D>();
        for (ContinuousCurve2D cont : curve.getContinuousCurves()) {
            Curve2D parallel = computeParallelContinuous(cont, dist);
            if (parallel != null) parallels.addCurve(parallel);
        }
        return parallels;
    }

    private static final ContinuousCurve2D computeParallelContinuous(ContinuousCurve2D curve, double dist) {
        if (curve instanceof CirculinearContinuousCurve2D) {
            return ((CirculinearContinuousCurve2D) curve).getParallel(dist);
        }
        if (curve instanceof SmoothCurve2D) {
            return computeParallelSmooth((SmoothCurve2D) curve, dist);
        }
        Collection<? extends SmoothCurve2D> smoothCurves = curve.getSmoothPieces();
        Iterator<? extends SmoothCurve2D> iterator = smoothCurves.iterator();
        SmoothCurve2D previous = null;
        SmoothCurve2D current = null;
        PolyCurve2D<ContinuousCurve2D> parallel = new PolyCurve2D<ContinuousCurve2D>();
        if (!iterator.hasNext()) return parallel;
        previous = iterator.next();
        ContinuousCurve2D smoothParallel = computeParallelSmooth(previous, dist);
        parallel.addCurve(smoothParallel);
        while (iterator.hasNext()) {
            current = iterator.next();
            Point2D center = current.getFirstPoint();
            Vector2D vp = previous.getTangent(previous.getT1());
            Vector2D vc = current.getTangent(current.getT0());
            double startAngle, endAngle;
            if (dist > 0) {
                startAngle = vp.getAngle() - Math.PI / 2;
                endAngle = vc.getAngle() - Math.PI / 2;
            } else {
                startAngle = vp.getAngle() + Math.PI / 2;
                endAngle = vc.getAngle() + Math.PI / 2;
            }
            CircleArc2D arc = new CircleArc2D(center, dist, startAngle, endAngle, Angle2D.formatAngle(endAngle - startAngle) < Math.PI);
            parallel.addCurve(arc);
            smoothParallel = computeParallelSmooth(current, dist);
            if (smoothParallel == null) return null;
            parallel.addCurve(smoothParallel);
            previous = current;
        }
        if (curve.isClosed()) {
            current = smoothCurves.iterator().next();
            Point2D center = current.getFirstPoint();
            Vector2D vp = previous.getTangent(previous.getT1());
            Vector2D vc = current.getTangent(current.getT0());
            double startAngle, endAngle;
            if (dist > 0) {
                startAngle = vp.getAngle() - Math.PI / 2;
                endAngle = vc.getAngle() - Math.PI / 2;
            } else {
                startAngle = vp.getAngle() + Math.PI / 2;
                endAngle = vc.getAngle() + Math.PI / 2;
            }
            CircleArc2D arc = new CircleArc2D(center, dist, startAngle, endAngle, Angle2D.formatAngle(endAngle - startAngle) < Math.PI);
            parallel.addCurve(arc);
        }
        parallel.setClosed(curve.isClosed());
        return parallel;
    }

    /**
	 * Return a parallel curve for some basic curves, otherwise return null.
	 */
    private static final ContinuousCurve2D computeParallelSmooth(SmoothCurve2D curve, double dist) {
        if (curve instanceof CirculinearElement2D) return ((CirculinearElement2D) curve).getParallel(dist);
        if (!curve.isBounded()) {
            System.err.println("Try to compute parallel of an unbounded curve.");
            return null;
        }
        Polyline2D polyline = curve.getAsPolyline(30);
        return computeParallelContinuous(polyline, dist);
    }
}
