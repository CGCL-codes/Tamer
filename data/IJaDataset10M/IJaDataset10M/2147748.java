package coho.geom.twodim;

import coho.common.number.*;
import coho.geom.*;

/**
 * A segment described by two points p1 and p2. 
 * Also (x-x1)/(x2-x1)  = (y-y1)/(y2-y1) or Ax+By+C=0; A=y2-y1 B = -(x2-x1) C = -(Ax1+By1).
 * CONSIDER: Is the segment directed or not?
 * For the operations, the result depends on the data type it used. 
 * It might throw exceptions, for example, InvalidIntervalException.
 * NotComparableIntervalException.
 * @author chaoyan
 *
 */
public class Segment implements GeomObj2, Comparable<Segment> {

    private final Point[] p = new Point[2];

    private final CohoType type;

    public Point p(int i) {
        switch(i) {
            case 0:
                return p[0];
            case 1:
                return p[1];
            default:
                throw new UnsupportedOperationException();
        }
    }

    public Point left() {
        return p[0].min(p[1]);
    }

    public Point right() {
        return p[0].max(p[1]);
    }

    public CohoType type() {
        return type;
    }

    public Segment(Point p0, Point p1) {
        if (p0.type() != p1.type()) throw new GeomException("Segment: The point should have same types:" + p0.type() + ", " + p1.type());
        p[0] = p0;
        p[1] = p1;
        type = p0.type();
    }

    public static Segment create(Point p0, Point p1) {
        return new Segment(p0, p1);
    }

    /*******************************************
	 * Operations doesn't depend on representation 
	 *******************************************/
    public boolean isPoint() {
        return p[0].equals(p[1]);
    }

    public CohoNumber len2() {
        CohoNumber dx = p[1].x().sub(p[0].x());
        CohoNumber dy = p[1].y().sub(p[0].y());
        return (dx.mult(dx).add(dy.mult(dy)));
    }

    private double len() {
        return (Math.sqrt(len2().doubleValue()));
    }

    public boolean isHoriz() {
        return p[0].y().equals(p[1].y());
    }

    public boolean isVert() {
        return p[0].x().equals(p[1].x());
    }

    public BoundingBox bbox() {
        return new BoundingBox(p[0], p[1]);
    }

    public Segment translate(Point offset) {
        return new Segment(p[0].translate(offset), p[1].translate(offset));
    }

    public Segment transpose() {
        return new Segment(p[0].transpose(), p[1].transpose());
    }

    public Segment negate() {
        return new Segment(p[0].negate(), p[1].negate());
    }

    public Segment reverse() {
        return new Segment(p[1], p[0]);
    }

    /***********************************************
	 * Operations depends on the data representation.
	 ***********************************************/
    public CohoNumber x(ScaleNumber y) {
        return transpose().y(y);
    }

    public CohoNumber y(ScaleNumber x) {
        if (x.compareTo(left().x()) < 0 || x.compareTo(right().x()) > 0) throw new GeomException("extrapolation is for fools:  " + this + ".y(" + x + ")"); else if (p[0].y().equals(p[1].y())) return p[0].y(); else if (p[0].x().equals(p[1].x())) throw new GeomException("singularity in interpolation"); else {
            CohoNumber temp = (p[0].y().mult(p[1].x().sub(x))).add(p[1].y().mult(x.sub(p[0].x())));
            return temp.div(p[1].x().sub(p[0].x()));
        }
    }

    public CohoNumber A() {
        return p[1].y().sub(p[0].y());
    }

    public CohoNumber B() {
        return p[0].x().sub(p[1].x());
    }

    public CohoNumber C() {
        CohoNumber x0 = p[0].x();
        CohoNumber y0 = p[0].y();
        CohoNumber x1 = p[1].x();
        CohoNumber y1 = p[1].y();
        CohoNumber delX = x1.sub(x0);
        CohoNumber delY = y1.sub(y0);
        return delX.mult(y0).sub(delY.mult(x0));
    }

    public Line line() {
        if (type instanceof ScaleType) {
            Segment aprSeg = specifyType(CohoAPR.type);
            Line l = new Line(aprSeg.A(), aprSeg.B(), aprSeg.C());
            return l.specifyType(type);
        } else {
            return new Line(A(), B(), C());
        }
    }

