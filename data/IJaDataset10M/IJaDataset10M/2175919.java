package jp.go.ipa.jgcl;

import org.magiclight.common.VectorNS;
import java.io.PrintWriter;
import org.magiclight.common.MLUtil;

/**
 *  Q     :  |     C     \   N   X B
 * <p>
 *  |     C     A   _     points   
 *        `           \   t   O closed
 *      `       B
 * </p>
 * <p>
 * closed    l   true                `         Afalse          J     `                 B
 *        `     |     C     A
 *                    _                                   B
 * </p>
 * <p>
 *  |     C     p     [ ^   `     A
 *            _       p     [ ^                   1        A
 *      S       [0, N]        B
 *        N    A
 *  |     C     J     `           (   _     - 1) A
 *        `           (   _    )        B
 * </p>
 * <p>
 * t    p     [ ^       |     C   P(t)    p     g   b N \     A           B
 * <pre>
 *	P(t) = (1 - (t - s)) * points[s] + (t - s) * points[s + 1]
 * </pre>
 *        s    t    z                 B
 * </p>
 *
 * @version $Revision: 1.83 $, $Date: 2000/08/11 06:19:00 $
 * @author Information-technology Promotion Agency, Japan
 */
public final class Polyline2D extends BoundedCurve2D {

    /**
	 *      I             _       I           ` F b N                   t   O
	 * <p>
	 *              false (         ` F b N      )            B
	 * </p>
	 */
    private static final boolean CHECK_SAME_POINTS = false;

    private static final long serialVersionUID = 1L;

    /**
	 *      p     [ ^ l           Z O     g       I   p     [ ^ l   \       N   X
	 */
    private class PolyParam {

        /**
		 *  Z O     g   J n _
		 */
        Pnt2D sp;

        /**
		 *  Z O     g   I   _
		 */
        Pnt2D ep;

        /**
		 *      I   p     [ ^ l
		 */
        double weight;

        /**
		 * (   K        )      I   p     [ ^ l
		 */
        double param;

        /**
		 *  Z O     g       (0  x [ X)
		 */
        int index;
    }

    /**
	 *    _   z   B
	 * @serial
	 */
    private Pnt2D[] points;

    /**
	 *        `           \   t   O B
	 * @serial
	 */
    private boolean closed;

    /**
	 *      C   X ^   X   t B [   h   l           B
	 * <p>
	 * closed    false        A
	 * points    v f     2
	 * ExceptionGeometryInvalidArgumentValue      O           B
	 * </p>
	 * <p>
	 * closed    true        A
	 * points    v f     3
	 * ExceptionGeometryInvalidArgumentValue      O           B
	 * </p>
	 *
	 * @param points	   _   z
	 * @param closed	                   \   t   O
	 * @see	ExceptionGeometryInvalidArgumentValue
	 */
    private void setPoints(Pnt2D[] points, boolean closed) {
        if (!closed && points.length < 2 || closed && points.length < 3) {
            throw new ExceptionGeometryInvalidArgumentValue();
        }
        this.closed = closed;
        this.points = new Pnt2D[points.length];
        this.points[0] = points[0];
        for (int i = 1; i < points.length; i++) {
            if (CHECK_SAME_POINTS) {
                if (points[i].identical(points[i - 1])) {
                    throw new ExceptionGeometryInvalidArgumentValue();
                }
            }
            this.points[i] = points[i];
        }
        if (CHECK_SAME_POINTS) {
            if (closed && points[0].identical(points[points.length - 1])) {
                throw new ExceptionGeometryInvalidArgumentValue();
            }
        }
    }

    /**
	 *    _         `           \   t   O   ^     I u W F N g   \ z     B
	 * <p>
	 * closed    false        A
	 * points    v f     2
	 * ExceptionGeometryInvalidArgumentValue      O           B
	 * </p>
	 * <p>
	 * closed    true        A
	 * points    v f     3
	 * ExceptionGeometryInvalidArgumentValue      O           B
	 * </p>
	 *
	 * @param points	   _   z
	 * @param closed	                   \   t   O
	 * @see	ExceptionGeometryInvalidArgumentValue
	 */
    public Polyline2D(Pnt2D[] points, boolean closed) {
        super();
        setPoints(points, closed);
    }

    /**
	 *    _   ^     J     `         I u W F N g   \ z     B
	 * <p>
	 * points    v f     2
	 * ExceptionGeometryInvalidArgumentValue      O           B
	 * </p>
	 *
	 * @param points	   _   z
	 * @see	ExceptionGeometryInvalidArgumentValue
	 */
    public Polyline2D(Pnt2D[] points) {
        super();
        setPoints(points, false);
    }

    /**
	 *  ^         L         w       e                             I u W F N g   \ z     B
	 *
	 * @param curve	 L
	 * @param tol	         e
	 * @see	BoundedCurve2D#toPolyline(ToleranceForDistance)
	 */
    public Polyline2D(BoundedCurve2D curve, ToleranceForDistance tol) {
        super();
        Polyline2D pl = curve.toPolyline(tol);
        this.points = pl.points;
        this.closed = pl.closed;
    }

    /**
	 *  ^               w           w       e                             I u W F N g   \ z     B
	 *
	 * @param curve
	 * @param pint	             p     [ ^
	 * @param tol	         e
	 * @see ParametricCurve2D#toPolyline(ParameterSection, ToleranceForDistance)
	 */
    public Polyline2D(ParametricCurve2D curve, ParameterSection pint, ToleranceForDistance tol) {
        super();
        Polyline2D pl = curve.toPolyline(pint, tol);
        this.points = pl.points;
        this.closed = pl.closed;
    }

    /**
	 *      |     C       _   z         B
	 *
	 * @return	   _   z
	 */
    public Pnt2D[] points() {
        Pnt2D[] pnts = new Pnt2D[points.length];
        for (int i = 0; i < points.length; i++) {
            pnts[i] = points[i];
        }
        return pnts;
    }

    /**
	 *      |     C     i          _       B
	 * <p>
	 *      |     C           `     Ai      _                   A0          _       B
	 * </p>
	 *
	 * @param i
	 * @return	i          _
	 */
    public Pnt2D pointAt(int i) {
        if (closed() && i == nPoints()) {
            return points[0];
        }
        return points[i];
    }

    /**
	 *      |     C           `                     B
	 *
	 * @return	       `           true A           false
	 */
    public boolean closed() {
        return this.closed;
    }

    /**
	 *      |     C       _           B
	 *
	 * @return	   _
	 */
    public int nPoints() {
        return points.length;
    }

    /**
	 *      |     C     Z O     g           B
	 * <p>
	 *      |     C           `           A
	 *  Z O     g         _             B
	 *  J     `           (   _     - 1)        B
	 * </p>
	 *
	 * @return	 Z O     g
	 */
    public int nSegments() {
        if (closed()) {
            return nPoints();
        }
        return nPoints() - 1;
    }

