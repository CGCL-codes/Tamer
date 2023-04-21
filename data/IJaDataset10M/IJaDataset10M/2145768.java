package jp.go.ipa.jgcl;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 *  R     :          \   N   X B
 * <p>
 *          A       _     u       X/Y                      W n
 * ( z u     A{@link Axis2Placement3D Axis2Placement3D}) position   
 *    _       _           focalDist
 *      `       B
 * </p>
 * <p>
 * t    p     [ ^             P(t)    p     g   b N \     A           B
 * <pre>
 *	P(t) = position.location() + focalDist * (t * t * position.x() + 2 * t * position.y())
 * </pre>
 * </p>
 *
 * @version $Revision: 1.48 $, $Date: 2000/08/11 06:18:54 $
 * @author Information-technology Promotion Agency, Japan
 */
public class Parabola3D extends Conic3D {

    private static final long serialVersionUID = 1L;

    /**
     *    _       _           B
     * @serial
     */
    private double focalDist;

    /**
     *    _       _             A               t B [   h           B
     * <p>
     * focalDist    l                       B
     * </p>
     * <p>
     * focalDist    l  
     *                      Z               e                      
     * ExceptionGeometryInvalidArgumentValue	     O           B
     * </p>
     *
     * @param focalDist	   _       _          
     * @see	ExceptionGeometryInvalidArgumentValue
     */
    private void setFocalDist(double focalDist) {
        ConditionOfOperation condition = ConditionOfOperation.getCondition();
        double dTol = condition.getToleranceForDistance();
        if (focalDist < dTol) {
            throw new ExceptionGeometryInvalidArgumentValue();
        }
        this.focalDist = focalDist;
    }

    /**
     *        W n     _       _             ^     I u W F N g   \ z     B
     * <p>
     * position    null            A
     * ExceptionGeometryInvalidArgumentValue      O           B
     * </p>
     * <p>
     * focalDist    l  
     *                      Z               e                      
     * ExceptionGeometryInvalidArgumentValue	     O           B
     * </p>
     * 
     * @param position	   S       X/Y/Z                      W n
     * @param focalDist	   _       _          
     * @see	ExceptionGeometryInvalidArgumentValue
     */
    public Parabola3D(Axis2Placement3D position, double focalDist) {
        super(position);
        setFocalDist(focalDist);
    }

    /**
     *              u   _ |   _       v   p     A ^           u   X     Q                       B
     * <p>
     * position    null            A
     * ExceptionGeometryInvalidArgumentValue      O           B
     * </p>
     *
     * @param position	 Q                 u   X               W n
     * @return	 Q            
     * @see	ExceptionGeometryInvalidArgumentValue
     */
    Conic2D toLocal2D(Axis2Placement2D position) {
        return new Parabola2D(position, focalDist());
    }

    /**
     *                _       _                 B
     * 
     * @return	   _       _          
     */
    public double focalDist() {
        return this.focalDist;
    }

    /**
     *            A ^         p     [ ^ l       W l       B
     * 
     * @param param	 p     [ ^ l
     * @return		   W l
     */
    public Pnt3D coordinates(double param) {
        Axis2Placement3D ax = position();
        Vec3D x = ax.x().multiply(param * param * focalDist);
        Vec3D y = ax.y().multiply(2 * param * focalDist);
        return ax.location().add(x.add(y));
    }

    /**
     *            A ^         p     [ ^ l       x N g         B
     * 
     * @param param	 p     [ ^ l
     * @return		   x N g  
     */
    public Vec3D tangentVector(double param) {
        Axis2Placement3D ax = position();
        Vec3D x1 = ax.x().multiply(2 * param * focalDist);
        Vec3D y1 = ax.y().multiply(2 * focalDist);
        return x1.add(y1);
    }

    /**
     *            A ^         p     [ ^ l               B
     * 
     * @param param	 p     [ ^ l
     * @return		    
     */
    public CurveCurvature3D curvature(double param) {
        Axis2Placement3D ax = position();
        double x1len = 2 * param * focalDist;
        double y1len = 2 * focalDist;
        double x2len = 2 * focalDist;
        double tlen = Math.sqrt(x1len * x1len + y1len * y1len);
        double crv = Math.abs(y1len * x2len) / (tlen * tlen * tlen);
        Vec3D ex1 = ax.x().multiply(x1len);
        Vec3D ey1 = ax.y().multiply(y1len);
        Vec3D tangent = ex1.add(ey1);
        Vec3D nrmDir = tangent.cross(ax.z());
        return new CurveCurvature3D(crv, nrmDir.normalized());
    }

    /**
     *            A ^         p     [ ^ l                 B
     * 
     * @param param	 p     [ ^ l
     * @return		      
     */
    public CurveDerivative3D evaluation(double param) {
        Axis2Placement3D ax = position();
        Vec3D ex = ax.x().multiply(param * param * focalDist);
        Vec3D ey = ax.y().multiply(2 * param * focalDist);
        Vec3D ex1 = ax.x().multiply(2 * param * focalDist);
        Vec3D ey1 = ax.y().multiply(2 * focalDist);
        Vec3D ex2 = ax.x().multiply(2 * focalDist);
        Pnt3D d0 = ax.location().add(ex.add(ey));
        Vec3D d1 = ex1.add(ey1);
        Vec3D zero = Vec3D.zeroVector;
        return new CurveDerivative3D(d0, d1, ex2, zero);
    }

    /**
     *  ^         p     [ ^             A
     *          [                       _   p     [ ^ l         B
     * <p>
     *        \ b h  
     * {@link Conic3D#toPolyline(ParameterSection, ToleranceForDistance) 
     * Conic3D.toPolyline(ParameterSection, ToleranceForDistance)}
     *              o       B
     * </p>
     * 
     * @param left	   [ (        )    p     [ ^ l
     * @param right	 E [ (        )    p     [ ^ l
     * @return		           _   p     [ ^ l
     */
    double getPeak(double left, double right) {
        return ((left + right) / 2.0);
    }

    /**
     *            w                   L   x W G               B
     * <p>
     *              A         L   x W G         v f         1       
     * </p>
     *
     * @param pint	         p     [ ^    
     * @return		           w                   L   x W G       z  
     */
    public PureBezierCurve3D[] toPolyBezierCurves(ParameterSection pint) {
        Parabola2D this2D = (Parabola2D) this.toLocal2D(Axis2Placement2D.origin);
        PureBezierCurve2D[] bzcs2D = this2D.toPolyBezierCurves(pint);
        return this.transformPolyBezierCurvesInLocal2DToGrobal3D(bzcs2D);
    }

    /**
     *            w                   L   a X v   C             B
     * 
     * @param pint	         p     [ ^    
     * @return		           w                   L   a X v   C      
     */
    public BsplineCurve3D toBsplineCurve(ParameterSection pint) {
        PureBezierCurve3D[] bzcs = this.toPolyBezierCurves(pint);
        return bzcs[0].toBsplineCurve();
    }

    /**
     *                          _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * <p>
     *                      A
     *                      Z           A
     *                            A
     *              _                   e               A
     *                X          p x   p x     e               A
     *              _ |   _                     e                       A
     *            I [ o [   b v                   A
     * ExceptionGeometryIndefiniteSolution      O             B
     * </p>
     *
     * @param mate	        
     * @return		   _   z  
     * @exception	ExceptionGeometryIndefiniteSolution mate            A           I [ o [   b v         A     s
     */
    public IntersectionPoint3D[] intersect(ParametricCurve3D mate) throws ExceptionGeometryIndefiniteSolution {
        return mate.intersect(this, true);
    }

    /**
     *              (       \        )    R         _   \                       B
     * 
     * @param poly	 x W G             a X v   C             Z O     g         \     z  
     * @return		             poly      _   \                  
     */
    RealPolynomial makePoly(RealPolynomial[] poly) {
        RealPolynomial yPoly = poly[1].multiply(poly[1]);
        double dA4fd = 4.0 * focalDist();
        boolean isPoly = poly.length < 4;
        int degree = yPoly.degree();
        double[] coef = new double[degree + 1];
        if (isPoly) {
            int deg = poly[1].degree();
            for (int j = 0; j <= degree; j++) if (j > (degree - deg)) coef[j] = yPoly.coefficientAt(j); else coef[j] = yPoly.coefficientAt(j) - (dA4fd * poly[0].coefficientAt(j));
        } else {
            RealPolynomial xwPoly = poly[0].multiply(poly[3]);
            for (int j = 0; j <= degree; j++) coef[j] = yPoly.coefficientAt(j) - (dA4fd * xwPoly.coefficientAt(j));
        }
        return new RealPolynomial(coef);
    }

    /**
     *  ^         _                           ` F b N     B
     * 
     * @param point	               _
     * @return		 ^         _                     true A               false
     */
    boolean checkSolution(Pnt3D point) {
        double param = getParameter(point);
        double px = focalDist() * param * param;
        double py = 2.0 * focalDist() * param;
        return point.identical(new CPnt3D(px, py, 0.0));
    }

    /**
     *  ^         _                             A
     *      _             p     [ ^ l         B
     * 
     * @param point	               _
     * @return		 p     [ ^ l
     */
    double getParameter(Pnt3D point) {
        return point.y() / (2.0 * focalDist());
    }

    /**
     *                    ( ~)      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * <p>
     * [        ]
     * <br>
     *          Z  
     * {@link Conic3D#intersectCnc(Conic3D, boolean)
     * Conic3D.intersectCnc(Conic3D, boolean)}
     *    s           B
     * </p>
     * 
     * @param mate               ( ~)
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return		   _   z  
     */
    IntersectionPoint3D[] intersect(Circle3D mate, boolean doExchange) {
        try {
            return intersectCnc(mate, doExchange);
        } catch (ExceptionGeometryIndefiniteSolution e) {
            throw new ExceptionGeometryFatal();
        }
    }

    /**
     *                    (   ~)      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * <p>
     * [        ]
     * <br>
     *          Z  
     * {@link Conic3D#intersectCnc(Conic3D, boolean)
     * Conic3D.intersectCnc(Conic3D, boolean)}
     *    s           B
     * </p>
     * 
     * @param mate               (   ~)
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return		   _   z  
     */
    IntersectionPoint3D[] intersect(Ellipse3D mate, boolean doExchange) {
        try {
            return intersectCnc(mate, doExchange);
        } catch (ExceptionGeometryIndefiniteSolution e) {
            throw new ExceptionGeometryFatal();
        }
    }

    /**
     *                    (      )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * <p>
     * [        ]
     * <br>
     *          Z  
     * {@link Conic3D#intersectCnc(Conic3D, boolean)
     * Conic3D.intersectCnc(Conic3D, boolean)}
     *    s           B
     * </p>
     * <p>
     *                      Z           A
     *                            A
     *              _                   e               A
     *                X          p x   p x     e               A
     *              _ |   _                     e                       A
     *            I [ o [   b v                   A
     * ExceptionGeometryIndefiniteSolution      O             B
     * </p>
     * 
     * @param mate               (      )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return		   _   z  
     * @exception	ExceptionGeometryIndefiniteSolution            I [ o [   b v         A     s
     */
    IntersectionPoint3D[] intersect(Parabola3D mate, boolean doExchange) throws ExceptionGeometryIndefiniteSolution {
        return intersectCnc(mate, doExchange);
    }

    /**
     *                    ( o    )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * <p>
     * [        ]
     * <br>
     *          Z  
     * {@link Conic3D#intersectCnc(Conic3D, boolean)
     * Conic3D.intersectCnc(Conic3D, boolean)}
     *    s           B
     * </p>
     * 
     * @param mate               ( o    )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return		   _   z  
     */
    IntersectionPoint3D[] intersect(Hyperbola3D mate, boolean doExchange) {
        try {
            return intersectCnc(mate, doExchange);
        } catch (ExceptionGeometryIndefiniteSolution e) {
            throw new ExceptionGeometryFatal();
        }
    }

    /**
     *                    ( |     C  )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * <p>
     * [        ]
     * <br>
     *          Z   A
     *  |     C     N   X   u |     C   vs.        v     _   Z   \ b h
     * {@link Polyline3D#intersect(Parabola3D, boolean)
     * Polyline3D.intersect(Parabola3D, boolean)}
     *    s           B
     * </p>
     * 
     * @param mate	         ( |     C  )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return		   _   z  
     */
    IntersectionPoint3D[] intersect(Polyline3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     *                    ( g        )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * <p>
     * [        ]
     * <br>
     *          Z   A
     *  g           N   X   u g         vs.        v     _   Z   \ b h
     * {@link TrimmedCurve3D#intersect(Parabola3D, boolean)
     * TrimmedCurve3D.intersect(Parabola3D, boolean)}
     *    s           B
     * </p>
     * 
     * @param mate	         ( g        )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return		   _   z  
     */
    IntersectionPoint3D[] intersect(TrimmedCurve3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     *                    (         Z O     g)      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * <p>
     * [        ]
     * <br>
     *          Z   A
     *          Z O     g   N   X   u         Z O     g vs.        v     _   Z   \ b h
     * {@link CompositeCurveSegment3D#intersect(Parabola3D, boolean)
     * CompositeCurveSegment3D.intersect(Parabola3D, boolean)}
     *    s           B
     * </p>
     * 
     * @param mate	         (         Z O     g)
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return		   _   z  
     */
    IntersectionPoint3D[] intersect(CompositeCurveSegment3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     *                    (        )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * <p>
     * [        ]
     * <br>
     *          Z   A
     *          N   X   u         vs.        v     _   Z   \ b h
     * {@link CompositeCurve3D#intersect(Parabola3D, boolean)
     * CompositeCurve3D.intersect(Parabola3D, boolean)}
     *    s           B
     * </p>
     * 
     * @param mate	         (        )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return		   _   z  
     */
    IntersectionPoint3D[] intersect(CompositeCurve3D mate, boolean doExchange) {
        return mate.intersect(this, !doExchange);
    }

    /**
     *            A ^         x N g     ]       s                   B
     *
     * @param moveVec	   s                 \   x N g  
     * @return		   s            
     */
    public ParametricCurve3D parallelTranslate(Vec3D moveVec) {
        return new Parabola3D(position().parallelTranslate(moveVec), focalDist);
    }

    /**
     *            p     [ ^   `         B
     * <p>
     *              I   p     [ ^   `         B
     * </p>
     * 
     * @return	             I   p     [ ^   `  
     */
    ParameterDomain getParameterDomain() {
        return new ParameterDomain();
    }

    /**
     *                I                         B
     * <p>
     *              A     false        B
     * </p>
     *
     * @return	                             A     <code>false</code>
     */
    boolean getClosedFlag() {
        return false;
    }

    /**
     *  v f           B
     *
     * @return	{@link ParametricCurve3D#PARABOLA_3D ParametricCurve3D.PARABOLA_3D}
     */
    int type() {
        return PARABOLA_3D;
    }

    /**
     *            A ^               W n   Z            A
     *  ^         p x       ]                 B
     *
     * @param trns	       W n               W       Z q
     * @param rCos	cos(   ] p x)
     * @param rSin	sin(   ] p x)
     * @return		   ]        
     */
    ParametricCurve3D rotateZ(CTransformationOperator3D trns, double rCos, double rSin) {
        Axis2Placement3D rpos = position().rotateZ(trns, rCos, rSin);
        return new Parabola3D(rpos, focalDist());
    }

    /**
     *              _   A ^                     _           B
     *
     * @param line	    
     * @return		 ^                     _
     */
    Pnt3D getPointNotOnLine(Line3D line) {
        ConditionOfOperation condition = ConditionOfOperation.getCondition();
        double dTol2 = condition.getToleranceForDistance2();
        double start = 0.0, increase = 1.0;
        int itry = 0, limit = 100;
        Pnt3D point;
        Vec3D vector;
        do {
            if (itry > limit) {
                throw new ExceptionGeometryFatal();
            }
            point = this.coordinates(start + (increase * itry));
            vector = point.subtract(line.project1From(point));
            itry++;
        } while (point.isOn(line) || vector.lengthSquared() < dTol2);
        return point;
    }

    /**
     *            A           x W G                     B
     * <p>
     *        \ b h   {@link IntsCncBzs3D IntsCncBzs3D}          g       B
     * </p>
     *
     * @param bi	           x W G          
     * @return	               true A               false
     */
    boolean checkInterfere(IntsCncBzs3D.BezierSurfaceInfo bi) {
        double dTol = getToleranceForDistance();
        if (!((bi.box.min().z() < -dTol) && (bi.box.max().z() > dTol))) return false;
        if (bi.box.max().x() < -dTol) return false;
        boolean all_in = true;
        boolean all_out = true;
        Pnt2D point = null;
        for (int i = 0; i < 4; i++) {
            switch(i) {
                case 0:
                    point = new CPnt2D(bi.box.min().x(), bi.box.min().y());
                    break;
                case 1:
                    point = new CPnt2D(bi.box.max().x(), bi.box.min().y());
                    break;
                case 2:
                    point = new CPnt2D(bi.box.max().x(), bi.box.max().y());
                    break;
                case 3:
                    point = new CPnt2D(bi.box.min().x(), bi.box.max().y());
                    break;
            }
            double epara = point.y() / (2.0 * this.focalDist());
            double ex = this.focalDist() * epara * epara;
            ex = point.x() - ex;
            if (ex < -dTol) all_in = false; else if (ex > dTol) all_out = false; else {
                all_out = false;
                all_in = false;
            }
        }
        if (all_in == true) return false; else if (all_out == true) {
            return ((bi.box.min().y() * bi.box.max().y()) > 0.0) ? false : true;
        } else return true;
    }

    /**
     *      ~         ^                 _         B
     * <p>
     *        \ b h   {@link IntsCncBzs3D IntsCncBzs3D}          g       B
     * </p>
     *
     * @param plane      
     * @return          _   z  
     */
    IntersectionPoint3D[] intersectConicPlane(Plane3D plane) {
        Axis2Placement3D position = new Axis2Placement3D(Pnt3D.origin, null, null);
        Parabola3D parabola = new Parabola3D(position, this.focalDist());
        try {
            return parabola.intersect(plane);
        } catch (ExceptionGeometryIndefiniteSolution e) {
            throw new ExceptionGeometryFatal();
        }
    }

    /**
     *            A ^         p     [ ^ l     (     ~               W n      )    W l
     *        B
     * 
     * @param parameter	 p     [ ^ l
     * @return	     ~               W n         W l
     */
    Pnt3D nlFunc(double parameter) {
        double y = this.focalDist() * parameter;
        double x = y * parameter;
        y = 2.0 * y;
        double z = 0.0;
        return new CPnt3D(x, y, z);
    }

    /**
     *            A ^         p     [ ^ l     (     ~               W n      )    x N g  
     *        B
     * 
     * @param parameter	 p     [ ^ l
     * @return	     ~               W n         x N g  
     */
    Vec3D dnlFunc(double parameter) {
        double y = 2.0 * this.focalDist();
        double x = y * parameter;
        double z = 0.0;
        return new LVec3D(x, y, z);
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
    protected synchronized ParametricCurve3D doTransformBy(boolean reverseTransform, CTransformationOperator3D transformationOperator, java.util.HashMap<Geometry, Geometry> transformedGeometries) {
        Axis2Placement3D tPosition = this.position().transformBy(reverseTransform, transformationOperator, transformedGeometries);
        double tFocalDist;
        if (reverseTransform != true) tFocalDist = transformationOperator.transform(this.focalDist()); else tFocalDist = transformationOperator.reverseTransform(this.focalDist());
        return new Parabola3D(tPosition, tFocalDist);
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
        writer.println(indent_tab + "\tposition");
        position().output(writer, indent + 2);
        writer.println(indent_tab + "\tfocalDist " + focalDist);
        writer.println(indent_tab + "End");
    }
}
