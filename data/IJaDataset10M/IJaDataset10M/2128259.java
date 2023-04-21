package jp.go.ipa.jgcl;

import org.magiclight.common.VectorNS;
import java.io.PrintWriter;
import org.magiclight.common.MLUtil;

/**
 * A composite curve segment is a bounded curve together with transition information
 * which is used to construct a composite curve.
 *
 * @author Information-technology Promotion Agency, Japan
 * @see	CompositeCurve2D
 */
public class CompositeCurveSegment2D extends BoundedCurve2D {

    private static final long serialVersionUID = 1L;

    /**
     *      Z O     g     A               B
     * @serial
     * @see TransitionCodeType
     */
    private int transition;

    /**
     *  Z O     g                               t   O B
     * @serial
     */
    private boolean sameSense;

    /**
     *  Z O     g         O     \         B
     * @serial
     */
    private BoundedCurve2D parentCurve;

    /**
     *  e t B [   h           l   ^     I u W F N g   \ z     B
     * <p>
     *        l   <a href="#CONSTRAINTS">[         S      ]</a>                      A
     * ExceptionGeometryInvalidArgumentValue      O           B
     * </p>
     * 
     * @param transition	     Z O     g     A              
     * @param sameSense		                             t   O
     * @param parentCurve	      
     * @see	TransitionCodeType
     * @see	ExceptionGeometryInvalidArgumentValue
     */
    public CompositeCurveSegment2D(int transition, boolean sameSense, BoundedCurve2D parentCurve) {
        super();
        if (parentCurve.isPeriodic() || parentCurve.isInfinite()) throw new ExceptionGeometryInvalidArgumentValue();
        this.transition = transition;
        this.sameSense = sameSense;
        this.parentCurve = parentCurve;
    }

    /**
     *      Z O     g       Z O     g     A                     B
     * 
     * @return	     Z O     g     A              
     * @see	TransitionCodeType
     */
    public int transition() {
        return this.transition;
    }

    /**
     *      Z O     g                               t   O       B
     * 
     * @return	                           t   O
     */
    public boolean sameSense() {
        return this.sameSense;
    }

    /**
     *      Z O     g               B
     * 
     * @return	      
     */
    public BoundedCurve2D parentCurve() {
        return this.parentCurve;
    }

    /**
     *      L         J n _       B
     *
     * @return	 J n _
     */
    public Pnt2D startPoint() {
        if (sameSense) return parentCurve.startPoint(); else return parentCurve.endPoint();
    }

    /**
     *      L         I   _       B
     *
     * @return	 I   _
     */
    public Pnt2D endPoint() {
        if (sameSense) return parentCurve.endPoint(); else return parentCurve.startPoint();
    }

    /**
     *      L         J n _   p     [ ^ l       B
     *
     * @return	 J n _   p     [ ^ l
     */
    double sParameter() {
        return parameterDomain().section().start();
    }

    /**
     *      L         I   _   p     [ ^ l       B
     *
     * @return	 I   _   p     [ ^ l
     */
    double eParameter() {
        return parameterDomain().section().end();
    }

    /**
     *      Z O     g         ^         p     [ ^ l   A
     *                p     [ ^ l           B
     * <p>
     *      Z O     g     `               `       v       A
     * sameSense    false            A p     [ ^ l         K v       B
     * </p>
     * <p>
     *  ^         p     [ ^ l               `     O                
     * ParameterOutOfRange      O           B
     * </p>
     *
     * @param param	 Z O     g         p     [ ^ l
     * @return	               p     [ ^ l
     */
    private double toBasisParameter(double param) {
        checkValidity(param);
        if (sameSense) return param; else {
            ParameterSection sec = parameterDomain().section();
            return sec.end() - (param - sec.start());
        }
    }