    /**
	 *  ^         p     [ ^ l           Z O     g       I   p     [ ^ l             B
	 * <p>
	 * param    l   i.0            i        Z O     g             B
	 *        Ai    Z O     g                     (i - 1)        Z O     g             B
	 * </p>
	 * <p>
	 *  ^         p     [ ^         `     O                 A
	 * ExceptionGeometryParameterOutOfRange      O           B
	 * </p>
	 *
	 * @param param	 p     [ ^ l
	 * @return	param            Z O     g       I   p     [ ^ l
	 * @see	ParametricCurve#checkValidity(double)
	 * @see	ExceptionGeometryParameterOutOfRange
	 */
    private PolyParam checkParameter(double param) {
        PolyParam p = new PolyParam();
        int n = closed ? points.length : points.length - 1;
        if (closed) {
            param = parameterDomain().wrap(param);
        } else {
            checkValidity(param);
        }
        int idx = (int) Math.floor(param);
        if (idx < 0) {
            idx = 0;
        }
        if (n - 1 < idx) {
            idx = n - 1;
        }
        p.sp = points[idx];
        if (idx + 1 == points.length) {
            p.ep = points[0];
        } else {
            p.ep = points[idx + 1];
        }
        p.weight = param - idx;
        p.param = param;
        p.index = idx;
        return p;
    }

    /**
	 *                p     [ ^               Z O     g     C           {             N   X B
	 */
    private abstract class LineSegmentAccumulator {

        /**
		 *  ^           _         A               s           \ b h B
		 * <p>
		 *        \ b h
		 * {@link #accumulate(ParameterSection) accumulate(ParameterSection)}
		 *            o       B
		 * </p>
		 * <p>
		 *  ^           _   A
		 *          Z O     g   [ _                   A
		 *      Z O     g       _                   B
		 * </p>
		 *
		 * @param sp	 J n _
		 * @param ep	 I   _
		 * @param sParam	sp            p     [ ^ l
		 * @param eParam	ep            p     [ ^ l
		 */
        abstract void doit(Pnt2D sp, Pnt2D ep, double sParam, double eParam);

        /**
		 *                n                   s           \ b h B
		 * <p>
		 *        \ b h
		 * {@link #accumulate(ParameterSection) accumulate(ParameterSection)}
		 *
		 * {@link #doit(Pnt2D, Pnt2D, double, double)
		 * doit(Pnt2D, Pnt2D, double, double)}
		 *        o   O       o       B
		 * </p>
		 */
        abstract void allocate(int nsegs);

        /**
		 *  ^         p     [ ^               Z O     g
		 * {@link #doit(Pnt2D, Pnt2D, double, double)
		 * doit(Pnt2D, Pnt2D, double, double)}
		 *        o   B
		 */
        void accumulate(ParameterSection pint) {
            PolyParam sPolyParam = checkParameter(pint.start());
            PolyParam ePolyParam = checkParameter(pint.end());
            Pnt2D sPoint;
            Pnt2D ePoint;
            ParameterDomain domain = parameterDomain();
            boolean wrapAround;
            if (domain.isPeriodic()) {
                wrapAround = (sPolyParam.param > ePolyParam.param);
            } else {
                wrapAround = false;
            }
            if (wrapAround) {
                allocate(nPoints() - sPolyParam.index + ePolyParam.index);
                sPoint = coordinates(sPolyParam.param);
                ePoint = sPolyParam.ep;
                doit(sPoint, ePoint, sPolyParam.param, (double) sPolyParam.index + 1);
                for (int seg = sPolyParam.index + 1; seg < nPoints(); seg++) {
                    int wrappedSeg1 = (seg + 1) % nPoints();
                    doit(points[seg], points[wrappedSeg1], (double) seg, (double) (seg + 1));
                }
                for (int seg = 0; seg < ePolyParam.index; seg++) {
                    int wrappedSeg1 = (seg + 1) % nPoints();
                    doit(points[seg], points[wrappedSeg1], (double) seg, (double) (seg + 1));
                }
                sPoint = ePolyParam.sp;
                ePoint = coordinates(ePolyParam.param);
                doit(sPoint, ePoint, (double) ePolyParam.index, ePolyParam.param);
            } else {
                if (sPolyParam.index == ePolyParam.index) {
                    allocate(1);
                    sPoint = coordinates(sPolyParam.param);
                    ePoint = coordinates(ePolyParam.param);
                    doit(sPoint, ePoint, sPolyParam.param, ePolyParam.param);
                } else {
                    allocate(ePolyParam.index - sPolyParam.index + 1);
                    sPoint = coordinates(sPolyParam.param);
                    ePoint = sPolyParam.ep;
                    doit(sPoint, ePoint, sPolyParam.param, (double) sPolyParam.index + 1);
                    for (int seg = sPolyParam.index + 1; seg < ePolyParam.index; seg++) {
                        doit(points[seg], points[seg + 1], (double) seg, (double) (seg + 1));
                    }
                    sPoint = ePolyParam.sp;
                    ePoint = coordinates(ePolyParam.param);
                    doit(sPoint, ePoint, (double) ePolyParam.index, ePolyParam.param);
                }
            }
        }
    }

    /**
	 * {@link #length(ParameterSection) length(ParameterSection)}
	 *                  LineSegmentAccumulator        B
	 */
    private class LengthAccumulator extends LineSegmentAccumulator {

        /**
		 *        w               B
		 */
        double leng;

        /**
		 * leng    0              B
		 *
		 * @param nsegs	           Z O     g
		 */
        void allocate(int nsegs) {
            leng = 0.0;
        }

        /**
		 *  ^           _           leng        B
		 *
		 * @param sp	 J n _
		 * @param ep	 I   _
		 * @param sParam	sp            p     [ ^ l
		 * @param eParam	ep            p     [ ^ l
		 */
        void doit(Pnt2D sp, Pnt2D ep, double sParam, double eParam) {
            leng += sp.distance(ep);
        }

        /**
		 *        w                     B
		 *
		 * @param leng    l
		 */
        double extract() {
            return leng;
        }
    }

    /**
	 *  ^         p     [ ^                                       (      )        B
	 * <p>
	 * pint        l                   B
	 * </p>
	 * <p>
	 *  ^         p     [ ^         `     O                 A
	 * ExceptionGeometryParameterOutOfRange      O           B
	 * </p>
	 *
	 * @param pint	                   p     [ ^
	 * @return	 w         p     [ ^
	 * @see	ExceptionGeometryParameterOutOfRange
	 */
    public double length(ParameterSection pint) {
        if (pint.increase() < 0.0) {
            return length(pint.reverse());
        }
        LengthAccumulator acc = new LengthAccumulator();
        acc.accumulate(pint);
        return acc.extract();
    }

