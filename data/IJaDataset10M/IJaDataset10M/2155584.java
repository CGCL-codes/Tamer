package jp.go.ipa.jgcl;

import java.io.PrintWriter;
import org.magiclight.common.MLUtil;
import org.magiclight.common.VectorNS;

/**
 * 2D Bspline curve
 * <p>
 * A B-spline curve is a piecewise parametric polynomial or rational curve described in terms of
 * control points and basis functions. The B-spline curve has been selected as the most stable
 * format to represent all types of polynomial or rational parametric curves. With appropriate
 * attribute values it is capable of representing single span or spline curves of explicit
 * polynomial, rational, Bezier or B-spline type.
 * <p>
 * Interpretation of the data is as follows:
 * <p>
 * <pre>
 *  1.
 *
 *    All weights shall be positive and the curve is given by
 *    k+1 	= number of control points
 *    Pi 	= control points
 *    wi 	= weights
 *    d 	= degree
 * </pre>
 * <p>
 * The knot array is an array of (k+d+2) real numbers [u-d ... uk+1], such that for all indices j
 * in [-d,k], uj <= uj+1. This array is obtained from the knot data list by repeating each multiple
 * knot according to the multiplicity. N di, the ith normalized B-spline basis function of degree d,
 * is defined on the subset [ui-d, ... , ui+1] of this array.
 * <p>
 * Let L denote the number of distinct values among the d+k+2 knots in the knot array; L will be
 * referred to as the 'upper index on knots'. Let mj denote the multiplicity (i.e. number of repetitions)
 * of the jth distinct knot. Then all knot multiplicities except the first and the last shall be in the
 * range 1 ... degree; the first and last may have a maximum value of degree + 1. In evaluating the
 * basis functions, a knot u of e.g. multiplicity 3 is interpreted as a string u, u, u, in the knot array.
 * The B-spline curve has 3 special subtypes where the knots and knot multiplicities are derived to provide
 * simple default capabilities.
 *
 * @author Information-technology Promotion Agency, Japan
 */
public class BsplineCurve2D extends FreeformCurveWithControlPoints2D {

    private static final long serialVersionUID = 1L;

    /**
	 * Knot data
	 */
    private BsplineKnotVector knotData;

    /**
	 * Type of curve
	 */
    private BsplineCurveForm curveForm = BsplineCurveForm.UNSPECIFIED;

    /**
	 * Constructor
	 *
	 * @param degree
	 * @param periodic
	 * @param knotMultiplicities
	 * @param knots
	 * @param controlPoints
	 */
    public BsplineCurve2D(int degree, boolean periodic, int[] knotMultiplicities, double[] knots, Pnt2D[] controlPoints) {
        super(controlPoints);
        knotData = new BsplineKnotVector(degree, KnotType.UNSPECIFIED, periodic, knotMultiplicities, knots, nControlPoints());
    }

    /**
	 * Constructor
	 *
	 *
	 * @param degree
	 * @param knotMultiplicities
	 * @param knots
	 * @param controlPoints
	 */
    public BsplineCurve2D(int degree, int[] knotMultiplicities, double[] knots, Pnt2D[] controlPoints) {
        super(controlPoints);
        knotData = new BsplineKnotVector(degree, KnotType.UNSPECIFIED, false, knotMultiplicities, knots, nControlPoints());
    }

    /**
	 * Constructor
	 *
	 * @param degree
	 * @param periodic
	 * @param knots
	 * @param knotMultiplicities 
	 * @param weights
	 * @param controlPoints
	 */
    public BsplineCurve2D(int degree, boolean periodic, int[] knotMultiplicities, double[] knots, Pnt2D[] controlPoints, double[] weights) {
        super(controlPoints, weights);
        knotData = new BsplineKnotVector(degree, KnotType.UNSPECIFIED, periodic, knotMultiplicities, knots, nControlPoints());
    }

    /**
	 * Constructor
	 *
	 * @param degree
	 * @param knotMultiplicities
	 * @param knots
	 * @param weights
	 * @param controlPoints
	 */
    public BsplineCurve2D(int degree, int[] knotMultiplicities, double[] knots, Pnt2D[] controlPoints, double[] weights) {
        super(controlPoints, weights);
        knotData = new BsplineKnotVector(degree, KnotType.UNSPECIFIED, false, knotMultiplicities, knots, nControlPoints());
    }

    /**
	 * Constructor
	 *
	 * @param degree
	 * @param knotSpec
	 * @param periodic
	 * @param controlPoints
	 */
    public BsplineCurve2D(int degree, boolean periodic, KnotType knotSpec, Pnt2D[] controlPoints) {
        super(controlPoints);
        knotData = new BsplineKnotVector(degree, knotSpec, periodic, null, null, nControlPoints());
    }

    /**
	 * Constructor
	 *
	 * @param degree
	 * @param knotSpec
	 * @param controlPoints
	 */
    public BsplineCurve2D(int degree, KnotType knotSpec, Pnt2D[] controlPoints) {
        super(controlPoints);
        knotData = new BsplineKnotVector(degree, knotSpec, false, null, null, nControlPoints());
    }

    /**
	 * Constructor
	 *
	 * @param degree 
	 * @param knotSpec
	 * @param periodic
	 * @param controlPoints
	 * @param weights
	 */
    public BsplineCurve2D(int degree, boolean periodic, KnotType knotSpec, Pnt2D[] controlPoints, double[] weights) {
        super(controlPoints, weights);
        knotData = new BsplineKnotVector(degree, knotSpec, periodic, null, null, nControlPoints());
    }

    /**
	 * Constructor
	 *
	 * @param degree 
	 * @param knotSpec
	 * @param controlPoints
	 * @param weights
	 */
    public BsplineCurve2D(int degree, KnotType knotSpec, Pnt2D[] controlPoints, double[] weights) {
        super(controlPoints, weights);
        knotData = new BsplineKnotVector(degree, knotSpec, false, null, null, nControlPoints());
    }

    /**
	 * Constructor
	 */
    BsplineCurve2D(BsplineKnotVector knotData, double[][] cpArray) {
        super(cpArray);
        if (knotData.nControlPoints() != nControlPoints()) {
            throw new ExceptionGeometryInvalidArgumentValue();
        }
        this.knotData = knotData;
    }

    /**
	 * Constructor
	 */
    BsplineCurve2D(BsplineKnotVector knotData, Pnt2D[] controlPoints, double[] weights) {
        super(controlPoints, weights, false);
        this.knotData = knotData;
    }