    /**
     *      Z O     g         ^         p     [ ^       A
     *                p     [ ^               B
     * <p>
     *      Z O     g     `               `       v       A
     * sameSense    false            A p     [ ^ l         K v       B
     * </p>
     * <p>
     *  ^         p     [ ^                   `     O                
     * ParameterOutOfRange      O           B
     * </p>
     *
     * @param pint	 Z O     g         p     [ ^    
     * @return	               p     [ ^    
     */
    public ParameterSection toBasisParameter(ParameterSection pint) {
        double start = toBasisParameter(pint.start());
        double end = toBasisParameter(pint.end());
        return new ParameterSection(start, end - start);
    }

    /**
     *      Z O     g                 ^         p     [ ^ l   A
     *      Z O     g         p     [ ^ l           B
     * <p>
     *      Z O     g     `               `       v       A
     * sameSense    false            A p     [ ^ l         K v       B
     * </p>
     *
     * @param param	               p     [ ^ l
     * @return	 Z O     g         p     [ ^ l
     */
    private double toOwnParameter(double param) {
        double result;
        if (sameSense) result = param; else {
            ParameterSection sec = parameterDomain().section();
            result = sec.start() - (param - sec.end());
        }
        return result;
    }

    /**
     *  ^         p     [ ^                                       (      )        B
     * <p>
     * pint        l                   B
     * </p>
     * <p>
     *  ^         p     [ ^                   `     O                
     * ParameterOutOfRange      O           B
     * </p>
     * 
     * @param pint	                   p     [ ^    
     * @return	 w         p     [ ^                      
     * @see	ParameterOutOfRange
     */
    public double length(ParameterSection pint) {
        return parentCurve.length(toBasisParameter(pint));
    }

    /**
     *            A ^         p     [ ^ l       W l       B
     * <p>
     *  ^         p     [ ^ l     `     O                 A
     * ParameterOutOfRange      O           B
     * </p>
     * 
     * @param param	 p     [ ^ l
     * @return		   W l
     * @see	ParameterOutOfRange
     */
    public Pnt2D coordinates(double param) {
        return parentCurve.coordinates(toBasisParameter(param));
    }

    /**
     *            A ^         p     [ ^ l       x N g         B
     * <p>
     *  ^         p     [ ^ l     `     O                 A
     * ParameterOutOfRange      O           B
     * </p>
     * 
     * @param param	 p     [ ^ l
     * @return		   x N g  
     * @see	ParameterOutOfRange
     */
    public Vec2D tangentVector(double param) {
        Vec2D tang = parentCurve.tangentVector(toBasisParameter(param));
        if (sameSense) return tang; else return tang.multiply(-1);
    }

    /**
     *            A ^         p     [ ^ l               B
     * <p>
     *  ^         p     [ ^ l     `     O                 A
     * ParameterOutOfRange      O           B
     * </p>
     * 
     * @param param	 p     [ ^ l
     * @return		    
     * @see	ParameterOutOfRange
     */
    public CurveCurvature2D curvature(double param) {
        return parentCurve.curvature(toBasisParameter(param));
    }

    /**
     *            A ^         p     [ ^ l                 B
     * <p>
     *  ^         p     [ ^ l     `     O                 A
     * ParameterOutOfRange      O           B
     * </p>
     * 
     * @param param	 p     [ ^ l
     * @return		      
     * @see	ParameterOutOfRange
     */
    public CurveDerivative2D evaluation(double param) {
        CurveDerivative2D curv = parentCurve.evaluation(toBasisParameter(param));
        if (sameSense) return curv; else {
            CurveDerivative2D rcurv = new CurveDerivative2D(curv.d0D(), curv.d1D().multiply(-1), curv.d2D());
            return rcurv;
        }
    }

    /**
     *                _       B
     * <p>
     *      _                       0    z         B
     * </p>
     * 
     * @return	     _   z  
     * @exception	ExceptionGeometryIndefiniteSolution	     s
     */
    public PointOnCurve2D[] singular() throws ExceptionGeometryIndefiniteSolution {
        PointOnCurve2D[] singular = parentCurve.singular();
        PointOnCurve2D[] thisSingular = new PointOnCurve2D[singular.length];
        for (int i = 0; i < singular.length; i++) {
            try {
                thisSingular[i] = new PointOnCurve2D(this, toOwnParameter(singular[i].parameter()));
            } catch (ExceptionGeometryInvalidArgumentValue e) {
                throw new ExceptionGeometryFatal();
            }
        }
        return thisSingular;
    }