    /**
	 *            A ^         p     [ ^ l       W l       B
	 * <p>
	 *  ^         p     [ ^         `     O                 A
	 * ExceptionGeometryParameterOutOfRange      O           B
	 * </p>
	 *
	 * @param param	 p     [ ^ l
	 * @return		   W l
	 * @see	ParametricCurve#checkValidity(double)
	 * @see	ExceptionGeometryParameterOutOfRange
	 */
    public Pnt2D coordinates(double param) {
        PolyParam p = checkParameter(param);
        return p.ep.linearInterpolate(p.sp, p.weight);
    }

    /**
	 *            A ^         p     [ ^ l       x N g         B
	 * <p>
	 *  ^         p     [ ^         `     O                 A
	 * ExceptionGeometryParameterOutOfRange      O           B
	 * </p>
	 *
	 * @param param	 p     [ ^ l
	 * @return		   x N g
	 * @see	ParametricCurve#checkValidity(double)
	 * @see	ExceptionGeometryParameterOutOfRange
	 */
    public Vec2D tangentVector(double param) {
        PolyParam p = checkParameter(param);
        return p.ep.subtract(p.sp);
    }

    /**
	 *            A ^         p     [ ^ l               B
	 * <p>
	 *  |     C           A     0        B
	 * </p>
	 * <p>
	 *  ^         p     [ ^         `     O                 A
	 * ExceptionGeometryParameterOutOfRange      O           B
	 * </p>
	 *
	 * @param param	 p     [ ^ l
	 * @return
	 * @see	ParametricCurve#checkValidity(double)
	 * @see	ExceptionGeometryParameterOutOfRange
	 */
    public CurveCurvature2D curvature(double param) {
        checkParameter(param);
        return new CurveCurvature2D(0.0, Vec2D.zeroVector());
    }

    /**
	 *            A ^         p     [ ^ l                 B
	 * <p>
	 *  ^         p     [ ^         `     O                 A
	 * ExceptionGeometryParameterOutOfRange      O           B
	 * </p>
	 *
	 * @param param	 p     [ ^ l
	 * @return
	 * @see	ParametricCurve#checkValidity(double)
	 * @see	ExceptionGeometryParameterOutOfRange
	 */
    public CurveDerivative2D evaluation(double param) {
        return new CurveDerivative2D(coordinates(param), tangentVector(param), Vec2D.zeroVector());
    }

    /**
	 * {@link #singular() singular()}
	 *                  LineSegmentAccumulator        B
	 */
    private class SingularAccumulator extends LineSegmentAccumulator {

        private ParametricCurve2D curve;

        private VectorNS<PointOnCurve2D> singularVec;

        private Vec2D prevTangVec;

        SingularAccumulator(ParametricCurve2D curve) {
            this.curve = curve;
        }

        void allocate(int nsegs) {
            singularVec = new VectorNS<PointOnCurve2D>();
            prevTangVec = null;
        }

        void doit(Pnt2D sp, Pnt2D ep, double sParam, double eParam) {
            Vec2D tangVec = ep.subtract(sp);
            if (prevTangVec != null) {
                if (!tangVec.identicalDirection(prevTangVec)) {
                    PointOnCurve2D candidate = new PointOnCurve2D(curve, sParam, MLUtil.DEBUG);
                    singularVec.addElement(candidate);
                }
            }
            prevTangVec = tangVec;
        }

        PointOnCurve2D[] extract() {
            PointOnCurve2D[] singular = new PointOnCurve2D[singularVec.size()];
            singularVec.copyInto(singular);
            return singular;
        }
    }

    /**
	 *                _       B
	 * <p>
	 *      _                       0    z         B
	 * </p>
	 *
	 * @return		     _   z
	 */
    public PointOnCurve2D[] singular() {
        SingularAccumulator acc = new SingularAccumulator(this);
        acc.accumulate(parameterDomain().section());
        return acc.extract();
    }

    /**
	 *                _       B
	 * <p>
	 *  |     C           _                       A     0    z         B
	 * </p>
	 *
	 * @return	     _   z
	 */
    public PointOnCurve2D[] inflexion() {
        return new PointOnCurve2D[0];
    }

    /**
	 * {@link #projectFrom(Pnt2D) projectFrom(Pnt2D)}
	 *                  LineSegmentAccumulator        B
	 */
    private class ProjectionAccumulator extends LineSegmentAccumulator {

        PointOnGeometryList projList;

        Pnt2D point;

        double dTol;

        Polyline2D curv;

        ProjectionAccumulator(Polyline2D curv, Pnt2D point, double dTol) {
            this.point = point;
            this.dTol = dTol;
            this.curv = curv;
        }

        void allocate(int nsegs) {
            projList = new PointOnGeometryList();
        }

        void doit(Pnt2D sp, Pnt2D ep, double sParam, double eParam) {
            Line2D line;
            try {
                line = new Line2D(sp, ep);
            } catch (ExceptionGeometryInvalidArgumentValue e) {
                return;
            }
            PointOnCurve2D proj = line.project1From(point);
            double length = line.dir().length();
            double param = proj.parameter();
            double fromSp = param * length;
            if (-dTol <= fromSp && fromSp <= length + dTol) {
                if (param < 0.0) {
                    param = 0.0;
                } else {
                    if (param > 1.0) {
                        param = 1.0;
                    }
                }
                double p2 = sParam + (eParam - sParam) * param;
                projList.addPoint(curv, p2);
            }
        }

        PointOnCurve2D[] extract() {
            return projList.toPointOnCurve2DArray();
        }
    }

    /**
	 *  ^         _                   e _         B
	 * <p>
	 *    e _                       0    z         B
	 * </p>
	 *
	 * @param point	   e     _
	 * @return	   e _
	 */
    public PointOnCurve2D[] projectFrom(Pnt2D point) {
        double dTol = getToleranceForDistance();
        ProjectionAccumulator acc = new ProjectionAccumulator(this, point, dTol);
        try {
            acc.accumulate(parameterDomain().section());
        } catch (ExceptionGeometryParameterOutOfRange e) {
            throw new ExceptionGeometryFatal();
        }
        return acc.extract();
    }

    /**
	 * {@link #toPolyline(ParameterSection, ToleranceForDistance)
	 * toPolyline(ParameterSection, ToleranceForDistance)}
	 *                  LineSegmentAccumulator        B
	 */
    private class ToPolylineAccumulator extends LineSegmentAccumulator {

        VectorNS<Pnt2D> pntVec;

        Pnt2D lastPoint;

        Polyline2D curv;

        ToPolylineAccumulator(Polyline2D curv) {
            this.curv = curv;
        }

        void allocate(int nsegs) {
            pntVec = new VectorNS<Pnt2D>();
            lastPoint = null;
        }

