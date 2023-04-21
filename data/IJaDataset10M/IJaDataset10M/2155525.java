package ru.vsu.triang.algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ru.vsu.triang.annotaions.AfterStep;
import ru.vsu.triang.annotaions.BeforeStep;
import ru.vsu.triang.model.interfaces.IAction;
import ru.vsu.triang.model.interfaces.ILine;
import ru.vsu.triang.model.interfaces.IPoint;
import ru.vsu.triang.model.interfaces.IStepping;
import ru.vsu.triang.model.interfaces.ITriangle;
import ru.vsu.triang.model.interfaces.ITriangulation;

@BeforeStep(className = "ru.vsu.triang.algorithms.visual.CoefficientInputer")
@AfterStep(className = "ru.vsu.triang.algorithms.visual.IntegralShower")
public class IntegralCriterion implements IAlgorithm, IStepping {

    private ITriangulation model;

    private IAction actionAfter;

    private IAction actionBefore;

    private double idealSquare;

    private int minCount = 4;

    private double eps = 0.2;

    public ITriangulation perform() {
        double[] input = (double[]) actionBefore.doStuff("input:kSquare, kShape, kSize, idelSquare", null);
        double kSquare = 0;
        double kShape = 0.5;
        double kSize = 0.5;
        this.idealSquare = 1000;
        minCount = (int) Math.round(input[0]);
        eps = input[1];
        double fSquare = getSquareMetric();
        double fShape = getShapeMetric();
        double fSize = getSizeMetric();
        double f = kSquare * fSquare + kShape * fShape + kSize * fSize;
        actionAfter.doStuff("fSquare= " + fSquare + ", fShape = " + fShape + ", fSize=" + fSize + ", f=" + f, null);
        return model;
    }

    private double getSizeMetric() {
        Map<Double, Integer> rasp = new HashMap<Double, Integer>();
        List<ITriangle> allTriangles = model.getTriangles();
        for (ITriangle triang : allTriangles) {
            double curS = TriangMath.getSquare(triang);
            double key = 0;
            for (double s : rasp.keySet()) {
                double del = eps * s;
                if (s - del < curS && curS < s + del) {
                    key = s;
                    break;
                }
            }
            int count = 1;
            if (key != 0) {
                count = rasp.get(key) + 1;
                rasp.remove(key);
                key = (key + curS) / 2;
            } else {
                key = curS;
            }
            rasp.put(key, count);
        }
        double bestS = 0;
        int maxCount = 0;
        for (double s : rasp.keySet()) {
            int curCount = rasp.get(s);
            if (curCount > maxCount) {
                bestS = s;
                maxCount = curCount;
            }
        }
        double res = 0;
        if (maxCount > minCount) {
            double trCount = 0;
            for (ITriangle triang : allTriangles) {
                double s = TriangMath.getSquare(triang);
                res += Math.abs(s - bestS) / bestS;
                trCount++;
            }
            res = res / trCount;
        }
        if (res == 0) return 0;
        return Math.exp(res * 10) / Math.exp(10);
    }

    private double getShapeMetric() {
        final double cos60 = 0.5;
        double res = 0;
        for (ITriangle triangle : model.getTriangles()) {
            Set<IPoint> points = new HashSet<IPoint>();
            for (ILine rib : triangle.getRibs()) {
                points.addAll(rib.getEnds());
            }
            Iterator<IPoint> iter = points.iterator();
            IPoint point1 = iter.next();
            IPoint point2 = iter.next();
            IPoint point3 = iter.next();
            double e = (Math.abs(cos60 - TriangMath.getCos(point1, point2, point3)) + Math.abs(cos60 - TriangMath.getCos(point3, point1, point2)) + Math.abs(cos60 - TriangMath.getCos(point2, point3, point1))) / 3;
            res += e;
        }
        res = res / model.getTriangles().size();
        return Math.exp(res * 10) / Math.exp(10);
    }

    private double getSquareMetric() {
        double res = 0;
        for (ITriangle triangle : model.getTriangles()) {
            res += Math.abs((TriangMath.getSquare(triangle) - idealSquare) / idealSquare);
        }
        res = res / model.getTriangles().size();
        return Math.exp(res * 10) / Math.exp(10);
    }

    public void setModel(ITriangulation model) {
        this.model = model;
    }

    public void setAfter(IAction action) {
        this.actionAfter = action;
    }

    public void setBefore(IAction action) {
        this.actionBefore = action;
    }

    public void setInner(IAction action) {
    }
}