    /**
     *                _       B
     * <p>
     *      _                       0    z         B
     * </p>
     * 
     * @return	     _   z  
     * @exception	ExceptionGeometryIndefiniteSolution	     s
     */
    public PointOnCurve2D[] inflexion() throws ExceptionGeometryIndefiniteSolution {
        PointOnCurve2D[] inflexion = parentCurve.inflexion();
        PointOnCurve2D[] thisInflexion = new PointOnCurve2D[inflexion.length];
        for (int i = 0; i < inflexion.length; i++) {
            try {
                thisInflexion[i] = new PointOnCurve2D(this, toOwnParameter(inflexion[i].parameter()));
            } catch (ExceptionGeometryInvalidArgumentValue e) {
                throw new ExceptionGeometryFatal();
            }
        }
        return thisInflexion;
    }

    /**
     *  ^         _                   e _         B
     * <p>
     *    e _                       0    z         B
     * </p>
     * 
     * @param point	   e     _
     * @return	   e _
     * @exception ExceptionGeometryIndefiniteSolution	     s
     */
    public PointOnCurve2D[] projectFrom(Pnt2D point) throws ExceptionGeometryIndefiniteSolution {
        PointOnCurve2D[] proj = parentCurve.projectFrom(point);
        PointOnCurve2D[] thisProj = new PointOnCurve2D[proj.length];
        for (int i = 0; i < proj.length; i++) {
            try {
                thisProj[i] = new PointOnCurve2D(this, toOwnParameter(proj[i].parameter()));
            } catch (ExceptionGeometryInvalidArgumentValue e) {
                throw new ExceptionGeometryFatal();
            }
        }
        return thisProj;
    }

    /**
     *            w           A ^                           |     C         B
     * <p>
     *                    |     C     \       _  
     *            x [ X           PointOnCurve2D   
     *                      B
     * </p>
     * <p>
     * section    l   A     x W G         `     O                
     * ParameterOutOfRange      O           B
     * </p>
     *
	 * @param pint
	 * @param tol
	 * @return		           w                       |     C
     * @see	ParameterOutOfRange
     */
    public Polyline2D toPolyline(ParameterSection pint, ToleranceForDistance tol) {
        Polyline2D pl = parentCurve.toPolyline(toBasisParameter(pint), tol);
        Pnt2D[] pnts = new Pnt2D[pl.nPoints()];
        for (int i = 0; i < pnts.length; i++) {
            PointOnCurve2D p = (PointOnCurve2D) pl.pointAt(i);
            pnts[i] = new PointOnCurve2D(this, toOwnParameter(p.parameter()), MLUtil.DEBUG);
        }
        return new Polyline2D(pnts);
    }

    /**
     *            w                         L   Bspline            B
     * <p>
     * pint    l   A     a X v   C           `     O                
     * ParameterOutOfRange      O           B
     * </p>
     * 
     * @param pint	 L   Bspline                p     [ ^    
     * @return		           w                   L   Bspline     
     * @see	ParameterOutOfRange
     */
    public BsplineCurve2D toBsplineCurve(ParameterSection pint) {
        return parentCurve.toBsplineCurve(toBasisParameter(pint));
    }