        void doit(Pnt2D sp, Pnt2D ep, double sParam, double eParam) {
            if (lastPoint == null) {
                lastPoint = new PointOnCurve2D(curv, sParam, MLUtil.DEBUG);
                pntVec.addElement(lastPoint);
            }
            Pnt2D newPoint = new PointOnCurve2D(curv, eParam, MLUtil.DEBUG);
            if (!newPoint.identical(lastPoint)) {
                pntVec.addElement(newPoint);
                lastPoint = newPoint;
            }
        }

        Polyline2D extract() {
            int nPnts = pntVec.size();
            if (nPnts < 2) {
                throw new ExceptionGeometryZeroLength();
            }
            Pnt2D[] pntsArray = new Pnt2D[nPnts];
            pntVec.copyInto(pntsArray);
            return new Polyline2D(pntsArray);
        }
    }

    /**
	 *            w           A ^                           |     C         B
	 * <p>
	 *                    |     C     \       _
	 *            x [ X       PointOnCurve2D
	 *                      B
	 * </p>
	 *
	 * @param pint	             p     [ ^
	 * @param tol	         e
	 * @return		           w                       |     C
	 */
    public Polyline2D toPolyline(ParameterSection pint, ToleranceForDistance tol) {
        if (pint.increase() < 0.0) {
            return toPolyline(pint.reverse(), tol).reverse();
        }
        ToPolylineAccumulator acc = new ToPolylineAccumulator(this);
        acc.accumulate(pint);
        return acc.extract();
    }

    /**
	 *      L       S                   L   Bspline            B
	 *
	 * @return	         S             L   Bspline
	 */
    public BsplineCurve2D toBsplineCurve() {
        int degree = 1;
        boolean periodic = this.closed();
        int uicp = this.nPoints();
        int uik = (periodic == false) ? uicp : (uicp + 2);
        int[] knotMultiplicities = new int[uik];
        double[] knots = new double[uik];
        Pnt2D[] controlPoints = new Pnt2D[uicp];
        double[] weights = new double[uicp];
        int ik = (periodic == false) ? 0 : 1;
        if (periodic == false) {
            ik = 0;
        } else {
            ik = 1;
            knots[0] = (-1.0);
            knots[uik - 1] = uicp + 1;
            knotMultiplicities[0] = 1;
            knotMultiplicities[uik - 1] = 1;
        }
        for (int i = 0; i < uicp; i++, ik++) {
            knots[ik] = i;
            if ((periodic == false) && ((i == 0) || (i == (this.nPoints() - 1)))) {
                knotMultiplicities[ik] = 2;
            } else {
                knotMultiplicities[ik] = 1;
            }
            controlPoints[i] = this.pointAt(i);
            weights[i] = 1.0;
        }
        return new BsplineCurve2D(degree, periodic, knotMultiplicities, knots, controlPoints, weights);
    }