    public GeomObj2 intersect(GeomObj2 g) {
        if (g instanceof BoundingBox) return intersectBoundingBox((BoundingBox) g).specifyType(type);
        if (g instanceof Point) return intersectPoint((Point) g).specifyType(type);
        if (g instanceof Segment) return intersectSegment((Segment) g).specifyType(type);
        return g.intersect(this).specifyType(type);
    }

    protected GeomObj2 intersectBoundingBox(BoundingBox that) {
        if ((type == CohoDouble.type || that.type() == CohoDouble.type) && (type instanceof ScaleType && that.type() instanceof ScaleType)) {
            try {
                GeomObj2 obj = this.specifyType(DoubleInterval.type).baseIntersectBoundingBox(that.specifyType(DoubleInterval.type));
                if (obj.maxError() > eps) throw new ArithmeticException("too large interval");
                return obj;
            } catch (ArithmeticException e) {
                return this.specifyType(CohoAPR.type).baseIntersectBoundingBox(that.specifyType(CohoAPR.type));
            }
        } else {
            return baseIntersectBoundingBox(that);
        }
    }

    private GeomObj2 baseIntersectBoundingBox(BoundingBox b) {
        if (isPoint()) {
            return b.intersect(this.left());
        } else if (b.isSegment()) {
            return baseIntersectSegment(new Segment(b.ll(), b.ur()));
        } else if (b.isPoint()) {
            return baseIntersectPoint(b.ll());
        }
        if (bbox().intersect(b) instanceof Empty) {
            return Empty.instance();
        } else if (b.contains(this)) {
            return this;
        }
        Point[] v = new Point[] { b.ll(), b.lr(), b.ur(), b.ul() };
        Point[] q = new Point[2];
        int i = 0;
        GeomObj2 g;
        for (int j = 0; j < 4; j++) {
            g = baseIntersectPoint(v[j]);
            if (!(g instanceof Empty)) {
                q[i++] = v[j];
                v[j] = null;
            }
        }
        for (int j = 0; j < 4; j++) {
            if (v[j] != null && v[(j + 1) % 4] != null) {
                g = baseIntersectSegment(new Segment(v[j], v[(j + 1) % 4]));
                if (!(g instanceof Empty)) q[i++] = (Point) g;
            }
        }
        j_loop: for (int j = 0; j < 2; i++) {
            for (int jj = 0; jj < i; jj++) if (p[j].compareTo(q[jj]) == 0) continue j_loop;
            if (!(b.intersect(p[j]) instanceof Empty)) q[i++] = p[j];
        }
        if (i == 0) return (Empty.instance()); else if (i == 1) return (q[0]); else return (new Segment(q[0], q[1]));
    }

    public GeomObj2 intersectSegment(Segment that) {
        if ((type == CohoDouble.type || that.type == CohoDouble.type) && (type instanceof ScaleType && that.type instanceof ScaleType)) {
            try {
                GeomObj2 obj = this.specifyType(DoubleInterval.type).baseIntersectSegment(that.specifyType(DoubleInterval.type));
                if (obj.maxError() > eps) throw new ArithmeticException("too large interval");
                return obj;
            } catch (ArithmeticException e) {
                return this.specifyType(CohoAPR.type).baseIntersectSegment(that.specifyType(CohoAPR.type));
            }
        } else {
            return baseIntersectSegment(that);
        }
    }

    private GeomObj2 baseIntersectSegment(Segment that) {
        if (isPoint() && that.isPoint()) {
            return (p[0].compareTo(that.p[0]) == 0) ? p[0] : Empty.instance();
        } else if (that.isPoint()) {
            return baseIntersectPoint(that.p[0]);
        } else if (isPoint()) {
            return that.baseIntersectPoint(p[0]);
        }
        int cmp = bbox().compareTo(that.bbox());
        if (cmp > 0) return that.baseIntersectSegment(this); else if (cmp == 0) {
            cmp = left().compareTo(that.left());
            if (cmp > 0) return that.baseIntersectSegment(this); else if (cmp == 0) return this;
        }
        GeomObj2 ibox = bbox().intersect(that.bbox());
        if (ibox instanceof Empty) {
            return Empty.instance();
        }
        CohoNumber A1 = A(), A2 = that.A();
        CohoNumber B1 = B(), B2 = that.B();
        CohoNumber C1 = C(), C2 = that.C();
        CohoNumber denom = A1.mult(B2).sub(A2.mult(B1));
        CohoNumber num1 = B1.mult(C2).sub(B2.mult(C1));
        CohoNumber num2 = C1.mult(A2).sub(C2.mult(A1));
        if (denom.compareTo(denom.zero()) == 0) {
            if (num1.compareTo(num1.zero()) != 0 || num2.compareTo(num2.zero()) != 0) {
                return Empty.instance();
            } else {
                Point p0 = left().max(that.left());
                Point p1 = right().min(that.right());
                if (p0.equals(p1)) return p0; else return new Segment(p0, p1);
            }
        } else {
            Point result = Point.create(num1.div(denom), num2.div(denom));
            if (bbox().contains(result) && that.bbox().contains(result)) {
                return result;
            } else return Empty.instance();
        }
    }