    /**
	 * Constructor
	 *
	 * @param points
	 * @param params
	 */
    public BsplineCurve2D(Pnt2D[] points, double[] params) {
        super();
        Interpolation2D doObj = new Interpolation2D(points, params);
        this.controlPoints = doObj.controlPoints();
        this.knotData = doObj.knotData();
        this.weights = doObj.weights();
    }

    /**
	 * Constructor
	 *
	 * @param points
	 * @param params
	 * @param endvecs
	 */
    public BsplineCurve2D(Pnt2D[] points, double[] params, Vec2D[] endvecs) {
        super();
        Interpolation2D doObj = new Interpolation2D(points, params, endvecs);
        this.controlPoints = doObj.controlPoints();
        this.knotData = doObj.knotData();
        this.weights = doObj.weights();
    }

    /**
	 * Constructor
	 *
	 * @param points 
	 * @param params
	 * @param isClosed
	 * @param endvecs
	 */
    public BsplineCurve2D(Pnt2D[] points, double[] params, Vec2D[] endvecs, boolean isClosed) {
        super();
        Interpolation2D doObj = new Interpolation2D(points, params, endvecs, isClosed);
        this.controlPoints = doObj.controlPoints();
        this.knotData = doObj.knotData();
        this.weights = doObj.weights();
    }

    /**
	 * Constructor
	 *
	 * @param points
	 * @param midTol
	 * @param endDir
	 * @param params
	 * @param isClosed
	 * @param tol
	 */
    public BsplineCurve2D(Pnt2D[] points, double[] params, Vec2D[] endDir, boolean isClosed, ToleranceForDistance tol, ToleranceForDistance midTol) {
        super();
        Approximation2D doObj = new Approximation2D(points, params, endDir, isClosed);
        BsplineCurve2D bsc = doObj.getApproximationWithTolerance(tol, midTol);
        this.controlPoints = bsc.controlPoints;
        this.knotData = bsc.knotData;
        this.weights = bsc.weights;
    }

    /**
	 * Returns degree
	 *
	 * @return
	 */
    public int degree() {
        return knotData.degree();
    }

    /**
	 * Returns knot specification
	 *
	 * @return
	 */
    public KnotType knotSpec() {
        return knotData.knotSpec();
    }

    /**
	 * Return knot values
	 *
	 * @return
	 */
    public int nKnotValues() {
        return knotData.nKnotValues();
    }

    /**
	 * Return knot at n
	 *
	 * @param n
	 * @return
	 */
    public double knotValueAt(int n) {
        return knotData.knotValueAt(n);
    }

    /**
	 * Return number of segments
	 */
    int nSegments() {
        return knotData.nSegments();
    }

    /**
	 * Return number of valid segments
	 */
    BsplineKnotVector.ValidSegmentInfo validSegments() {
        return knotData.validSegments();
    }

    /**
	 * Return knot data
	 */
    BsplineKnotVector knotData() {
        return knotData;
    }

    @Override
    public boolean isPeriodic() {
        return knotData.isPeriodic();
    }

    @Override
    public Pnt2D controlPointAt(int n) {
        if (isPeriodic()) {
            int ncp = nControlPoints();
            while (n < 0) {
                n += ncp;
            }
            while (n >= ncp) {
                n -= ncp;
            }
        }
        return controlPoints[n];
    }

    private void make_coef(double[] coef, int jjj, int seg, double pTol) {
        int degree = coef.length - 3;
        if (degree == 0) {
            coef[1] = (jjj == seg) ? 1.0 : 0.0;
        } else {
            double coef_1[] = new double[degree + 2];
            double aaa;
            double kj;
            int ijk;
            for (ijk = 0; ijk <= degree; ijk++) {
                coef[ijk + 1] = 0.0;
            }
            coef_1[0] = coef_1[degree + 1] = 0.0;
            if (jjj != (seg - degree)) {
                kj = knotValueAt(jjj);
                aaa = knotValueAt(jjj + degree) - kj;
                if (aaa > pTol) {
                    make_coef(coef_1, jjj, seg, pTol);
                    for (ijk = 0; ijk <= degree; ijk++) {
                        coef[ijk + 1] += (coef_1[ijk + 1] - coef_1[ijk] * kj) / aaa;
                    }
                }
            }
            if (jjj != seg) {
                kj = knotValueAt(jjj + degree + 1);
                aaa = kj - knotValueAt(jjj + 1);
                if (aaa > pTol) {
                    make_coef(coef_1, jjj + 1, seg, pTol);
                    for (ijk = 0; ijk <= degree; ijk++) {
                        coef[ijk + 1] -= (coef_1[ijk + 1] - coef_1[ijk] * kj) / aaa;
                    }
                }
            }
        }
    }

    /**
	 *
	 * @param iSseg
	 * @param isPoly
	 * @return
	 */
    public RealPolynomial[] polynomial(int iSseg, boolean isPoly) {
        int degree = degree();
        int isckt = iSseg;
        int ijk, klm, mno, pklm, i;
        double[][] cntlPnts = toDoubleArray(isPoly);
        int uicp = nControlPoints();
        int dim = cntlPnts[0].length;
        double[][] coef = new double[dim][];
        for (i = 0; i < dim; i++) {
            coef[i] = new double[degree + 1];
        }
        double[] eA = new double[degree + 3];
        double pTol = getToleranceForParameter();
        RealPolynomial[] polynomial = new RealPolynomial[dim];
        for (i = 0; i < dim; i++) {
            for (ijk = 0; ijk <= degree; ijk++) {
                coef[i][ijk] = 0.0;
            }
        }
        for (ijk = 0, pklm = klm = isckt; ijk <= degree; ijk++, pklm++, klm++) {
            make_coef(eA, klm, (isckt + degree), pTol);
            if ((isPeriodic()) && (pklm == uicp)) {
                pklm = 0;
            }
            for (i = 0; i < dim; i++) {
                for (mno = 0; mno <= degree; mno++) {
                    coef[i][degree - mno] += eA[mno + 1] * cntlPnts[pklm][i];
                }
            }
        }
        for (i = 0; i < dim; i++) {
            polynomial[i] = new RealPolynomial(coef[i]);
        }
        return polynomial;
    }

