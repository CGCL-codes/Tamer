package xmage.math.util;

import xmage.math.*;
import huf.misc.HMath;
import huf.misc.Utils;
import huf.misc.tester.ITesterComparator;

/**
 * Comparator comparing <code>xmage.math</code> classes using
 * specified precision.
 */
public final class MathCloseMatchComparator implements ITesterComparator {

    /**
	 * Create a comparator using default precision ({@link HMath#PRECISION}).
	 */
    public MathCloseMatchComparator() {
        this(HMath.PRECISION);
    }

    /**
	 * Create a comparator using specified precision.
	 *
	 * @param precision precision
	 */
    public MathCloseMatchComparator(int precision) {
        this.precision = precision;
    }

    /** Precision. */
    private final int precision;

    /**
	 * Compare objects.
	 *
	 * @param wanted valid value
	 * @param real tested value
	 */
    public boolean equals(Object wanted, Object real) {
        if (wanted.getClass() == AxisAngled.class) {
            return compareAxisAngled(wanted, real);
        }
        if (wanted.getClass() == AxisAnglef.class) {
            return compareAxisAnglef(wanted, real);
        }
        if (wanted.getClass() == Line3d.class) {
            return compareLine3d(wanted, real);
        }
        if (wanted.getClass() == Line3f.class) {
            return compareLine3f(wanted, real);
        }
        if (wanted.getClass() == Matrix4d.class) {
            return compareMatrix4d(wanted, real);
        }
        if (wanted.getClass() == Matrix4f.class) {
            return compareMatrix4f(wanted, real);
        }
        if (wanted.getClass() == Planed.class) {
            return comparePlaned(wanted, real);
        }
        if (wanted.getClass() == Planef.class) {
            return comparePlanef(wanted, real);
        }
        if (wanted.getClass() == Point2d.class) {
            return comparePoint2d(wanted, real);
        }
        if (wanted.getClass() == Point2f.class) {
            return comparePoint2f(wanted, real);
        }
        if (wanted.getClass() == Point3d.class) {
            return comparePoint3d(wanted, real);
        }
        if (wanted.getClass() == Point3f.class) {
            return comparePoint3f(wanted, real);
        }
        if (wanted.getClass() == Segment3d.class) {
            return compareSegment3d(wanted, real);
        }
        if (wanted.getClass() == Segment3f.class) {
            return compareSegment3f(wanted, real);
        }
        if (wanted.getClass() == Triangle3d.class) {
            return compareTriangle3d(wanted, real);
        }
        if (wanted.getClass() == Triangle3f.class) {
            return compareTriangle3f(wanted, real);
        }
        return wanted.equals(real);
    }

    private boolean compareAxisAngled(Object wanted, Object real) {
        assert real.getClass() == AxisAngled.class : "failed assert real.getClass() == AxisAngled.class: " + real.getClass().getName();
        AxisAngled aa = (AxisAngled) wanted;
        AxisAngled ab = (AxisAngled) real;
        return HMath.isClose(aa.x, ab.x, precision) && HMath.isClose(aa.y, ab.y, precision) && HMath.isClose(aa.z, ab.z, precision) && HMath.isClose(aa.a, ab.a, precision);
    }

    private boolean compareAxisAnglef(Object wanted, Object real) {
        assert real.getClass() == AxisAnglef.class : "failed assert real.getClass() == AxisAnglef.class: " + real.getClass().getName();
        AxisAnglef aa = (AxisAnglef) wanted;
        AxisAnglef ab = (AxisAnglef) real;
        return HMath.isClose(aa.x, ab.x, precision) && HMath.isClose(aa.y, ab.y, precision) && HMath.isClose(aa.z, ab.z, precision) && HMath.isClose(aa.a, ab.a, precision);
    }

    private boolean compareLine3d(Object wanted, Object real) {
        assert real.getClass() == Line3d.class : "failed assert real.getClass() == Line3d.class: " + real.getClass().getName();
        Line3d la = (Line3d) wanted;
        Line3d lb = (Line3d) real;
        return comparePoint3d(la.a, lb.a) && comparePoint3d(la.b, lb.b);
    }

    private boolean compareLine3f(Object wanted, Object real) {
        assert real.getClass() == Line3f.class : "failed assert real.getClass() == Line3f.class: " + real.getClass().getName();
        Line3f la = (Line3f) wanted;
        Line3f lb = (Line3f) real;
        return comparePoint3f(la.a, lb.a) && comparePoint3f(la.b, lb.b);
    }

    private boolean compareMatrix4d(Object wanted, Object real) {
        assert real.getClass() == Matrix4d.class : "failed assert real.getClass() == Matrix4d.class: " + real.getClass().getName();
        double[] ma = new double[16];
        double[] mb = new double[16];
        System.arraycopy(((Matrix4d) wanted).e, 0, ma, 0, 16);
        System.arraycopy(((Matrix4d) real).e, 0, mb, 0, 16);
        return Utils.isEqual(ma, mb, precision);
    }