    protected GeomObj2 intersectPoint(Point that) {
        if ((type == CohoDouble.type || that.type() == CohoDouble.type) && (type instanceof ScaleType && that.type() instanceof ScaleType)) {
            try {
                GeomObj2 obj = this.specifyType(DoubleInterval.type).baseIntersectPoint(that.specifyType(DoubleInterval.type));
                if (obj.maxError() > eps) throw new ArithmeticException("too large interval");
                return obj;
            } catch (ArithmeticException e) {
                return this.specifyType(CohoAPR.type).baseIntersectPoint(that.specifyType(CohoAPR.type));
            }
        } else {
            return baseIntersectPoint(that);
        }
    }

    private GeomObj2 baseIntersectPoint(Point pt) {
        if (!bbox().contains(pt)) return Empty.instance();
        CohoNumber A = A(), B = B(), C = C();
        CohoNumber x = pt.x(), y = pt.y();
        CohoNumber eq = A.mult(x).add(B.mult(y)).add(C);
        if (eq.compareTo(eq.zero()) == 0) return pt; else return Empty.instance();
    }

    public boolean isParallelTo(Segment that) {
        if ((type == CohoDouble.type || that.type == CohoDouble.type) && (type instanceof ScaleType && that.type instanceof ScaleType)) {
            try {
                return this.specifyType(DoubleInterval.type).baseIsParallelTo(that.specifyType(DoubleInterval.type));
            } catch (ArithmeticException e) {
                return this.specifyType(CohoAPR.type).baseIsParallelTo(that.specifyType(CohoAPR.type));
            }
        } else {
            return baseIsParallelTo(that);
        }
    }

    private boolean baseIsParallelTo(Segment that) {
        CohoNumber A1 = A(), B1 = B();
        CohoNumber A2 = that.A(), B2 = that.B();
        CohoNumber del = A1.mult(B2).sub(A2.mult(B1));
        return del.compareTo(del.zero()) == 0;
    }

    public boolean contains(GeomObj2 g) {
        if (g instanceof Point) {
            if (baseIntersectPoint((Point) g) instanceof Point) return true;
        } else if (g instanceof Segment) {
            if (baseIntersectSegment((Segment) g).equals(g)) return true;
        }
        return false;
    }

    public GeomObj2 union(GeomObj2 g) {
        throw new UnsupportedOperationException("Doesn't support it now");
    }

    public int compareTo(Segment s) {
        int result = left().compareTo(s.left());
        if (result == 0) {
            result = right().compareTo(s.right());
        }
        return result;
    }