    @Override
    public double length(ParameterSection pint) {
        double length = 0.0;
        RealFunctionWithOneVariable realFunction;
        double dTol = getToleranceForDistance() / 2.0;
        double pTol = getToleranceForParameter();
        if (!isPolynomial() || Math.abs(pint.increase()) <= pTol) {
            realFunction = new RealFunctionWithOneVariable() {

                @Override
                public double evaluate(double parameter) {
                    return tangentVector(parameter).length();
                }
            };
            length = GeometryMath.getDefiniteIntegral(realFunction, pint, dTol);
        } else {
            BsplineCurve2D tbsc = truncate(pint);
            BsplineKnotVector.ValidSegmentInfo vsegInfo = tbsc.knotData.validSegments();
            int nvseg = vsegInfo.nSegments();
            for (int ijk = 0; ijk < nvseg; ijk++) {
                RealPolynomial[] poly = tbsc.polynomial(ijk, isPolynomial());
                final RealPolynomial[] deriv = new RealPolynomial[poly.length];
                for (int klm = 0; klm < poly.length; klm++) {
                    deriv[klm] = poly[klm].derive();
                }
                realFunction = new RealFunctionWithOneVariable() {

                    @Override
                    public double evaluate(double parameter) {
                        final double[] tang = new double[2];
                        for (int klm = 0; klm < 2; klm++) {
                            tang[klm] = deriv[klm].evaluate(parameter);
                        }
                        return Math.sqrt(tang[0] * tang[0] + tang[1] * tang[1]);
                    }
                };
                ParameterSection psec = new ParameterSection(vsegInfo.knotPoint(ijk)[0], vsegInfo.knotPoint(ijk)[1]);
                length += GeometryMath.getDefiniteIntegral(realFunction, psec, dTol);
            }
        }
        return length;
    }

    @Override
    public Pnt2D coordinates(double param) {
        double[][] cntlPnts;
        double[] d0D;
        boolean isPoly = isPolynomial();
        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        d0D = BsplineCurveEvaluation.coordinates(knotData, cntlPnts, param);
        if (!isPoly) {
            convRational0Deriv(d0D);
        }
        return new CPnt2D(d0D[0], d0D[1]);
    }

    @Override
    public Vec2D tangentVector(double param) {
        double[][] cntlPnts;
        double[] d1D = new double[3];
        boolean isPoly = isPolynomial();
        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        if (isPoly) {
            BsplineCurveEvaluation.evaluation(knotData, cntlPnts, param, null, d1D, null, null);
        } else {
            double[] d0D = new double[3];
            BsplineCurveEvaluation.evaluation(knotData, cntlPnts, param, d0D, d1D, null, null);
            convRational1Deriv(d0D, d1D);
        }
        return new LVec2D(d1D[0], d1D[1]);
    }

    @Override
    public CurveCurvature2D curvature(double param) {
        int degree;
        CurveDerivative2D deriv;
        boolean tang0;
        double tang_leng;
        double dDcrv;
        Vec2D dDnrm;
        CurveCurvature2D crv;
        ConditionOfOperation condition = ConditionOfOperation.getCondition();
        double tol_d = condition.getToleranceForDistance();
        degree = degree();
        deriv = evaluation(param);
        tang_leng = deriv.d1D().lengthSquared();
        if (tang_leng < (tol_d * tol_d)) {
            tang0 = true;
        } else {
            tang0 = false;
        }
        if ((degree < 2) || (tang0 == true)) {
            dDcrv = 0.0;
            dDnrm = Vec2D.zeroVector;
        } else {
            double ewvec;
            tang_leng = Math.sqrt(tang_leng);
            dDcrv = tang_leng * tang_leng * tang_leng;
            ewvec = deriv.d1D().crossZ(deriv.d2D());
            dDcrv = Math.abs(ewvec) / dDcrv;
            if (ewvec < 0.0) {
                dDnrm = new LVec2D(deriv.d1D().y(), (-deriv.d1D().x()));
            } else {
                dDnrm = new LVec2D((-deriv.d1D().y()), deriv.d1D().x());
            }
            dDnrm = dDnrm.normalized();
        }
        return new CurveCurvature2D(dDcrv, dDnrm);
    }

    @Override
    public CurveDerivative2D evaluation(double param) {
        double[][] cntlPnts;
        double[] ld0D = new double[3];
        double[] ld1D = new double[3];
        double[] ld2D = new double[3];
        Pnt2D d0D;
        Vec2D d1D;
        Vec2D d2D;
        boolean isPoly = isPolynomial();
        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        BsplineCurveEvaluation.evaluation(knotData, cntlPnts, param, ld0D, ld1D, ld2D, null);
        if (!isPoly) {
            convRational2Deriv(ld0D, ld1D, ld2D);
        }
        d0D = new CPnt2D(ld0D[0], ld0D[1]);
        d1D = new LVec2D(ld1D[0], ld1D[1]);
        d2D = new LVec2D(ld2D[0], ld2D[1]);
        return new CurveDerivative2D(d0D, d1D, d2D);
    }

    /**
	 *
	 * @param segNumber
	 * @param parameters
	 * @return
	 */
    public Pnt2D blossoming(int segNumber, double[] parameters) {
        double[] adjustedParameters = new double[this.degree()];
        for (int i = 0; i < this.degree(); i++) {
            adjustedParameters[i] = this.checkParameter(parameters[i]);
        }
        boolean isPoly = this.isPolynomial();
        double[] d0D = BsplineCurveEvaluation.blossoming(knotData, toDoubleArray(isPoly), segNumber, adjustedParameters);
        if (isPoly != true) {
            this.convRational0Deriv(d0D);
        }
        if (isPoly == true) {
            return new CPnt2D(d0D[0], d0D[1]);
        } else {
            return new HPnt2D(d0D[0], d0D[1], d0D[2]);
        }
    }

    private void checkEndPoint(double minParam, double maxParam, int minDegree, int seg, int nvseg, VectorNS<Double> paramVec) {
        int mno;
        if (degree() < minDegree) {
            if (isClosed() && (paramVec.size() == 0)) {
                paramVec.addElement(new Double(minParam));
            }
            if (seg != (nvseg - 1)) {
                for (mno = 0; mno < paramVec.size(); mno++) {
                    if (identicalParameter((paramVec.elementAt(mno)).doubleValue(), maxParam)) {
                        break;
                    }
                }
                if (mno == paramVec.size()) {
                    paramVec.addElement(new Double(maxParam));
                }
            }
        }
    }