    /**
     *                        _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * <p>
     * [        ]
     *                                _             A
     *                        p     [ ^ l  
     *      Z O     g         p     [ ^ l              
     *    u   _ v           B
     * </p>
     * 
     * @param mate	        
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return	   _   z  
     */
    private IntersectionPoint2D[] doIntersect(ParametricCurve2D mate, boolean doExchange) {
        IntersectionPoint2D[] ints;
        try {
            ints = parentCurve.intersect(mate);
        } catch (ExceptionGeometryIndefiniteSolution e) {
            return new IntersectionPoint2D[0];
        }
        IntersectionPoint2D[] thisInts = new IntersectionPoint2D[ints.length];
        for (int i = 0; i < ints.length; i++) {
            double param = toOwnParameter(ints[i].pointOnCurve1().parameter());
            PointOnCurve2D thisPnts = new PointOnCurve2D(this, param, MLUtil.DEBUG);
            if (!doExchange) thisInts[i] = new IntersectionPoint2D(thisPnts, ints[i].pointOnCurve2(), MLUtil.DEBUG); else thisInts[i] = new IntersectionPoint2D(ints[i].pointOnCurve2(), thisPnts, MLUtil.DEBUG);
        }
        return thisInts;
    }

    /**
     *                        _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * 
     * @param mate	        
     * @return	   _   z  
     */
    public IntersectionPoint2D[] intersect(ParametricCurve2D mate) {
        return doIntersect(mate, false);
    }