    /**
	 *            w                         L   Bspline            B
	 *
	 * @param pint	 L   Bspline                p     [ ^
	 * @return		           w                   L   Bspline
	 */
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
        Polyline2D target;
        if (this.closed() == true) {
            if (pint.absIncrease() >= this.parameterDomain().section().absIncrease()) {
                target = this;
                if (pint.increase() < 0.0) {
                    target = target.reverse();
                }
            } else {
                target = this.toPolyline(pint, this.getToleranceForDistanceAsObject());
            }
        } else {
            target = this.toPolyline(pint, this.getToleranceForDistanceAsObject());
        }
        return target.toBsplineCurve();
    }

    /**
	 *                        _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 *
	 * @param mate
	 * @return		   _   z
	 */
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate) {
        return mate.intersect(this, true);
    }

    /**
	 *      |     C     w     Z O     g               B
	 *
	 * @param nseg	 Z O     g
	 * @param segIdx	             Z O     g       (0  x [ X)
	 * @return	 w     Z O     g
	 */
    private double segLength(int nseg, int segIdx) {
        int head_pnt_idx;
        int tail_pnt_idx;
        if (closed()) {
            while (segIdx < 0) {
                segIdx += nseg;
            }
            while (segIdx > (nseg - 1)) {
                segIdx -= nseg;
            }
        }
        head_pnt_idx = segIdx;
        if (closed() && (head_pnt_idx == nSegments())) {
            tail_pnt_idx = 0;
        } else {
            tail_pnt_idx = head_pnt_idx + 1;
        }
        return points[head_pnt_idx].distance(points[tail_pnt_idx]);
    }

    /**
	 *                        _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 * <ul>
	 * <li>	 e Z O     g         A   [ _             l   A                         _         B
	 * <li>	                   _       A         Z O     g                   u   _ v       B
	 * </ul>
	 * </p>
	 *
	 * @param mate
	 * @param doExchange	   _   pointOnCurve1/2
	 * @return		   _   z
	 */
    private IntersectionPoint2D[] doIntersect(ParametricCurve2D mate, boolean doExchange) {
        int nSeg = nSegments();
        CurveCurveInterferenceList intf = new CurveCurveInterferenceList(this, mate);
        for (int i = 0; i < nSeg; i++) {
            Line2D realSegment;
            BoundedLine2D segment;
            try {
                realSegment = new Line2D(pointAt(i), pointAt(i + 1));
                segment = new BoundedLine2D(pointAt(i), pointAt(i + 1));
            } catch (ExceptionGeometryInvalidArgumentValue e) {
                continue;
            }
            IntersectionPoint2D[] segIntersect;
            try {
                segIntersect = realSegment.intersect(mate);
            } catch (ExceptionGeometryIndefiniteSolution e) {
                segIntersect = new IntersectionPoint2D[1];
                Pnt2D segmentMidPoint = realSegment.coordinates(0.5);
                double paramOnMate = mate.pointToParameter(segmentMidPoint);
                segIntersect[0] = new IntersectionPoint2D(realSegment, 0.5, mate, paramOnMate, MLUtil.DEBUG);
            }
            if ((segIntersect == null) || (segIntersect.length == 0)) {
                continue;
            }
            int segResolution = segIntersect.length;
            for (int j = 0; j < segResolution; j++) {
                Pnt2D point = segIntersect[j].coordinates();
                double segParam = segIntersect[j].pointOnCurve1().parameter();
                ParameterValidityType validity = segment.parameterValidity(segParam);
                switch(validity) {
                    case OUTSIDE:
                        continue;
                    case TOLERATED_LOWER_LIMIT:
                        if (segParam < 0.0) {
                            if ((!closed()) && (i == 0)) {
                                segParam = 0.0;
                            } else {
                                segParam = segLength(nSeg, i) * segParam / segLength(nSeg, (i - 1));
                            }
                        }
                        break;
                    case TOLERATED_UPPER_LIMIT:
                        if (segParam > 1.0) {
                            if ((!closed()) && (i == (nSeg - 1))) {
                                segParam = 1.0;
                            } else {
                                segParam = 1.0 + (segLength(nSeg, i) * (segParam - 1.0)) / segLength(nSeg, (i + 1));
                            }
                        }
                        break;
                    default:
                        break;
                }
                intf.addAsIntersection(point, segParam + i, segIntersect[j].pointOnCurve2().parameter());
            }
        }
        return intf.toIntersectionPoint2DArray(doExchange);
    }

    /**
	 *      |     C             (    )      _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 * this.{@link #doIntersect(ParametricCurve2D, boolean)
	 * doIntersect}(mate, doExchange)              B
	 * </p>
	 *
	 * @param mate	         (    )
	 * @param doExchange	   _   pointOnCurve1/2
	 * @return	   _   z
	 */
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
	 *      |     C             ( ~)      _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 * this.{@link #doIntersect(ParametricCurve2D, boolean)
	 * doIntersect}(mate, doExchange)              B
	 * </p>
	 *
	 * @param mate	         ( ~)
	 * @param doExchange	   _   pointOnCurve1/2
	 * @return	   _   z
	 */
    IntersectionPoint2D[] intersect(Circle2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
	 *      |     C             (   ~)      _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 * this.{@link #doIntersect(ParametricCurve2D, boolean)
	 * doIntersect}(mate, doExchange)              B
	 * </p>
	 *
	 * @param mate	         (   ~)
	 * @param doExchange	   _   pointOnCurve1/2
	 * @return	   _   z
	 */
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
	 *      |     C             (      )      _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 * this.{@link #doIntersect(ParametricCurve2D, boolean)
	 * doIntersect}(mate, doExchange)              B
	 * </p>
	 *
	 * @param mate	         (      )
	 * @param doExchange	   _   pointOnCurve1/2
	 * @return	   _   z
	 */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
	 *      |     C             ( o    )      _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 * this.{@link #doIntersect(ParametricCurve2D, boolean)
	 * doIntersect}(mate, doExchange)              B
	 * </p>
	 *
	 * @param mate	         ( o    )
	 * @param doExchange	   _   pointOnCurve1/2
	 * @return	   _   z
	 */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
	 *      |     C             ( |     C  )      _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 *          Z   A
	 * {@link IntsPolPol2D#intersection(Polyline2D, Polyline2D, boolean)
	 * IntsPolPol2D.intersection}(this, mate, doExchange)
	 *    s           B
	 * </p>
	 *
	 * @param mate	         ( |     C  )
	 * @param doExchange	   _   pointOnCurve1/2
	 * @return	   _   z
	 */
    IntersectionPoint2D[] intersect(Polyline2D mate, boolean doExchange) {
        return IntsPolPol2D.intersection(this, mate, doExchange);
    }

    /**
	 *      |     C             ( x W G    )      _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 * this.{@link #doIntersect(ParametricCurve2D, boolean)
	 * doIntersect}(mate, doExchange)              B
	 * </p>
	 *
	 * @param mate	         ( x W G    )
	 * @param doExchange	   _   pointOnCurve1/2
	 * @return	   _   z
	 */
    IntersectionPoint2D[] intersect(PureBezierCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
	 *      |     C             ( a X v   C      )      _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 * this.{@link #doIntersect(ParametricCurve2D, boolean)
	 * doIntersect}(mate, doExchange)              B
	 * </p>
	 *
	 * @param mate	         ( a X v   C      )
	 * @param doExchange	   _   pointOnCurve1/2
	 * @return	   _   z
	 */
    IntersectionPoint2D[] intersect(BsplineCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
	 *      |     C             ( g        )      _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 *          Z   A
	 *  g           N   X   u g         vs.  |     C   v     _   Z   \ b h
	 * {@link TrimmedCurve2D#intersect(Polyline2D, boolean)
	 * TrimmedCurve2D.intersect(Polyline2D, boolean)}
	 *    s           B
	 * </p>
	 *
	 * @param mate	         ( g        )
	 * @param doExchange	   _   pointOnCurve1/2
	 * @return	   _   z
	 */
    IntersectionPoint2D[] intersect(TrimmedCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
	 *      |     C             (         Z O     g)      _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 *          Z   A
	 *          Z O     g   N   X   u         Z O     g vs.  |     C   v     _   Z   \ b h
	 * {@link CompositeCurveSegment2D#intersect(Polyline2D, boolean)
	 * CompositeCurveSegment2D.intersect(Polyline2D, boolean)}
	 *    s           B
	 * </p>
	 *
	 * @param mate	         (         Z O     g)
	 * @param doExchange	   _   pointOnCurve1/2
	 * @return	   _   z
	 */
    IntersectionPoint2D[] intersect(CompositeCurveSegment2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
	 *      |     C             (        )      _         B
	 * <p>
	 *    _                       0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 *          Z   A
	 *            N   X   u         vs.  |     C   v     _   Z   \ b h
	 * {@link CompositeCurve2D#intersect(Polyline2D, boolean)
	 * CompositeCurve2D.intersect(Polyline2D, boolean)}
	 *    s           B
	 * </p>
	 *
	 * @param mate	         (        )
	 * @param doExchange	   _   pointOnCurve1/2
	 * @return	   _   z
	 */
    IntersectionPoint2D[] intersect(CompositeCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
	 *      L             L E                   B
	 * <p>
	 *                              0    z         B
	 * </p>
	 *
	 * @param mate
	 * @return		               z
	 */
    public CurveCurveInterference2D[] interfere(BoundedCurve2D mate) {
        return mate.interfere(this, true);
    }

    /**
	 *      L             L       (    )                B
	 * <p>
	 *                            0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 *          Z   A
	 *        N   X   u     vs.  |     C   v         Z   \ b h
	 * {@link BoundedLine2D#interfere(Polyline2D, boolean)
	 * BoundedLine2D.interfere(Polyline2D, boolean)}
	 *    s           B
	 * </p>
	 *
	 * @param mate	     L       (    )
	 * @param doExchange	             this    mate      u
	 * @return		               z
	 */
    CurveCurveInterference2D[] interfere(BoundedLine2D mate, boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
	 *      L             L       ( |     C  )                B
	 * <p>
	 *                            0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 *          Z   A
	 * {@link IntsPolPol2D#interference(Polyline2D, Polyline2D)
	 * IntsPolPol2D.interference}(this, mate)
	 *
	 * IntsPolPol2D.interference(mate, this)
	 *    s           B
	 * </p>
	 *
	 * @param mate	         ( |     C  )
	 * @param doExchange	   G         this    mate      u
	 * @return		               z
	 */
    CurveCurveInterference2D[] interfere(Polyline2D mate, boolean doExchange) {
        if (!doExchange) {
            return IntsPolPol2D.interference(this, mate);
        } else {
            return IntsPolPol2D.interference(mate, this);
        }
    }

    /**
	 *  ^                                   |     C       X     B
	 *
	 * @param sourceInterferences	       z
	 * @param doExchange	   G         this    mate      u
	 * @return		               z
	 */
    private CurveCurveInterference2D[] convertInterferences(CurveCurveInterference2D[] sourceInterferences, boolean doExchange) {
        VectorNS<CurveCurveInterference2D> resultVector = new VectorNS<CurveCurveInterference2D>();
        for (int i = 0; i < sourceInterferences.length; i++) {
            CurveCurveInterference2D intf;
            if (!doExchange) {
                intf = sourceInterferences[i].changeCurve1(this);
            } else {
                intf = sourceInterferences[i].changeCurve2(this);
            }
            if (intf != null) {
                resultVector.addElement(intf);
            }
        }
        CurveCurveInterference2D[] result = new CurveCurveInterference2D[resultVector.size()];
        resultVector.copyInto(result);
        return result;
    }

    /**
	 *      L             L       ( x W G    )                B
	 * <p>
	 *                            0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 *      |     C     L   a X v   C               A
	 *  a X v   C         N   X   u a X v   C       vs.  x W G     v         Z   \ b h
	 * {@link BsplineCurve2D#interfere(PureBezierCurve2D, boolean)
	 * BsplineCurve2D.interfere(PureBezierCurve2D, boolean)}
	 *        o         B
	 * </p>
	 *
	 * @param mate	     L       ( x W G    )
	 * @param doExchange	             this    mate      u
	 * @return		               z
	 */
    CurveCurveInterference2D[] interfere(PureBezierCurve2D mate, boolean doExchange) {
        return this.convertInterferences(this.toBsplineCurve().interfere(mate, doExchange), doExchange);
    }

    /**
	 *      L             L       ( a X v   C      )                B
	 * <p>
	 *                            0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 *      |     C     L   a X v   C               A
	 *  a X v   C         N   X   u a X v   C       vs.  a X v   C       v         Z   \ b h
	 * {@link BsplineCurve2D#interfere(BsplineCurve2D, boolean)
	 * BsplineCurve2D.interfere(BsplineCurve2D, boolean)}
	 *        o         B
	 * </p>
	 *
	 * @param mate	     L       ( a X v   C      )
	 * @param doExchange	             this    mate      u
	 * @return		               z
	 */
    CurveCurveInterference2D[] interfere(BsplineCurve2D mate, boolean doExchange) {
        return this.convertInterferences(this.toBsplineCurve().interfere(mate, doExchange), doExchange);
    }

    /**
	 *      L             L       ( g        )                B
	 * <p>
	 *                            0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 *          Z   A
	 *  g           N   X   u g         vs.  |     C   v         Z   \ b h
	 * {@link TrimmedCurve2D#interfere(Polyline2D, boolean)
	 * TrimmedCurve2D.interfere(Polyline2D, boolean)}
	 *    s           B
	 * </p>
	 *
	 * @param mate	     L       ( g        )
	 * @param doExchange	             this    mate      u
	 * @return		               z
	 */
    CurveCurveInterference2D[] interfere(TrimmedCurve2D mate, boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
	 *      L             L       (         Z O     g)                B
	 * <p>
	 *                            0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 *          Z   A
	 *          Z O     g   N   X   u         Z O     g vs.  |     C   v         Z   \ b h
	 * {@link CompositeCurveSegment2D#interfere(Polyline2D, boolean)
	 * CompositeCurveSegment2D.interfere(Polyline2D, boolean)}
	 *    s           B
	 * </p>
	 *
	 * @param mate	     L       (         Z O     g)
	 * @param doExchange	             this    mate      u
	 * @return		               z
	 */
    CurveCurveInterference2D[] interfere(CompositeCurveSegment2D mate, boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
	 *      L             L       (        )                B
	 * <p>
	 *                            0    z         B
	 * </p>
	 * <p>
	 * [        ]
	 * <br>
	 *          Z   A
	 *            N   X   u         vs.  |     C   v         Z   \ b h
	 * {@link CompositeCurve2D#interfere(Polyline2D, boolean)
	 * CompositeCurve2D.interfere(Polyline2D, boolean)}
	 *    s           B
	 * </p>
	 *
	 * @param mate	     L       (        )
	 * @param doExchange	             this    mate      u
	 * @return		               z
	 */
    CurveCurveInterference2D[] interfere(CompositeCurve2D mate, boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
	 *      |     C             _         B
	 * <p>
	 *                                0    z         B
	 * </p>
	 *
	 * @return	         _   z
	 */
    public IntersectionPoint2D[] selfIntersect() {
        return SelfIntsPol2D.intersection(this);
    }

    /**
	 *      |     C                     B
	 * <p>
	 *                                0    z         B
	 * </p>
	 *
	 * @return		           z
	 */
    public CurveCurveInterference2D[] selfInterfere() {
        return SelfIntsPol2D.interference(this);
    }

    /**
	 *            w           I t Z b g           A
	 *  ^                                       B
	 * <p>
	 *                                  |     C     I t Z b g                         B
	 *        A       \ b h           tol    l   Q         B
	 * </p>
	 *
	 * @param pint	 I t Z b g     p     [ ^
	 * @param magni	 I t Z b g
	 * @param side       I t Z b g       (WhichSide.LEFT/RIGHT)
	 * @param tol	         e
	 * @return		           w           I t Z b g
	 * @see	WhichSide
	 */
    public CompositeCurve2D offsetByCompositeCurve(ParameterSection pint, double magni, int side, ToleranceForDistance tol) {
        boolean offsettedIsPeriodic = false;
        if (this.isPeriodic() == true) {
            if ((this.parameterDomain().section().absIncrease() - pint.absIncrease()) < this.getToleranceForParameter()) {
                offsettedIsPeriodic = true;
            }
        }
        BoundedLine2D[] boundedLines = this.toBoundedLines(pint);
        int nBoundedLines = boundedLines.length;
        CompositeCurveSegment2D[] offsetted = new CompositeCurveSegment2D[2 * nBoundedLines];
        BoundedLine2D prevOffsettedCurve = null;
        BoundedLine2D crntOffsettedCurve = null;
        BoundedLine2D firstOffsettedCurve = null;
        int transition;
        for (int i = 0; i <= nBoundedLines; i++) {
            if (i == nBoundedLines) {
                crntOffsettedCurve = firstOffsettedCurve;
            } else {
                crntOffsettedCurve = (BoundedLine2D) boundedLines[i].offsetByBoundedCurve(magni, side, tol);
                if (i == 0) {
                    firstOffsettedCurve = crntOffsettedCurve;
                }
                transition = TransitionCodeType.CONTINUOUS;
                if ((offsettedIsPeriodic == false) && (i == (nBoundedLines - 1))) {
                    transition = TransitionCodeType.DISCONTINUOUS;
                }
                offsetted[2 * i] = new CompositeCurveSegment2D(transition, true, crntOffsettedCurve);
            }
            if (i == 0) {
                ;
            } else {
                if ((i == nBoundedLines) && (offsettedIsPeriodic == false)) {
                    offsetted[2 * i - 1] = null;
                } else {
                    if (prevOffsettedCurve.epnt().identical(crntOffsettedCurve.spnt()) == true) {
                        offsetted[2 * i - 1] = null;
                    } else {
                        Pnt2D center = (i < nBoundedLines) ? boundedLines[i].spnt() : boundedLines[0].spnt();
                        TrimmedCurve2D offsettedCorner = Circle2D.makeTrimmedCurve(center, prevOffsettedCurve.epnt(), crntOffsettedCurve.spnt());
                        transition = TransitionCodeType.CONTINUOUS;
                        offsetted[2 * i - 1] = new CompositeCurveSegment2D(transition, true, offsettedCorner);
                    }
                }
            }
            prevOffsettedCurve = crntOffsettedCurve;
        }
        VectorNS<CompositeCurveSegment2D> listOfOffsetted = new VectorNS<CompositeCurveSegment2D>();
        for (int i = 0; i < (2 * nBoundedLines); i++) {
            if (offsetted[i] != null) {
                listOfOffsetted.addElement(offsetted[i]);
            }
        }
        offsetted = new CompositeCurveSegment2D[listOfOffsetted.size()];
        listOfOffsetted.copyInto(offsetted);
        return new CompositeCurve2D(offsetted, offsettedIsPeriodic);
    }

    /**
	 *            w           I t Z b g           A
	 *  ^                       Bspline              B
	 * <p>
	 *                    Bspline            |     C     I t Z b g                         B
	 *        A       \ b h           tol    l   Q         B
	 * </p>
	 *
	 * @param pint	 I t Z b g     p     [ ^
	 * @param magni	 I t Z b g
	 * @param side       I t Z b g       (WhichSide.LEFT/RIGHT)
	 * @param tol	         e
	 * @return		           w           I t Z b g               Bspline
	 * @see	WhichSide
	 */
    public BsplineCurve2D offsetByBsplineCurve(ParameterSection pint, double magni, int side, ToleranceForDistance tol) {
        CompositeCurve2D cmc = this.offsetByCompositeCurve(pint, magni, side, tol);
        return cmc.toBsplineCurve();
    }

    /**
	 *            w           I t Z b g           A
	 *  ^                       L               B
	 * <p>
	 *                    L             |     C     I t Z b g                         B
	 *        A       \ b h           tol    l   Q         B
	 * </p>
	 *
	 * @param pint	 I t Z b g     p     [ ^
	 * @param magni	 I t Z b g
	 * @param side       I t Z b g       (WhichSide.LEFT/RIGHT)
	 * @param tol	         e
	 * @return		           w           I t Z b g               L
	 * @see	WhichSide
	 */
    public BoundedCurve2D offsetByBoundedCurve(ParameterSection pint, double magni, int side, ToleranceForDistance tol) {
        return this.offsetByCompositeCurve(pint, magni, side, tol);
    }

    /**
	 * {@link #doFillet(ParameterSection, int, ParametricCurve2D, ParameterSection, int, double, boolean)
	 * doFillet(ParameterSection, int, ParametricCurve2D, ParameterSection, int, double, boolean)}
	 *                  LineSegmentAccumulator        B
	 */
    private class FilletAccumulator extends LineSegmentAccumulator {

        ParametricCurve2D mate;

        Polyline2D curve;

        ParameterSection mateSec;

        int mateSide;

        int mySide;

        double radius;

        boolean doExchange;

        FilletObjectList fltList;

        ParameterSection sec;

        FilletAccumulator(ParametricCurve2D mate, ParameterSection mateSec, int mateSide, Polyline2D curve, int mySide, double radius, boolean doExchange) {
            this.curve = curve;
            this.mate = mate;
            this.mateSec = mateSec;
            this.mateSide = mateSide;
            this.mySide = mySide;
            this.radius = radius;
            this.doExchange = doExchange;
            sec = new ParameterSection(0.0, 1.0);
        }

        void allocate(int nsegs) {
            fltList = new FilletObjectList();
        }

        void doit(Pnt2D sp, Pnt2D ep, double sParam, double eParam) {
            BoundedLine2D blin = new BoundedLine2D(sp, ep);
            FilletObject2D[] flts;
            try {
                flts = blin.doFillet(sec, mySide, mate, mateSec, mateSide, radius, false);
            } catch (ExceptionGeometryIndefiniteSolution e) {
                flts = new FilletObject2D[1];
                flts[0] = (FilletObject2D) e.suitable();
            }
            FilletObject2D thisFlt;
            for (int i = 0; i < flts.length; i++) {
                double param = sParam + (eParam - sParam) * flts[i].pointOnCurve1().parameter();
                PointOnCurve2D thisPnt = new PointOnCurve2D(curve, param, MLUtil.DEBUG);
                if (!doExchange) {
                    thisFlt = new FilletObject2D(radius, flts[i].center(), thisPnt, flts[i].pointOnCurve2());
                } else {
                    thisFlt = new FilletObject2D(radius, flts[i].center(), flts[i].pointOnCurve2(), thisPnt);
                }
                fltList.addFillet(thisFlt);
            }
        }

        FilletObject2D[] extract() {
            return fltList.toFilletObject2DArray(false);
        }
    }

    /**
	 *      |     C     w                     w                 t B   b g         B
	 *
	 * @param pint1	           p     [ ^
	 * @param side1	                     t B   b g                 t   O
	 *			(WhichSide.LEFT           ARIGHT       E   ABOTH          )
	 * @param mate
	 * @param pint2	           p     [ ^
	 * @param side2	                     t B   b g                 t   O
	 *			(WhichSide.LEFT           ARIGHT       E   ABOTH          )
	 * @param radius	 t B   b g   a
	 * @param doExchange	 t B   b g   point1/2
	 * @return		 t B   b g   z
	 * @exception ExceptionGeometryIndefiniteSolution	   s   (                        )
	 * @see	WhichSide
	 */
    FilletObject2D[] doFillet(ParameterSection pint1, int side1, ParametricCurve2D mate, ParameterSection pint2, int side2, double radius, boolean doExchange) throws ExceptionGeometryIndefiniteSolution {
        FilletAccumulator acc = new FilletAccumulator(mate, pint2, side2, this, side1, radius, doExchange);
        acc.accumulate(pint1);
        return acc.extract();
    }

    /**
	 *                                        B
	 * <p>
	 *                                  0    z         B
	 * </p>
	 * <p>
	 *      _                         A
	 * ExceptionGeometryNotSupported	     O           B
	 * </p>
	 *
	 * @param mate
	 * @return		           z
	 * @exception	ExceptionGeometryNotSupported	             A             @ \
	 */
    public CommonTangent2D[] commonTangent(ParametricCurve2D mate) {
        throw new ExceptionGeometryNotSupported();
    }

    /**
	 *                            @           B
	 * <p>
	 *      @                           0    z         B
	 * </p>
	 * <p>
	 *      _                         A
	 * ExceptionGeometryNotSupported	     O           B
	 * </p>
	 *
	 * @param mate
	 * @return		     @     z
	 * @exception	ExceptionGeometryNotSupported	             A             @ \
	 */
    public CommonNormal2D[] commonNormal(ParametricCurve2D mate) {
        throw new ExceptionGeometryNotSupported();
    }

    /**
	 *      |     C       ]     |     C         B
	 *
	 * @return	   ]     |     C
	 */
    Polyline2D reverse() {
        int uip = nPoints();
        Pnt2D[] rPnts = new Pnt2D[uip];
        int i, j;
        if (closed) {
            rPnts[0] = points[0];
            i = 1;
        } else {
            i = 0;
        }
        for (j = uip - 1; i < uip; i++, j--) {
            rPnts[i] = points[j];
        }
        return new Polyline2D(rPnts, closed);
    }

    /**
	 *      |     C                     `       B
	 *
	 * @return	                 `
	 */
    EnclosingBox2D enclosingBox() {
        double min_crd_x;
        double min_crd_y;
        double max_crd_x;
        double max_crd_y;
        max_crd_x = min_crd_x = pointAt(0).x();
        max_crd_y = min_crd_y = pointAt(0).y();
        for (int i = 1; i < nPoints(); i++) {
            min_crd_x = Math.min(min_crd_x, pointAt(i).x());
            min_crd_y = Math.min(min_crd_y, pointAt(i).y());
            max_crd_x = Math.max(max_crd_x, pointAt(i).x());
            max_crd_y = Math.max(max_crd_y, pointAt(i).y());
        }
        return new EnclosingBox2D(min_crd_x, min_crd_y, max_crd_x, max_crd_y);
    }

    /**
	 *            p     [ ^   `         B
	 *
	 * @return	 p     [ ^   `
	 */
    ParameterDomain getParameterDomain() {
        double n = closed ? points.length : points.length - 1;
        try {
            return new ParameterDomain(closed, 0, n);
        } catch (ExceptionGeometryInvalidArgumentValue e) {
            throw new ExceptionGeometryFatal();
        }
    }

    /**
	 *          v f     R `               B
	 *
	 * @return	     true
	 */
    public boolean isFreeform() {
        return true;
    }

    /**
	 *      L         J n _       B
	 * <p>
	 *              `           null        B
	 * </p>
	 *
	 * @return	 J n _
	 */
    public Pnt2D startPoint() {
        if (isPeriodic()) {
            return null;
        }
        return points[0];
    }

    /**
	 *      L         I   _       B
	 * <p>
	 *              `           null        B
	 * </p>
	 *
	 * @return	 I   _
	 */
    public Pnt2D endPoint() {
        if (isPeriodic()) {
            return null;
        }
        return points[points.length - 1];
    }

    /**
	 *  v f           B
	 *
	 * @return	{@link ParametricCurve2D#POLYLINE_2D ParametricCurve2D.POLYLINE_2D}
	 */
    int type() {
        return POLYLINE_2D;
    }

    /**
	 * {@link #toBoundedLines(ParameterSection)
	 * toBoundedLines(ParameterSection)}
	 *                  LineSegmentAccumulator        B
	 */
    private class ToBoundedLinesAccumulator extends LineSegmentAccumulator {

        BoundedLine2D[] boundedLines;

        int index;

        boolean rvrs;

        ToBoundedLinesAccumulator(boolean reverse) {
            rvrs = reverse;
        }

        void allocate(int nsegs) {
            boundedLines = new BoundedLine2D[nsegs];
            if (rvrs == false) {
                index = 0;
            } else {
                index = nsegs - 1;
            }
        }

        void doit(Pnt2D sp, Pnt2D ep, double sParam, double eParam) {
            if (rvrs == false) {
                boundedLines[index++] = new BoundedLine2D(sp, ep, false);
            } else {
                boundedLines[index--] = new BoundedLine2D(ep, sp, false);
            }
        }

        BoundedLine2D[] extract() {
            return boundedLines;
        }
    }

    /**
	 *      |     C     w                           B
	 *
	 * @param pint	                 p     [ ^
	 * @return	 w
	 */
    public BoundedLine2D[] toBoundedLines(ParameterSection pint) {
        ToBoundedLinesAccumulator acc = new ToBoundedLinesAccumulator(pint.increase() < 0.0);
        acc.accumulate(pint);
        return acc.extract();
    }

    /**
	 *      |     C   S                     B
	 *
	 * @return	     S
	 */
    public BoundedLine2D[] toBoundedLines() {
        return this.toBoundedLines(this.parameterDomain().section());
    }

    /**
	 *            A ^             I       Z q           B
	 * <p>
	 * transformedGeometries    A
	 *      O       v f   L [     A
	 *              v f   l       n b V   e [ u         B
	 * </p>
	 * <p>
	 * this    transformedGeometries      L [                         A
	 * this    transformationOperator                      B
	 *            \ b h         this    L [ A
	 *            l       transformedGeometries            B
	 * </p>
	 * <p>
	 * this    transformedGeometries          L [                       A
	 *              s       A     L [           l       B
	 *              A I   s         B
	 * </p>
	 * <p>
	 * transformedGeometries    null      \       B
	 * transformedGeometries    null            A
	 *      this    transformationOperator                      B
	 * </p>
	 *
	 * @param reverseTransform		 t                   true A               false
	 * @param transformationOperator	     I       Z q
	 * @param transformedGeometries	       l         {         v f       n b V   e [ u
	 * @return	             v f
	 */
    protected synchronized ParametricCurve2D doTransformBy(boolean reverseTransform, CTransformationOperator2D transformationOperator, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        Pnt2D[] tPoints = Pnt2D.transform(this.points, reverseTransform, transformationOperator, transformedGeometries);
        return new Polyline2D(tPoints, this.closed);
    }

    /**
	 *            |     C                           B
	 *
	 * @return	     true
	 */
    protected boolean hasPolyline() {
        return true;
    }

    /**
	 *            |     C                                     B
	 *
	 * @return	     true
	 */
    protected boolean isComposedOfOnlyPolylines() {
        return true;
    }

    /**
	 *  o   X g   [     `         o       B
	 *
	 * @param writer    PrintWriter
	 * @param indent	 C   f   g   [
	 * @see		Geometry
	 */
    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);
        writer.println(indent_tab + getClassName());
        writer.println(indent_tab + "\tpoints");
        for (int i = 0; i < nPoints(); i++) {
            points[i].output(writer, indent + 2);
        }
        writer.println(indent_tab + "\tclosed\t" + closed);
        writer.println(indent_tab + "End");
    }
}