    @Override
    public PointOnCurve2D[] singular() {
        BsplineKnotVector.ValidSegmentInfo vsegInfo = knotData.validSegments();
        int nvseg = vsegInfo.nSegments();
        int numseg = nSegments();
        PureBezierCurve2D[] bzcs = toPureBezierCurveArray();
        int minDegree = 2;
        int ijk, klm, mno;
        VectorNS<Double> snglrParam = new VectorNS<Double>();
        for (ijk = 0; ijk < numseg; ijk++) {
            double minParam;
            double maxParam;
            if ((klm = vsegInfo.isValidSegment(ijk)) < 0) {
                minParam = knotValueAt(degree() + ijk);
                maxParam = knotValueAt(degree() + ijk + 1);
                checkEndPoint(minParam, maxParam, minDegree, ijk, nvseg, snglrParam);
                continue;
            }
            minParam = vsegInfo.knotPoint(klm)[0];
            maxParam = vsegInfo.knotPoint(klm)[1];
            checkEndPoint(minParam, maxParam, minDegree, klm, nvseg, snglrParam);
            PointOnCurve2D[] singularOnBzc;
            try {
                singularOnBzc = bzcs[klm].singular();
            } catch (ExceptionGeometryIndefiniteSolution e) {
                continue;
            }
            for (klm = 0; klm < singularOnBzc.length; klm++) {
                double candidateParam = (maxParam - minParam) * singularOnBzc[klm].parameter() + minParam;
                for (mno = 0; mno < snglrParam.size(); mno++) {
                    if (identicalParameter((snglrParam.elementAt(mno)).doubleValue(), candidateParam)) {
                        break;
                    }
                }
                if (mno == snglrParam.size()) {
                    snglrParam.addElement(new Double(candidateParam));
                }
            }
        }
        int numberOfSolution = snglrParam.size();
        PointOnCurve2D[] singular = new PointOnCurve2D[numberOfSolution];
        for (ijk = 0; ijk < numberOfSolution; ijk++) {
            singular[ijk] = new PointOnCurve2D(this, (snglrParam.elementAt(ijk)).doubleValue(), false);
        }
        return singular;
    }

    @Override
    public PointOnCurve2D[] inflexion() {
        BsplineKnotVector.ValidSegmentInfo vsegInfo = knotData.validSegments();
        int nvseg = vsegInfo.nSegments();
        int numseg = nSegments();
        PureBezierCurve2D[] bzcs = toPureBezierCurveArray();
        int minDegree = 3;
        int ijk, klm, mno;
        VectorNS<Double> inflxParam = new VectorNS<Double>();
        for (ijk = 0; ijk < numseg; ijk++) {
            double minParam;
            double maxParam;
            if ((klm = vsegInfo.isValidSegment(ijk)) < 0) {
                minParam = knotValueAt(degree() + ijk);
                maxParam = knotValueAt(degree() + ijk + 1);
                checkEndPoint(minParam, maxParam, minDegree, ijk, nvseg, inflxParam);
                continue;
            }
            minParam = vsegInfo.knotPoint(klm)[0];
            maxParam = vsegInfo.knotPoint(klm)[1];
            checkEndPoint(minParam, maxParam, minDegree, klm, nvseg, inflxParam);
            PointOnCurve2D[] inflexionOnBzc;
            try {
                inflexionOnBzc = bzcs[klm].inflexion();
            } catch (ExceptionGeometryIndefiniteSolution e) {
                continue;
            }
            for (klm = 0; klm < inflexionOnBzc.length; klm++) {
                double candidateParam = (maxParam - minParam) * inflexionOnBzc[klm].parameter() + minParam;
                for (mno = 0; mno < inflxParam.size(); mno++) {
                    if (identicalParameter((inflxParam.elementAt(mno)).doubleValue(), candidateParam)) {
                        break;
                    }
                }
                if (mno == inflxParam.size()) {
                    inflxParam.addElement(new Double(candidateParam));
                }
            }
        }
        int numberOfSolution = inflxParam.size();
        PointOnCurve2D[] inflexion = new PointOnCurve2D[numberOfSolution];
        for (ijk = 0; ijk < numberOfSolution; ijk++) {
            inflexion[ijk] = new PointOnCurve2D(this, (inflxParam.elementAt(ijk)).doubleValue(), false);
        }
        return inflexion;
    }

    @Override
    public PointOnCurve2D[] projectFrom(Pnt2D mate) {
        int dimension = 2;
        int ijk, klm, mno, i;
        int coef_size = isPolynomial() ? dimension : dimension + 1;
        RealPolynomial work0, work1, work2, sub;
        double[] work3;
        RealPolynomial[] pointPoly;
        RealPolynomial[] offsPoly = new RealPolynomial[dimension];
        RealPolynomial[] tangPoly = new RealPolynomial[coef_size];
        RealPolynomial[] dotePoly = new RealPolynomial[dimension];
        BsplineKnotVector.ValidSegmentInfo vsegInfo = knotData.validSegments();
        PointOnCurve2D proj;
        PointOnGeometryList projList = new PointOnGeometryList();
        double dTol = getToleranceForDistance();
        ComplexPolynomial dtPoly;
        Complex[] root;
        double[] intv;
        ParameterDomain domain;
        double par;
        for (ijk = 0; ijk < vsegInfo.nSegments(); ijk++) {
            pointPoly = polynomial(vsegInfo.segmentNumber(ijk), isPolynomial());
            if (isRational()) {
                offsPoly[0] = pointPoly[dimension].multiply(mate.x());
                offsPoly[1] = pointPoly[dimension].multiply(mate.y());
            } else {
                double coef[][] = { { mate.x() }, { mate.y() } };
                offsPoly[0] = new RealPolynomial(coef[0]);
                offsPoly[1] = new RealPolynomial(coef[1]);
            }
            for (i = 0; i < dimension; i++) {
                pointPoly[i] = pointPoly[i].subtract(offsPoly[i]);
            }
            for (klm = 0; klm < coef_size; klm++) {
                tangPoly[klm] = pointPoly[klm].derive();
            }
            if (!isRational()) {
                for (klm = 0; klm < dimension; klm++) {
                    dotePoly[klm] = pointPoly[klm].multiply(tangPoly[klm]);
                }
            } else {
                for (klm = 0; klm < 2; klm++) {
                    work0 = pointPoly[2].multiply(tangPoly[klm]);
                    work1 = tangPoly[2].multiply(pointPoly[klm]);
                    work2 = work0.subtract(work1);
                    work3 = work2.coefficientsBetween(0, (work2.degree() - 1));
                    sub = new RealPolynomial(work3);
                    dotePoly[klm] = pointPoly[klm].multiply(sub);
                }
            }
            try {
                dtPoly = dotePoly[0].add(dotePoly[1]).toComplexPolynomial();
            } catch (ExceptionGeometryInvalidArgumentValue e) {
                throw new ExceptionGeometryFatal();
            }
            try {
                root = dtPoly.getRootsByDKA();
            } catch (ComplexPolynomial.DKANotConverge e) {
                root = e.getValues();
            } catch (ComplexPolynomial.ImpossibleEquation e) {
                throw new ExceptionGeometryFatal();
            } catch (ComplexPolynomial.IndefiniteEquation e) {
                throw new ExceptionGeometryFatal();
            }
            intv = vsegInfo.knotPoint(ijk);
            domain = new ParameterDomain(false, intv[0], intv[1] - intv[0]);
            for (mno = 0; mno < root.length; mno++) {
                par = root[mno].real();
                if (!domain.isValid(par)) {
                    continue;
                }
                par = domain.force(par);
                proj = checkProjection(par, mate, dTol * dTol);
                if (proj != null) {
                    projList.addPoint(proj);
                }
            }
        }
        return projList.toPointOnCurve2DArray();
    }