    private boolean compareMatrix4f(Object wanted, Object real) {
        assert real.getClass() == Matrix4f.class : "failed assert real.getClass() == Matrix4f.class: " + real.getClass().getName();
        float[] ma = new float[16];
        float[] mb = new float[16];
        System.arraycopy(((Matrix4f) wanted).e, 0, ma, 0, 16);
        System.arraycopy(((Matrix4f) real).e, 0, mb, 0, 16);
        return Utils.isEqual(ma, mb, precision);
    }

    private boolean comparePlaned(Object wanted, Object real) {
        assert real.getClass() == Planed.class : "failed assert real.getClass() == Planed.class: " + real.getClass().getName();
        Planed pa = (Planed) wanted;
        Planed pb = (Planed) real;
        return comparePoint3d(pa.plPoint, pb.plPoint) && comparePoint3d(pa.plNormal, pb.plNormal);
    }

    private boolean comparePlanef(Object wanted, Object real) {
        assert real.getClass() == Planef.class : "failed assert real.getClass() == Planef.class: " + real.getClass().getName();
        Planef pa = (Planef) wanted;
        Planef pb = (Planef) real;
        return comparePoint3f(pa.plPoint, pb.plPoint) && comparePoint3f(pa.plNormal, pb.plNormal);
    }

    private boolean comparePoint2d(Object wanted, Object real) {
        assert real.getClass() == Point2d.class : "failed assert real.getClass() == Point2d.class: " + real.getClass().getName();
        Point2d pa = (Point2d) wanted;
        Point2d pb = (Point2d) real;
        return HMath.isClose(pa.x, pb.x, precision) && HMath.isClose(pa.y, pb.y, precision);
    }

    private boolean comparePoint2f(Object wanted, Object real) {
        assert real.getClass() == Point2f.class : "failed assert real.getClass() == Point2f.class: " + real.getClass().getName();
        Point2f pa = (Point2f) wanted;
        Point2f pb = (Point2f) real;
        return HMath.isClose(pa.x, pb.x, precision) && HMath.isClose(pa.y, pb.y, precision);
    }

    private boolean comparePoint3d(Object wanted, Object real) {
        assert real.getClass() == Point3d.class : "failed assert real.getClass() == Point3d.class: " + real.getClass().getName();
        Point3d pa = (Point3d) wanted;
        Point3d pb = (Point3d) real;
        return HMath.isClose(pa.x, pb.x, precision) && HMath.isClose(pa.y, pb.y, precision) && HMath.isClose(pa.z, pb.z, precision);
    }

    private boolean comparePoint3f(Object wanted, Object real) {
        assert real.getClass() == Point3f.class : "failed assert real.getClass() == Point3f.class: " + real.getClass().getName();
        Point3f pa = (Point3f) wanted;
        Point3f pb = (Point3f) real;
        return HMath.isClose(pa.x, pb.x, precision) && HMath.isClose(pa.y, pb.y, precision) && HMath.isClose(pa.z, pb.z, precision);
    }

    private boolean compareSegment3d(Object wanted, Object real) {
        assert real.getClass() == Segment3d.class : "failed assert real.getClass() == Segment3d.class: " + real.getClass().getName();
        Segment3d sa = (Segment3d) wanted;
        Segment3d sb = (Segment3d) real;
        return comparePoint3d(sa.a, sb.a) && comparePoint3d(sa.b, sb.b);
    }

    private boolean compareSegment3f(Object wanted, Object real) {
        assert real.getClass() == Segment3f.class : "failed assert real.getClass() == Segment3f.class: " + real.getClass().getName();
        Segment3f sa = (Segment3f) wanted;
        Segment3f sb = (Segment3f) real;
        return comparePoint3f(sa.a, sb.a) && comparePoint3f(sa.b, sb.b);
    }

    private boolean compareTriangle3d(Object wanted, Object real) {
        assert real.getClass() == Triangle3d.class : "failed assert real.getClass() == Triangle3d.class: " + real.getClass().getName();
        Triangle3d ta = (Triangle3d) wanted;
        Triangle3d tb = (Triangle3d) real;
        return comparePoint3d(ta.a, tb.a) && comparePoint3d(ta.b, tb.b) && comparePoint3d(ta.c, tb.c);
    }

    private boolean compareTriangle3f(Object wanted, Object real) {
        assert real.getClass() == Triangle3f.class : "failed assert real.getClass() == Triangle3f.class: " + real.getClass().getName();
        Triangle3f ta = (Triangle3f) wanted;
        Triangle3f tb = (Triangle3f) real;
        return comparePoint3f(ta.a, tb.a) && comparePoint3f(ta.b, tb.b) && comparePoint3f(ta.c, tb.c);
    }
}