    /**
     *                    (    )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * 
     * @param mate	         (    )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return	   _   z  
     * @see	#doIntersect(ParametricCurve2D, boolean)
     */
    IntersectionPoint2D[] intersect(Line2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     *                    ( ~)      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * 
     * @param mate	         ( ~)
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return	   _   z  
     * @see	#doIntersect(ParametricCurve2D, boolean)
     */
    IntersectionPoint2D[] intersect(Circle2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     *                    (   ~)      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * 
     * @param mate	         (   ~)
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return	   _   z  
     * @see	#doIntersect(ParametricCurve2D, boolean)
     */
    IntersectionPoint2D[] intersect(Ellipse2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     *                    (      )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * 
     * @param mate	         (      )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return	   _   z  
     * @see	#doIntersect(ParametricCurve2D, boolean)
     */
    IntersectionPoint2D[] intersect(Parabola2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     *                    ( o    )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * 
     * @param mate	         ( o    )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return	   _   z  
     * @see	#doIntersect(ParametricCurve2D, boolean)
     */
    IntersectionPoint2D[] intersect(Hyperbola2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     *                    ( |     C  )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * 
     * @param mate	         ( |     C  )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return	   _   z  
     * @see	#doIntersect(ParametricCurve2D, boolean)
     */
    IntersectionPoint2D[] intersect(Polyline2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     *                    ( x W G    )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * 
     * @param mate	         ( x W G    )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return	   _   z  
     * @see	#doIntersect(ParametricCurve2D, boolean)
     */
    IntersectionPoint2D[] intersect(PureBezierCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     *                    ( a X v   C      )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * 
     * @param mate	         ( a X v   C      )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return	   _   z  
     * @see	#doIntersect(ParametricCurve2D, boolean)
     */
    IntersectionPoint2D[] intersect(BsplineCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     *                    ( g        )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * 
     * @param mate	         ( g        )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return	   _   z  
     * @see	#doIntersect(ParametricCurve2D, boolean)
     */
    IntersectionPoint2D[] intersect(TrimmedCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     *                    (         Z O     g)      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * 
     * @param mate	         (         Z O     g)
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return	   _   z  
     * @see	#doIntersect(ParametricCurve2D, boolean)
     */
    IntersectionPoint2D[] intersect(CompositeCurveSegment2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     *                    (        )      _         B
     * <p>
     *    _                       0    z         B
     * </p>
     * 
     * @param mate	         (        )
     * @param doExchange	   _   pointOnCurve1/2                   
     * @return	   _   z  
     * @see	#doIntersect(ParametricCurve2D, boolean)
     */
    IntersectionPoint2D[] intersect(CompositeCurve2D mate, boolean doExchange) {
        return doIntersect(mate, doExchange);
    }

    /**
     *      L             L                     B
     * <p>
     *                              0    z         B
     * </p>
     * 
     * @param mate	        
     * @return		               z  
     */
    public CurveCurveInterference2D[] interfere(BoundedCurve2D mate) {
        return this.interfere(mate, false);
    }

    /**
     *                p     [ ^ l    
     *  Z O     g         p     [ ^ l               \       N   X B
     * @see	#interfere(BoundedCurve2D, boolean)
     */
    class ToSegmentConversion extends ParameterConversion2D {

        /**
	 *      ^       I u W F N g   \ z     B
	 */
        ToSegmentConversion() {
        }

        /**
	 *                p     [ ^ l   Z O     g         p     [ ^ l           B
	 * 
	 * @param param	               p     [ ^ l
	 * @return	 Z O     g         p     [ ^ l
	 */
        double convParameter(double param) {
            return CompositeCurveSegment2D.this.toOwnParameter(param);
        }

        /**
	 *                p     [ ^ l                     Z O     g       B
	 * 
	 * @param param                p     [ ^ l
	 * @return	 Z O     g
	 */
        ParametricCurve2D convCurve(double param) {
            return CompositeCurveSegment2D.this;
        }
    }

    /**
     *      L             L                     B
     * <p>
     *                              0    z         B
     * </p>
     * <p>
     * [        ]
     * <br>
     *                        L                         A
     *                        p     [ ^ l  
     *      Z O     g         p     [ ^ l              
     *    u     v           B
     * </p>
     * 
     * @param mate	        
     * @param doExchange	             this    mate      u                  
     * @return		               z  
     * @see	CompositeCurveSegment2D.ToSegmentConversion
     */
    CurveCurveInterference2D[] interfere(BoundedCurve2D mate, boolean doExchange) {
        ToSegmentConversion conv = new ToSegmentConversion();
        ParameterSection sec = this.parameterDomain().section();
        CurveCurveInterference2D[] intf;
        if (!doExchange) {
            intf = this.parentCurve.interfere(mate);
        } else {
            intf = mate.interfere(this.parentCurve);
        }
        VectorNS<CurveCurveInterference2D> vec = new VectorNS<CurveCurveInterference2D>();
        for (int i = 0; i < intf.length; i++) {
            CurveCurveInterference2D trimintf;
            if (!doExchange) {
                trimintf = intf[i].trim1(sec, conv);
            } else {
                trimintf = intf[i].trim2(sec, conv);
            }
            if (trimintf != null) vec.addElement(trimintf);
        }
        CurveCurveInterference2D[] interf = new CurveCurveInterference2D[vec.size()];
        vec.copyInto(interf);
        return interf;
    }

    /**
     *      L             L       (    )                B
     * <p>
     *                              0    z         B
     * </p>
     * 
     * @param mate	     L       (    )
     * @param doExchange	             this    mate      u                  
     * @return		        
     * @see	#interfere(BoundedCurve2D, boolean)
     */
    CurveCurveInterference2D[] interfere(BoundedLine2D mate, boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     *      L             L       ( |     C  )                B
     * <p>
     *                              0    z         B
     * </p>
     * 
     * @param mate	     L       ( |     C  )
     * @param doExchange	             this    mate      u                  
     * @return		        
     * @see	#interfere(BoundedCurve2D, boolean)
     */
    CurveCurveInterference2D[] interfere(Polyline2D mate, boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     *      L             L       ( x W G    )                B
     * <p>
     *                              0    z         B
     * </p>
     * 
     * @param mate	     L       ( x W G    )
     * @param doExchange	             this    mate      u                  
     * @return		        
     * @see	#interfere(BoundedCurve2D, boolean)
     */
    CurveCurveInterference2D[] interfere(PureBezierCurve2D mate, boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     *      L             L       ( a X v   C      )                B
     * <p>
     *                              0    z         B
     * </p>
     * 
     * @param mate	     L       ( a X v   C      )
     * @param doExchange	             this    mate      u                  
     * @return		        
     * @see	#interfere(BoundedCurve2D, boolean)
     */
    CurveCurveInterference2D[] interfere(BsplineCurve2D mate, boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     *      L             L       ( g        )                B
     * <p>
     *                              0    z         B
     * </p>
     * 
     * @param mate	     L       ( g        )
     * @param doExchange	             this    mate      u                  
     * @return		        
     * @see	#interfere(BoundedCurve2D, boolean)
     */
    CurveCurveInterference2D[] interfere(TrimmedCurve2D mate, boolean doExchange) {
        return this.interfere((BoundedCurve2D) mate, doExchange);
    }

    /**
     *      L             L       (         Z O     g)                B
     * <p>
     *                              0    z         B
     * </p>
     * 
     * @param mate	     L       (         Z O     g)
     * @param doExchange	             this    mate      u                  
     * @return		        
     * @see	#interfere(BoundedCurve2D, boolean)
     */
    CurveCurveInterference2D[] interfere(CompositeCurveSegment2D mate, boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     *      L             L       (        )                B
     * <p>
     *                              0    z         B
     * </p>
     * 
     * @param mate	     L       (        )
     * @param doExchange	             this    mate      u                  
     * @return		        
     * @see	#interfere(BoundedCurve2D, boolean)
     */
    CurveCurveInterference2D[] interfere(CompositeCurve2D mate, boolean doExchange) {
        return mate.interfere(this, !doExchange);
    }

    /**
     *            w           I t Z b g           A
     *  ^                       Bspline              B
     * 
     * @param pint	 I t Z b g     p     [ ^    
     * @param magni	 I t Z b g  
     * @param side       I t Z b g       (SideType.LEFT/RIGHT)
     * @param tol	         e    
     * @return		           w           I t Z b g               Bspline     
     * @see	SideType
     */
    public BsplineCurve2D offsetByBsplineCurve(ParameterSection pint, double magni, int side, ToleranceForDistance tol) {
        Ofst2D doObj = new Ofst2D(this, pint, magni, side, tol);
        return doObj.offset();
    }

    /**
     *            w           I t Z b g           A
     *  ^                       L               B
     * 
     * @param pint	 I t Z b g     p     [ ^    
     * @param magni	 I t Z b g  
     * @param side       I t Z b g       (SideType.LEFT/RIGHT)
     * @param tol	         e    
     * @return		           w           I t Z b g               L      
     * @see	SideType
     */
    public BoundedCurve2D offsetByBoundedCurve(ParameterSection pint, double magni, int side, ToleranceForDistance tol) {
        ParameterSection basisPint = this.toBasisParameter(pint);
        int basisSide = (this.sameSense() == true) ? side : SideType.reverse(side);
        return this.parentCurve().offsetByBoundedCurve(basisPint, magni, basisSide, tol);
    }

    /**
     *            w           A           w                 t B   b g         B
     * <p>
     *  t B   b g                         0    z         B
     * </p>
     * 
     * @param pint1	           p     [ ^    
     * @param side1	                     t B   b g                 t   O
     *			(SideType.LEFT           ARIGHT       E   ABOTH          )
     * @param mate	        
     * @param pint2	           p     [ ^    
     * @param side2	                     t B   b g                 t   O
     *			(SideType.LEFT           ARIGHT       E   ABOTH          )
     * @param radius	 t B   b g   a
     * @param doExchange	 t B   b g   point1/2                   
     * @return		 t B   b g   z  
     * @exception ExceptionGeometryIndefiniteSolution	   s   (                        )
     */
    FilletObject2D[] doFillet(ParameterSection pint1, int side1, ParametricCurve2D mate, ParameterSection pint2, int side2, double radius, boolean doExchange) throws ExceptionGeometryIndefiniteSolution {
        FilletObject2D[] flts;
        try {
            flts = parentCurve.fillet(toBasisParameter(pint1), side1, mate, pint2, side2, radius);
        } catch (ExceptionGeometryIndefiniteSolution e) {
            flts = new FilletObject2D[1];
            flts[0] = (FilletObject2D) e.suitable();
        }
        for (int i = 0; i < flts.length; i++) {
            double param = toOwnParameter(flts[i].pointOnCurve1().parameter());
            PointOnCurve2D thisPnt = new PointOnCurve2D(this, param, MLUtil.DEBUG);
            if (!doExchange) flts[i] = new FilletObject2D(radius, flts[i].center(), thisPnt, flts[i].pointOnCurve2()); else flts[i] = new FilletObject2D(radius, flts[i].center(), flts[i].pointOnCurve2(), thisPnt);
        }
        return flts;
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
     *            p     [ ^   `         B
     * <p>
     *            `         B
     * </p>
     *
     * @return	 L           I   p     [ ^   `  
     */
    ParameterDomain getParameterDomain() {
        return parentCurve.parameterDomain();
    }

    /**
     *  v f           B
     *
     * @return	{@link ParametricCurve2D#COMPOSITE_CURVE_SEGMENT_2D ParametricCurve2D.COMPOSITE_CURVE_SEGMENT_2D}
     */
    int type() {
        return COMPOSITE_CURVE_SEGMENT_2D;
    }

    /**
     *          v f     R `               B
     *
     * @return	           R `           true A               false
     */
    public boolean isFreeform() {
        return this.parentCurve.isFreeform();
    }

    /**
     *      Z O     g   u     Z O     g     A               v  
     *  w     l     X     I u W F N g       B
     *
     * @param transition	     Z O     g     A              
     * @return	 w       X                 Z O     g
     */
    CompositeCurveSegment2D makeCopyWithTransition(int transition) {
        return new CompositeCurveSegment2D(transition, this.sameSense, this.parentCurve);
    }

    /**
     *      Z O     g   u     Z O     g     A               v  
     *  w     l     X   A Z O     g           ]       I u W F N g       B
     *
     * @param transition	     Z O     g     A              
     * @return	 w       X                 Z O     g
     */
    CompositeCurveSegment2D makeReverseWithTransition(int transition) {
        return new CompositeCurveSegment2D(transition, !this.sameSense, this.parentCurve);
    }

    /**
     *      Z O     g   A ^         p     [ ^         f     I u W F N g       B
     *
     * @param section	   f     c         \   p     [ ^    
     * @return	   f     c           \   Z O     g
     */
    CompositeCurveSegment2D truncate(ParameterSection section, int transition) {
        ParameterSection parentSection = this.toBasisParameter(section);
        TrimmedCurve2D newParent;
        if (this.parentCurve.type() == TRIMMED_CURVE_2D) {
            TrimmedCurve2D trc = (TrimmedCurve2D) this.parentCurve;
            newParent = new TrimmedCurve2D(trc.basisCurve(), parentSection);
        } else {
            newParent = new TrimmedCurve2D(this.parentCurve, parentSection);
        }
        return new CompositeCurveSegment2D(transition, true, newParent);
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
        BoundedCurve2D tParentCurve = (BoundedCurve2D) this.parentCurve().transformBy(reverseTransform, transformationOperator, transformedGeometries);
        return new CompositeCurveSegment2D(this.transition(), this.sameSense(), tParentCurve);
    }

    /**
     *            |     C                           B
     *
     * @return	           |     C           A
     *		         g   \         i       |     C               true A
     *		               false
     */
    protected boolean hasPolyline() {
        return parentCurve.hasPolyline();
    }

    /**
     *            |     C                                     B
     *
     * @return	           |     C           A
     *		         g   \         i       |     C                   true A
     *		               false
     */
    protected boolean isComposedOfOnlyPolylines() {
        return parentCurve.isComposedOfOnlyPolylines();
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
        writer.println(indent_tab + "\ttransition\t" + TransitionCodeType.toString(transition));
        writer.println(indent_tab + "\tsameSense\t" + sameSense);
        writer.println(indent_tab + "\tparentCurve");
        parentCurve.output(writer, indent + 2);
        writer.println(indent_tab + "End");
    }
}