    @Override
    public BsplineCurve2D toBsplineCurve() {
        if (this.isRational() == true) {
            return this;
        }
        return new BsplineCurve2D(this.knotData, this.controlPoints, this.makeUniformWeights());
    }

    @Override
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
        BsplineCurve2D target;
        if (this.isPeriodic() == true) {
            if (pint.absIncrease() >= this.parameterDomain().section().absIncrease()) {
                target = this;
                try {
                    target = target.shiftIfPeriodic(pint.start());
                } catch (ExceptionGeometryOpenCurve e) {
                }
                if (pint.increase() < 0.0) {
                    target = target.reverse();
                }
            } else {
                target = this.truncate(pint);
            }
        } else {
            target = this.truncate(pint);
        }
        return target.toBsplineCurve();
    }

    @Override
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate) {
        return mate.intersect(this, true);
    }

    @Override
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange) {
        BsplineKnotVector.ValidSegmentInfo vsegInfo = knotData.validSegments();
        Axis2Placement2D placement = new Axis2Placement2D(mate.pnt(), mate.dir());
        CTransformationOperator2D transform = new CTransformationOperator2D(placement, 1.0);
        int uicp = nControlPoints();
        Pnt2D[] newCp = new Pnt2D[uicp];
        for (int i = 0; i < uicp; i++) {
            newCp[i] = transform.toLocal(controlPointAt(i));
        }
        double[] weights = weights();
        if (isRational()) {
            double max_weight = 0.0;
            for (int i = 0; i < uicp; i++) {
                if (Math.abs(weights[i]) > max_weight) {
                    max_weight = weights[i];
                }
            }
            if (max_weight > 0.0) {
                for (int i = 0; i < uicp; i++) {
                    weights[i] /= max_weight;
                }
            }
        }
        BsplineCurve2D bsc = new BsplineCurve2D(knotData, newCp, weights);
        VectorNS<Double> lineParam = new VectorNS<Double>();
        VectorNS<Double> bscParam = new VectorNS<Double>();
        int nSeg = vsegInfo.nSegments();
        int k = 0;
        for (int i = 0; i < nSeg; i++) {
            RealPolynomial[] realPoly = bsc.polynomial(vsegInfo.segmentNumber(i), isPolynomial());
            ComplexPolynomial compPoly = realPoly[1].toComplexPolynomial();
            Complex[] roots;
            try {
                roots = compPoly.getRootsByDKA();
            } catch (ComplexPolynomial.DKANotConverge e) {
                roots = e.getValues();
            } catch (ComplexPolynomial.ImpossibleEquation e) {
                throw new ExceptionGeometryFatal();
            } catch (ComplexPolynomial.IndefiniteEquation e) {
                throw new ExceptionGeometryFatal();
            }
            int nRoots = roots.length;
            for (int j = 0; j < nRoots; j++) {
                double realRoot = roots[j].real();
                if (bsc.parameterValidity(realRoot) == ParameterValidityType.OUTSIDE) {
                    continue;
                }
                double[] knotParams = vsegInfo.knotPoint(i);
                if (realRoot < knotParams[0]) {
                    realRoot = knotParams[0];
                }
                if (realRoot > knotParams[1]) {
                    realRoot = knotParams[1];
                }
                Pnt2D workPoint = bsc.coordinates(realRoot);
                double dTol = bsc.getToleranceForDistance();
                int jj;
                if (Math.abs(workPoint.y()) < dTol) {
                    for (jj = 0; jj < k; jj++) {
                        double paramA = (lineParam.elementAt(jj)).doubleValue();
                        double paramB = (bscParam.elementAt(jj)).doubleValue();
                        if ((Math.abs(paramA - workPoint.x()) < dTol) && (bsc.identicalParameter(paramA, paramB))) {
                            break;
                        }
                    }
                    if (jj >= k) {
                        lineParam.addElement(new Double(workPoint.x()));
                        bscParam.addElement(new Double(realRoot));
                        k++;
                    }
                }
            }
        }
        int num = bscParam.size();
        IntersectionPoint2D[] intersectPoint = new IntersectionPoint2D[num];
        double mateLength = mate.dir().length();
        for (int i = 0; i < k; i++) {
            double work = (lineParam.elementAt(i)).doubleValue() / mateLength;
            PointOnCurve2D pointOnLine = new PointOnCurve2D(mate, work, MLUtil.DEBUG);
            work = (bscParam.elementAt(i)).doubleValue();
            PointOnCurve2D pointOnBsc = new PointOnCurve2D(this, work, MLUtil.DEBUG);
            Pnt2D coordinates = coordinates(work);
            if (!doExchange) {
                intersectPoint[i] = new IntersectionPoint2D(coordinates, pointOnBsc, pointOnLine, MLUtil.DEBUG);
            } else {
                intersectPoint[i] = new IntersectionPoint2D(coordinates, pointOnLine, pointOnBsc, MLUtil.DEBUG);
            }
        }
        return intersectPoint;
    }

    @Override
    IntersectionPoint2D[] intersect(Circle2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    @Override
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    @Override
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    @Override
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    IntersectionPoint2D[] intersect(Polyline2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    @Override
    IntersectionPoint2D[] intersect(PureBezierCurve2D mate, boolean doExchange) {
        return IntsBzcBsc2D.intersection(mate, this, !doExchange);
    }

    @Override
    IntersectionPoint2D[] intersect(BsplineCurve2D mate, boolean doExchange) {
        return IntsBscBsc2D.intersection(this, mate, doExchange);
    }

    @Override
    IntersectionPoint2D[] intersect(TrimmedCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    @Override
    IntersectionPoint2D[] intersect(CompositeCurveSegment2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    @Override
    IntersectionPoint2D[] intersect(CompositeCurve2D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    @Override
    public CurveCurveInterference2D[] interfere(BoundedCurve2D mate) {
        return mate.interfere(this, true);
    }

    @Override
    CurveCurveInterference2D[] interfere(BoundedLine2D mate, boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    @Override
    CurveCurveInterference2D[] interfere(Polyline2D mate, boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    @Override
    CurveCurveInterference2D[] interfere(PureBezierCurve2D mate, boolean doExchange) {
        return IntsBzcBsc2D.interference(mate, this, !doExchange);
    }

    @Override
    CurveCurveInterference2D[] interfere(BsplineCurve2D mate, boolean doExchange) {
        return IntsBscBsc2D.interference(this, mate, doExchange);
    }

    @Override
    CurveCurveInterference2D[] interfere(TrimmedCurve2D mate, boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    @Override
    CurveCurveInterference2D[] interfere(CompositeCurveSegment2D mate, boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    @Override
    CurveCurveInterference2D[] interfere(CompositeCurve2D mate, boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    @Override
    public BsplineCurve2D offsetByBsplineCurve(ParameterSection pint, double magni, int side, ToleranceForDistance tol) {
        Ofst2D doObj = new Ofst2D(this, pint, magni, side, tol);
        return doObj.offset();
    }

    @Override
    public CommonTangent2D[] commonTangent(ParametricCurve2D mate) {
        throw new ExceptionGeometryNotSupported();
    }

    @Override
    public CommonNormal2D[] commonNormal(ParametricCurve2D mate) {
        throw new ExceptionGeometryNotSupported();
    }

    /**
	 * Insert knot at param
	 * @param param
	 * @return
	 */
    public BsplineCurve2D insertKnot(double param) {
        double[][] cntlPnts;
        boolean isPoly = isPolynomial();
        Object[] result;
        BsplineKnotVector newKnotData;
        double[][] newControlPoints;
        BsplineCurve2D bsc;
        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        result = BsplineCurveEvaluation.insertKnot(knotData, cntlPnts, param);
        newKnotData = (BsplineKnotVector) result[0];
        newControlPoints = (double[][]) result[1];
        return new BsplineCurve2D(newKnotData, newControlPoints);
    }

    /**
	 *
	 * @param param
	 * @return
	 */
    public BsplineCurve2D[] divide(double param) {
        double[][] cntlPnts;
        boolean isPoly = isPolynomial();
        BsplineKnotVector[] newKnotData = new BsplineKnotVector[2];
        double[][][] newControlPoints = new double[2][][];
        int n_bsc;
        BsplineCurve2D[] bsc;
        param = checkParameter(param);
        cntlPnts = toDoubleArray(isPoly);
        BsplineCurveEvaluation.divide(knotData, cntlPnts, param, newKnotData, newControlPoints);
        if (newKnotData[0] == null) {
            throw new ExceptionGeometryFatal();
        } else {
            if (newKnotData[1] == null) {
                n_bsc = 1;
            } else {
                n_bsc = 2;
            }
        }
        bsc = new BsplineCurve2D[n_bsc];
        for (int i = 0; i < n_bsc; i++) {
            try {
                bsc[i] = new BsplineCurve2D(newKnotData[i], newControlPoints[i]);
            } catch (ExceptionGeometryInvalidArgumentValue e) {
                throw new ExceptionGeometryFatal();
            }
        }
        return bsc;
    }

    /**
	 *
	 * @param section
	 * @return
	 */
    public BsplineCurve2D truncate(ParameterSection section) {
        if (Math.abs(section.increase()) <= getToleranceForParameter()) {
            throw new ExceptionGeometryInvalidArgumentValue();
        }
        double start_par, end_par;
        BsplineCurve2D t_bsc;
        if (isNonPeriodic()) {
            start_par = checkParameter(section.lower());
            end_par = checkParameter(section.upper());
            t_bsc = divide(start_par)[1];
            t_bsc = t_bsc.divide(end_par - start_par)[0];
        } else {
            double crv_intvl = parameterDomain().section().increase();
            double tol_p = ConditionOfOperation.getCondition().getToleranceForParameter();
            start_par = checkParameter(section.start());
            t_bsc = divide(start_par)[0];
            if (Math.abs(section.increase()) < crv_intvl - tol_p) {
                if (section.increase() > 0.0) {
                    end_par = section.increase();
                    t_bsc = t_bsc.divide(end_par)[0];
                } else {
                    end_par = crv_intvl + section.increase();
                    t_bsc = t_bsc.divide(end_par)[1];
                }
            }
        }
        if (section.increase() < 0.0) {
            t_bsc = t_bsc.reverse();
        }
        return t_bsc;
    }

    /**
	 *
	 * @param newStartParam
	 * @return
	 * @throws ExceptionGeometryOpenCurve
	 */
    public BsplineCurve2D shiftIfPeriodic(double newStartParam) throws ExceptionGeometryOpenCurve {
        if (this.isPeriodic() != true) {
            throw new ExceptionGeometryOpenCurve();
        }
        newStartParam = this.parameterDomain().wrap(newStartParam);
        int newFirstSegment = this.knotData().getSegmentNumberThatStartIsEqualTo(newStartParam);
        if (newFirstSegment == (-1)) {
            return this.insertKnot(newStartParam).shiftIfPeriodic(newStartParam);
        }
        BsplineKnotVector newKnotData = this.knotData().shift(newFirstSegment);
        int nNewControlPoints = newKnotData.nControlPoints();
        Pnt2D[] newControlPoints = new Pnt2D[nNewControlPoints];
        for (int i = 0; i < nNewControlPoints; i++) {
            newControlPoints[i] = this.controlPointAt((i + newFirstSegment) % nNewControlPoints);
        }
        double[] newWeights = null;
        if (this.isRational() == true) {
            newWeights = new double[nNewControlPoints];
            for (int i = 0; i < nNewControlPoints; i++) {
                newWeights[i] = this.weightAt((i + newFirstSegment) % nNewControlPoints);
            }
        }
        return new BsplineCurve2D(newKnotData, newControlPoints, newWeights);
    }

    @Override
    public Polyline2D toPolyline(ParameterSection section, ToleranceForDistance tolerance) {
        BsplineKnotVector.ValidSegmentInfo vseg_info;
        int nseg;
        double lower_limit, upper_limit;
        double my_sp, my_ep;
        int my_sseg, my_eseg;
        PureBezierCurve2D[] bzcs;
        int no_total_pnts;
        SegmentInfo[] seg_infos;
        double[] kp;
        double bzc_sp, bzc_ep, bzc_ip;
        ParameterSection lpint;
        Polyline2D lpol;
        Pnt2D[] pnts;
        PointOnCurve2D lpnt;
        Pnt2D pnt;
        double param;
        int npnts;
        double tol_p = ConditionOfOperation.getCondition().getToleranceForParameter();
        int i, j, m;
        vseg_info = knotData.validSegments();
        nseg = vseg_info.nSegments();
        lower_limit = vseg_info.knotPoint(0)[0];
        upper_limit = vseg_info.knotPoint(nseg - 1)[1];
        if (isPeriodic()) {
            BsplineCurve2D o_bsc;
            double par_intvl = upper_limit - lower_limit;
            my_sp = checkParameter(section.start());
            o_bsc = divide(my_sp)[0];
            if (section.increase() > 0.0) {
                my_sp = lower_limit;
            } else {
                my_sp = upper_limit;
            }
            lpint = new ParameterSection(my_sp, section.increase());
            lpol = o_bsc.toPolyline(lpint, tolerance);
            pnts = new Pnt2D[npnts = lpol.nPoints()];
            for (i = 0; i < npnts; i++) {
                lpnt = (PointOnCurve2D) lpol.pointAt(i);
                pnt = new CPnt2D(lpnt.x(), lpnt.y());
                param = lpnt.parameter() + section.start();
                if (param > upper_limit) {
                    param -= upper_limit;
                }
                pnts[i] = new PointOnCurve2D(pnt, this, param, MLUtil.DEBUG);
            }
            return new Polyline2D(pnts);
        }
        my_sp = checkParameter(section.lower());
        my_ep = checkParameter(section.upper());
        my_sseg = vseg_info.segmentIndex(my_sp);
        my_eseg = vseg_info.segmentIndex(my_ep);
        bzcs = toPureBezierCurveArray();
        no_total_pnts = 0;
        seg_infos = new SegmentInfo[nseg];
        for (i = my_sseg; i <= my_eseg; i++) {
            kp = vseg_info.knotPoint(i);
            seg_infos[i] = new SegmentInfo(kp[0], kp[1]);
            if (i == my_sseg) {
                bzc_sp = (my_sp - seg_infos[i].lp()) / seg_infos[i].dp();
            } else {
                bzc_sp = 0.0;
            }
            if (i == my_eseg) {
                bzc_ep = (my_ep - seg_infos[i].lp()) / seg_infos[i].dp();
            } else {
                bzc_ep = 1.0;
            }
            if ((bzc_ip = bzc_ep - bzc_sp) < tol_p) {
                my_eseg = i - 1;
                break;
            }
            lpint = new ParameterSection(bzc_sp, bzc_ip);
            try {
                lpol = bzcs[i].toPolyline(lpint, tolerance);
            } catch (ExceptionGeometryZeroLength e) {
                continue;
            }
            seg_infos[i].pnts(lpol.points());
            no_total_pnts += seg_infos[i].nPnts();
            if (i > my_sseg) {
                no_total_pnts--;
            }
        }
        if (no_total_pnts < 2) {
            throw new ExceptionGeometryZeroLength();
        }
        pnts = new Pnt2D[no_total_pnts];
        boolean first = true;
        for (i = my_sseg, m = 0; i <= my_eseg; i++) {
            if (first) {
                j = 0;
            } else {
                j = 1;
            }
            for (; j < seg_infos[i].nPnts(); j++, m++) {
                lpnt = (PointOnCurve2D) seg_infos[i].pnts(j);
                pnt = new CPnt2D(lpnt.x(), lpnt.y());
                param = seg_infos[i].lp() + (seg_infos[i].dp() * lpnt.parameter());
                pnts[m] = new PointOnCurve2D(pnt, this, param, MLUtil.DEBUG);
                first = false;
            }
        }
        if (section.increase() > 0.0) {
            return new Polyline2D(pnts);
        } else {
            return new Polyline2D(pnts).reverse();
        }
    }

    private class SegmentInfo {

        private double lp;

        private double up;

        private double dp;

        private Pnt2D[] pnts;

        private SegmentInfo(double lp, double up) {
            this.lp = lp;
            this.up = up;
            this.dp = up - lp;
        }

        private void pnts(Pnt2D[] pnts) {
            this.pnts = pnts;
        }

        private double lp() {
            return lp;
        }

        private double up() {
            return up;
        }

        private double dp() {
            return dp;
        }

        private int nPnts() {
            if (pnts == null) {
                return 0;
            }
            return pnts.length;
        }

        private Pnt2D pnts(int n) {
            return pnts[n];
        }
    }

    /**
	 *
	 * @return
	 */
    public PureBezierCurve2D[] toPureBezierCurveArray() {
        double[][] cntlPnts;
        boolean isPoly = isPolynomial();
        double[][][] bzc_array;
        PureBezierCurve2D[] bzcs;
        cntlPnts = toDoubleArray(isPoly);
        bzc_array = BsplineCurveEvaluation.toBezierCurve(knotData, cntlPnts);
        bzcs = new PureBezierCurve2D[bzc_array.length];
        for (int i = 0; i < bzc_array.length; i++) {
            try {
                bzcs[i] = new PureBezierCurve2D(bzc_array[i]);
            } catch (ExceptionGeometryInvalidArgumentValue e) {
                throw new ExceptionGeometryFatal();
            }
        }
        return bzcs;
    }

    /**
	 * Reverse curve
	 * @return
	 */
    BsplineCurve2D reverse() {
        BsplineKnotVector rKd;
        boolean isRat = isRational();
        int uicp = nControlPoints();
        Pnt2D[] rCp = new Pnt2D[uicp];
        double[] rWt = null;
        int i, j;
        rKd = knotData.reverse();
        if (isRat) {
            rWt = new double[uicp];
        }
        for (i = 0, j = uicp - 1; i < uicp; i++, j--) {
            rCp[i] = controlPointAt(j);
            if (isRat) {
                rWt[i] = weightAt(j);
            }
        }
        return new BsplineCurve2D(rKd, rCp, rWt);
    }

    @Override
    ParameterDomain getParameterDomain() {
        return knotData.getParameterDomain();
    }

    private double checkParameter(double param) {
        checkValidity(param);
        return parameterDomain().force(parameterDomain().wrap(param));
    }

    /**
	 *
	 * @return
	 */
    public BsplineCurve2D elevateOneDegree() {
        BsplineKnotVector oldKnotData = this.knotData();
        double[][] oldControlPoints = this.toDoubleArray(this.isPolynomial());
        BsplineKnotVector newKnotData = BsplineCurveEvaluation.getNewKnotDataAtDegreeElevation(oldKnotData);
        double[][] newControlPoints = BsplineCurveEvaluation.getNewControlPointsAtDegreeElevation(oldKnotData, newKnotData, oldControlPoints);
        return new BsplineCurve2D(newKnotData, newControlPoints);
    }

    /**
	 *
	 * @param mate
	 * @return
	 * @throws ExceptionGeometryTwoGeomertiesAreNotContinuous
	 */
    public BsplineCurve2D mergeIfContinuous(BsplineCurve2D mate) throws ExceptionGeometryTwoGeomertiesAreNotContinuous {
        BsplineCurve2D headCurve = this;
        BsplineCurve2D tailCurve = mate;
        ParameterSection headSection = headCurve.parameterDomain().section();
        ParameterSection tailSection = tailCurve.parameterDomain().section();
        double headEndParameter = headSection.end();
        double tailStartParameter = tailSection.start();
        Pnt2D headEnd = headCurve.coordinates(headEndParameter);
        Pnt2D tailStart = tailCurve.coordinates(tailStartParameter);
        if (headEnd.identical(tailStart) != true) {
            throw new ExceptionGeometryTwoGeomertiesAreNotContinuous();
        }
        boolean headPoly = headCurve.isPolynomial();
        boolean tailPoly = tailCurve.isPolynomial();
        boolean isPoly;
        if ((headPoly == true) && (tailPoly == true)) {
            isPoly = true;
        } else {
            if (headPoly == true) {
                isPoly = false;
                headCurve = headCurve.toBsplineCurve();
            } else {
                if (tailPoly == true) {
                    isPoly = false;
                    tailCurve = tailCurve.toBsplineCurve();
                } else {
                    isPoly = false;
                }
            }
        }
        int headDegree = headCurve.degree();
        int tailDegree = tailCurve.degree();
        while (headDegree < tailDegree) {
            headCurve = headCurve.elevateOneDegree();
            headDegree++;
        }
        while (headDegree > tailDegree) {
            tailCurve = tailCurve.elevateOneDegree();
            tailDegree++;
        }
        BsplineCurve2D[] dividedCurves;
        dividedCurves = headCurve.divide(headEndParameter);
        headCurve = dividedCurves[0];
        dividedCurves = tailCurve.divide(tailStartParameter);
        tailCurve = dividedCurves[1];
        BsplineKnotVector headKnotData = headCurve.knotData();
        BsplineKnotVector tailKnotData = tailCurve.knotData();
        int arrayLength;
        arrayLength = headKnotData.nKnots() + tailKnotData.nKnots() - 1;
        double[] newKnots = new double[arrayLength];
        int[] newKnotMultiplicities = new int[arrayLength];
        int nNewKnots = 0;
        for (int j = 0; j < headKnotData.nKnots(); j++) {
            newKnots[nNewKnots] = headKnotData.knotAt(j);
            newKnotMultiplicities[nNewKnots] = headKnotData.knotMultiplicityAt(j);
            nNewKnots++;
        }
        newKnotMultiplicities[nNewKnots - 1] = headDegree;
        double offset = headEndParameter - tailStartParameter;
        for (int j = 1; j < tailKnotData.nKnots(); j++) {
            newKnots[nNewKnots] = tailKnotData.knotAt(j) + offset;
            newKnotMultiplicities[nNewKnots] = tailKnotData.knotMultiplicityAt(j);
            nNewKnots++;
        }
        arrayLength = headKnotData.nControlPoints() + tailKnotData.nControlPoints() - 1;
        Pnt2D[] newControlPoints = new Pnt2D[arrayLength];
        double[] newWeights = null;
        if (isPoly != true) {
            newWeights = new double[arrayLength];
        }
        int nNewControlPoints = 0;
        for (int j = 0; j < headKnotData.nControlPoints(); j++) {
            newControlPoints[nNewControlPoints] = headCurve.controlPointAt(j);
            if (isPoly != true) {
                newWeights[nNewControlPoints] = headCurve.weightAt(j);
            }
            nNewControlPoints++;
        }
        double weightRatio = 0;
        if (isPoly != true) {
            weightRatio = newWeights[nNewControlPoints - 1] / tailCurve.weightAt(0);
        }
        for (int j = 1; j < tailKnotData.nControlPoints(); j++) {
            newControlPoints[nNewControlPoints] = tailCurve.controlPointAt(j);
            if (isPoly != true) {
                newWeights[nNewControlPoints] = tailCurve.weightAt(j) * weightRatio;
            }
            nNewControlPoints++;
        }
        BsplineCurve2D result;
        if (isPoly == true) {
            result = new BsplineCurve2D(headDegree, false, newKnotMultiplicities, newKnots, newControlPoints);
        } else {
            result = new BsplineCurve2D(headDegree, false, newKnotMultiplicities, newKnots, newControlPoints, newWeights);
        }
        return result;
    }

    @Override
    int type() {
        return BSPLINE_CURVE_2D;
    }

    @Override
    protected synchronized ParametricCurve2D doTransformBy(boolean reverseTransform, CTransformationOperator2D transformationOperator, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        Pnt2D[] tControlPoints = Pnt2D.transform(this.controlPoints, reverseTransform, transformationOperator, transformedGeometries);
        return new BsplineCurve2D(this.knotData, tControlPoints, this.weights);
    }

    @Override
    protected void output(PrintWriter writer, int indent) {
        String indent_tab = makeIndent(indent);
        StringBuilder buf = new StringBuilder();
        writer.println(indent_tab + getClassName());
        knotData.output(writer, indent, 0);
        writer.println(indent_tab + "\tcontrolPoints");
        for (int i = 0; i < nControlPoints(); i++) {
            controlPointAt(i).output(writer, indent + 2);
        }
        if (weights() != null) {
            writer.println(indent_tab + "\tweights ");
            int i = 0;
            while (true) {
                for (int j = 0; j < 10 && i < weights().length; j++, i++) {
                    writer.print(" " + weightAt(i));
                }
                writer.println();
                if (i < weights().length) {
                    writer.print(indent_tab + "\t");
                } else {
                    break;
                }
            }
        }
        writer.println(indent_tab + "\tcurveForm\t" + curveForm);
        writer.println(indent_tab + "End");
    }
}