    @Override
    public boolean equals(Object s) {
        try {
            return (s instanceof Segment) && (compareTo((Segment) s) == 0);
        } catch (ArithmeticException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return p[0].hashCode() ^ (p[1].hashCode() >> 16);
    }

    public int compareBySlope(Segment that) {
        if ((type == CohoDouble.type || that.type == CohoDouble.type) && (type instanceof ScaleType && that.type instanceof ScaleType)) {
            try {
                return this.specifyType(DoubleInterval.type).baseCompareBySlope(that.specifyType(DoubleInterval.type));
            } catch (ArithmeticException e) {
                return this.specifyType(CohoAPR.type).baseCompareBySlope(that.specifyType(CohoAPR.type));
            }
        } else {
            return baseCompareBySlope(that);
        }
    }

    private int baseCompareBySlope(Segment s) {
        if (isPoint() && s.isPoint()) {
            return 0;
        } else if (isPoint()) {
            return -1;
        } else if (s.isPoint()) {
            return 1;
        }
        Segment s0 = this, s1 = s;
        CohoNumber dy0 = s0.right().y().sub(s0.left().y());
        CohoNumber dy1 = s1.right().y().sub(s1.left().y());
        int cmp0 = dy0.compareTo(dy0.zero());
        int cmp1 = dy1.compareTo(dy1.zero());
        int cord0 = 0, cord1 = 0;
        if (cmp0 < 0) cord0 = 1;
        if (cmp1 < 0) cord1 = 1;
        if (cord0 != cord1) return -(cord0 - cord1);
        CohoNumber dx0 = s0.right().x().sub(s0.left().x());
        CohoNumber dx1 = s1.right().x().sub(s1.left().x());
        cmp0 = dx0.compareTo(dx0.zero());
        cmp1 = dx1.compareTo(dx1.zero());
        if (cmp0 == 0 && cmp1 == 0) {
            return 0;
        } else if (cmp0 == 0) {
            return 1;
        } else if (cmp1 == 0) {
            return -1;
        }
        CohoNumber tan0 = dy0.div(dx0);
        CohoNumber tan1 = dy1.div(dx1);
        return tan0.compareTo(tan1);
    }

    public int compareByAngle(Segment that) {
        if ((type == CohoDouble.type || that.type == CohoDouble.type) && (type instanceof ScaleType && that.type instanceof ScaleType)) {
            try {
                return this.specifyType(DoubleInterval.type).baseCompareByAngle(that.specifyType(DoubleInterval.type));
            } catch (ArithmeticException e) {
                return this.specifyType(CohoAPR.type).baseCompareByAngle(that.specifyType(CohoAPR.type));
            }
        } else {
            return baseCompareByAngle(that);
        }
    }

    private int baseCompareByAngle(Segment s) {
        if (isPoint() && s.isPoint()) {
            return 0;
        } else if (isPoint()) {
            return -1;
        } else if (s.isPoint()) {
            return 1;
        }
        Segment s0 = this, s1 = s;
        CohoNumber dx0 = s0.p[1].x().sub(s0.p[0].x());
        CohoNumber dy0 = s0.p[1].y().sub(s0.p[0].y());
        CohoNumber dx1 = s1.p[1].x().sub(s1.p[0].x());
        CohoNumber dy1 = s1.p[1].y().sub(s1.p[0].y());
        int cord0 = 0;
        int cmpx = dx0.compareTo(dx0.zero());
        int cmpy = dy0.compareTo(dx0.zero());
        if (cmpx >= 0 && cmpy >= 0) {
            cord0 = 0;
        } else if (cmpx < 0 && cmpy >= 0) {
            cord0 = 1;
        } else if (cmpx <= 0 && cmpy < 0) {
            cord0 = 2;
        } else {
            cord0 = 3;
        }
        int cord1 = 0;
        cmpx = dx1.compareTo(dx1.zero());
        cmpy = dy1.compareTo(dx1.zero());
        if (cmpx >= 0 && cmpy >= 0) {
            cord1 = 0;
        } else if (cmpx < 0 && cmpy >= 0) {
            cord1 = 1;
        } else if (cmpx <= 0 && cmpy < 0) {
            cord1 = 2;
        } else {
            cord1 = 3;
        }
        if (cord0 != cord1) return Integer.signum(cord0 - cord1);
        if (dx0.equals(dx0.zero()) && dx1.equals(dx1.zero())) {
            return 0;
        } else if (dx0.equals(dx0.zero())) {
            return 1;
        } else if (dx1.equals(dx1.zero())) {
            return -1;
        }
        CohoNumber tan0 = dy0.div(dx0);
        CohoNumber tan1 = dy1.div(dx1);
        return tan0.compareTo(tan1);
    }

    public int compareByRightX(Segment that) {
        int cmp = right().x().compareTo(that.right().x());
        if (cmp != 0) return cmp;
        return right().y().compareTo(that.right().y());
    }

    public Segment specifyType(CohoType type) {
        return new Segment(p[0].specifyType(type), p[1].specifyType(type));
    }

    public double maxError() {
        return Math.max(p[0].maxError(), p[1].maxError());
    }

    public String toString() {
        return "[" + p[0] + ", " + p[1] + "]";
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Segment seg1 = Segment.create(new Point(0.4494169588187707, 1.7829963763766523), new Point(0.44941695881877064, 1.8013332866549514));
        Segment seg2 = Segment.create(new Point(0.44941695881877064, 1.7829963763766523), new Point(0.4494169588187707, 1.8013332866549514));
        System.out.println(seg1.compareTo(seg2));
        System.out.println(seg1.intersectSegment(seg2));
        System.out.println(seg2.intersectSegment(seg1));
    }
}
