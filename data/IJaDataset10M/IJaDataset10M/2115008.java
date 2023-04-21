package jp.go.ipa.jgcl;

/**
 *  R     F V     _   m     _         N   X
 * <p>
 *  ~     m           A Q       u   W                       A               B
 *  ~     m     u   W       A P O                         B
 * </p>
 * @version $Revision: 1.21 $, $Date: 2000/08/11 06:18:52 $
 * @author Information-technology Promotion Agency, Japan
 */
class IntsCylCyl3D {

    /**
     *  V     _     v        
     */
    private static final int COINCIDENT = 0;

    /**
     *  V     _         s      
     */
    private static final int PARALLEL = 1;

    /**
     *  V     _         s     V     _   m     G          
     */
    private static final int PARALLEL_NO_TOUCH = 2;

    /**
     *  V     _         s     V     _   m     G        
     */
    private static final int PARALLEL_TOUCH = 3;

    /**
     *  V     _         s     V     _   m            
     */
    private static final int PARALLEL_INTERSECT = 4;

    /**
     *  V     _                     a          
     */
    private static final int INTERSECT_SAME_RADIUS = 5;

    /** 
     *  V     _                    
     *        V     _         V     _                
     */
    private static final int NO_INTERSECT_OUT_OF_LINE = 6;

    /**
     *  V     _                    
     *        V     _           V     _     S                  
     */
    private static final int NO_INTERSECT_BETWEEN_LINE = 7;

    /**
     *  V     _                    
     *        V     _         V     _   O       G        
     */
    private static final int NO_INTERSECT_TOUCH_OUTSIDE = 8;

    /**
     *  V     _                    
     *        V     _         V     _           G        
     */
    private static final int NO_INTERSECT_TOUCH_INSIDE = 9;

    /** 
     *  V     _                    
     *        V     _         V     _       I            
     */
    private static final int NO_INTERSECT_LINE_INTERSECT = 10;

    /**
     *  V     _  
     */
    private CylindricalSurface3D bCyl;

    /**
     *  V     _  
     */
    private CylindricalSurface3D sCyl;

    /**
     *  V     _       _
     */
    private Pnt3D bOrg;

    /**
     *  V     _       _
     */
    private Pnt3D sOrg;

    /**
     *  V     _      
     */
    private Line3D bAxis;

    /**
     *  V     _      
     */
    private Line3D sAxis;

    /**
     *  P   x N g  
     */
    private Vec3D bCylU;

    private Vec3D bCylV;

    private Vec3D bCylW;

    /**
     *  V     _   m          
     */
    private double w1w2;

    /**
     *        \       |     C     _    
     */
    private static final int nst = 41;

    /**
     *  V     _                     t   O
     */
    private boolean doExchange;

    /**
     *        _(   ~     S     p)
     */
    private IntersectionPoint3D axisIntsP;

    /**
     *    V     _           V     _     _
     */
    private IntersectionPoint3D[] bLineSCylIntsP;

    /**
     * 2     ~                             t   O   ^    
     *                      I u W F N g   \ z     B
     *
     * @param cyl1          V     _
     * @param cyl2          V     _
     * @param doExchange    V     _                     t   O
     */
    private IntsCylCyl3D(CylindricalSurface3D cyl1, CylindricalSurface3D cyl2, boolean doExchange) {
        if (cyl1.radius() < cyl2.radius()) {
            sCyl = cyl1;
            bCyl = cyl2;
            this.doExchange = !doExchange;
        } else {
            sCyl = cyl2;
            bCyl = cyl1;
            this.doExchange = doExchange;
        }
        Axis2Placement3D bPosition = bCyl.position();
        Axis2Placement3D sPosition = sCyl.position();
        bOrg = bPosition.location();
        sOrg = sPosition.location();
        bAxis = bCyl.getAxis();
        sAxis = sCyl.getAxis();
        bCylW = bPosition.z();
        bCylV = bCylW.cross(sOrg.subtract(bOrg)).normalized();
        bCylU = bCylV.cross(bCylW).normalized();
        w1w2 = bAxis.dir().dot(sAxis.dir());
    }

    /**
     *  V     _                     V     _                          
     *
     * @return        2 {      
     */
    private Line3D[] makeLinesOnCylinder() {
        Vec3D zOfSCyl = sAxis.dir();
        Vec3D zOfBCyl = bAxis.dir();
        Vec3D evec = zOfSCyl.cross(zOfBCyl);
        Vec3D axis = evec.cross(zOfBCyl);
        Vec3D refDirection = zOfBCyl;
        Pnt3D location = bCyl.position().location();
        Plane3D bPlane = new Plane3D(new Axis2Placement3D(location, axis, refDirection));
        try {
            SurfaceSurfaceInterference3D[] intf = bCyl.intersect(bPlane);
            int nSol = intf.length;
            Line3D[] lines = new Line3D[nSol];
            for (int i = 0; i < nSol; i++) {
                ParametricCurve3D curve = intf[i].toIntersectionCurve().curve3d();
                if (curve instanceof Line3D) {
                    lines[i] = (Line3D) curve;
                }
            }
            return lines;
        } catch (ExceptionGeometryIndefiniteSolution e) {
            throw new ExceptionGeometryFatal();
        }
    }

    /**
     *  V     _   m         s               A                        
     * <p>
     *  V     _   m           s       A            3                     B
     *                  o           l         p     B
     * </p>
     * @return         V     _   m         \      
     */
    private int getRelationInParallel() {
        Pnt3D source = sOrg;
        PointOnCurve3D[] foot = bAxis.projectFrom(source);
        if (foot.length != 1) throw new ExceptionGeometryFatal();
        double dist = source.distance(foot[0]);
        double dTol = bCyl.getToleranceForDistance();
        double bRadius = bCyl.radius();
        double sRadius = sCyl.radius();
        if (Math.abs(dist - (bRadius + sRadius)) < dTol || Math.abs(dist - (bRadius - sRadius)) < dTol) return PARALLEL_TOUCH; else if (dist > (bRadius + sRadius) || dist < (bRadius - sRadius)) return PARALLEL_NO_TOUCH; else return PARALLEL_INTERSECT;
    }

    /**
     *  V     _   m         s                   A                        
     * <p>
     *  V     _   m           s           A                             B
     *                  o           l         p     B
     * </p>
     * @return         V     _   m         \      
     */
    private int getRelationInNotParallel() {
        double dTol = bCyl.getToleranceForDistance();
        IntersectionPoint3D[] intsP;
        try {
            intsP = bAxis.intersect(sAxis);
        } catch (ExceptionGeometryIndefiniteSolution e) {
            throw new ExceptionGeometryFatal();
        }
        if ((intsP.length == 1) && (Math.abs(bCyl.radius() - sCyl.radius()) < dTol)) {
            axisIntsP = intsP[0];
            return INTERSECT_SAME_RADIUS;
        } else return getRelationInNotParallelNotIntersect();
    }

    /**
     *  V     _   m         s                                     A
     *                         
     * <p>
     *  V     _   m           s           A                           A
     *                              B
     *                  o           l         p     B
     * </p>
     * @return         V     _   m         \      
     */
    private int getRelationInNotParallelNotIntersect() {
        Line3D[] bLines = makeLinesOnCylinder();
        IntersectionPoint3D[] intsPB1;
        IntersectionPoint3D[] intsPB2;
        try {
            intsPB1 = bLines[0].intersect(sCyl);
            intsPB2 = bLines[1].intersect(sCyl);
        } catch (ExceptionGeometryIndefiniteSolution e) {
            throw new ExceptionGeometryFatal();
        }
        int n1 = intsPB1.length;
        int n2 = intsPB2.length;
        if (n1 != 0) bLineSCylIntsP = intsPB1; else if (n2 != 0) bLineSCylIntsP = intsPB2; else bLineSCylIntsP = null;
        if ((n1 == 0) && (n2 == 0)) return noIntersectionPoint(); else if (((n1 == 0) && (n2 == 1)) || ((n1 == 1) && (n2 == 0))) {
            return oneIntersectionPoint();
        } else if (((n1 == 0) && (n2 == 2)) || ((n1 == 2) && (n2 == 0))) return NO_INTERSECT_LINE_INTERSECT; else throw new ExceptionGeometryFatal();
    }

    /**
     *  V     _   m           S           A                                
     * <p>
     *  V     _   m           S           A                             A
     *          l       o         p     B
     * </p>
     * @return         V     _   m         \      
     */
    private int noIntersectionPoint() {
        IntersectionPoint3D[] point;
        try {
            point = bCyl.intersect(sAxis);
        } catch (ExceptionGeometryIndefiniteSolution e) {
            throw new ExceptionGeometryFatal();
        }
        if (point.length == 0) return NO_INTERSECT_OUT_OF_LINE; else return NO_INTERSECT_BETWEEN_LINE;
    }

    /**
     *  V     _   m           G       A O       G                  
     * <p>
     *  V     _   m     G       A           \       l       o         p     B
     * </p>
     * @return         V     _   m         \      
     */
    private int oneIntersectionPoint() {
        IntersectionPoint3D[] point;
        try {
            point = bCyl.intersect(sAxis);
        } catch (ExceptionGeometryIndefiniteSolution e) {
            throw new ExceptionGeometryFatal();
        }
        if (point.length == 0) return NO_INTERSECT_TOUCH_OUTSIDE; else return NO_INTERSECT_TOUCH_INSIDE;
    }

    private int twoIntersectionPoint() {
        return NO_INTERSECT_LINE_INTERSECT;
    }

    /**
     *  V     _   m     W            
     * <p>
     *  V     _   m     v       A
     *  V     _   m         s   A                   A                         B
     * </p>
     * @return         V     _   m         \      
     */
    private int getRelation() {
        if (bCyl.equals(sCyl)) return COINCIDENT;
        if (bCyl.isParallel(sCyl)) {
            return getRelationInParallel();
        } else {
            return getRelationInNotParallel();
        }
    }

    /**
     *  V     _   m      (1     )      
     * <p>
     *    V     _           A   V     _         s               B
     * </p>
     * @return               z  
     */
    private SurfaceSurfaceInterference3D[] makeOneLine() {
        Vec3D bTos = bCylU.multiply(bCyl.radius());
        Pnt3D point = bOrg.add(bTos);
        Vec3D direction = bCylW;
        Line3D res = new Line3D(point, direction);
        IntersectionCurve3D ints = bCyl.curveToIntersectionCurve(res, sCyl, doExchange);
        SurfaceSurfaceInterference3D[] sol = { ints };
        return sol;
    }

    /**
     *  V     _   m      (2     )      
     * <p>
     *  o     V     _   ~   l   A       _         B
     *        _       V     _           L                           B
     * </p>
     * @return               z  
     */
    private SurfaceSurfaceInterference3D[] makeTwoLine() {
        Circle3D bCylCircle = new Circle3D(bCyl.position(), bCyl.radius());
        IntersectionPoint3D[] intsP;
        try {
            intsP = bCylCircle.intersect(sCyl);
        } catch (ExceptionGeometryIndefiniteSolution e) {
            throw new ExceptionGeometryFatal();
        }
        if (intsP.length != 2) throw new ExceptionGeometryFatal();
        Line3D res = new Line3D(intsP[0], bCylW);
        IntersectionCurve3D ints1 = bCyl.curveToIntersectionCurve(res, sCyl, doExchange);
        res = new Line3D(intsP[1], bCylW);
        IntersectionCurve3D ints2 = bCyl.curveToIntersectionCurve(res, sCyl, doExchange);
        SurfaceSurfaceInterference3D[] sol = { ints1, ints2 };
        return sol;
    }

    /**
     *  V     _   m      (2    ~)      
     * <p>
     *  V     _       m     _  2   ~     S       B
     *  V     _   Z       ~     a       B
     *    ~             m               p x                   B
     * </p>
     * @return               z  
     */
    private SurfaceSurfaceInterference3D[] makeTwoEllipse() {
        double theta = bAxis.angleWith(sAxis);
        Vec3D zOfBCyl = bAxis.dir();
        Vec3D zOfSCyl = sAxis.dir();
        Vec3D w1 = zOfSCyl.add(zOfBCyl).normalized();
        Vec3D w2 = zOfSCyl.subtract(zOfBCyl).normalized();
        if (axisIntsP == null) throw new ExceptionGeometryFatal();
        Axis2Placement3D position;
        double xRadius, yRadius;
        double bRadius = bCyl.radius();
        position = new Axis2Placement3D(axisIntsP, w1, w2);
        xRadius = bRadius / Math.cos(theta / 2.0);
        yRadius = bRadius;
        Ellipse3D ellipse = new Ellipse3D(position, xRadius, yRadius);
        IntersectionCurve3D ints1 = bCyl.curveToIntersectionCurve(ellipse, sCyl, doExchange);
        position = new Axis2Placement3D(axisIntsP, w2, w1);
        xRadius = bRadius / Math.sin(theta / 2.0);
        yRadius = bRadius;
        ellipse = new Ellipse3D(position, xRadius, yRadius);
        IntersectionCurve3D ints2 = bCyl.curveToIntersectionCurve(ellipse, sCyl, doExchange);
        SurfaceSurfaceInterference3D[] sol = { ints1, ints2 };
        return sol;
    }

    private Pnt3D uvPointFromT(Pnt3D point, Vec3D uVec, Vec3D vVec, double r, double t) {
        double cost = Math.cos(t);
        double sint = Math.sin(t);
        return new CPnt3D(point.x() + r * ((cost * uVec.x()) + (sint * vVec.x())), point.y() + r * ((cost * uVec.y()) + (sint * vVec.y())), point.z() + r * ((cost * uVec.z()) + (sint * vVec.z())));
    }

    private Vec3D[] getUVVector() {
        Vec3D cSu, cSv, cSw;
        PointOnCurve3D[] cNorm;
        try {
            cNorm = sAxis.commonNormal(bAxis);
        } catch (ExceptionGeometryIndefiniteSolution e) {
            throw new ExceptionGeometryFatal();
        }
        cSw = sAxis.dir();
        cSu = cNorm[1].subtract(cNorm[0]).normalized();
        if (cSu.lengthSquared() == 0) {
            cSu = sCyl.position().effectiveRefDirection();
        }
        cSv = cSw.cross(cSu).normalized();
        Vec3D[] unitVec = { cSu, cSv, cSw };
        return unitVec;
    }

    /**
     *  V     _   m      (2    R    , 8 ^    )      
     * <p>
     * 2   R             B
     *  V     _                       A         p     A
     *    R             |     C   \         B
     * </p>
     * @return               z  
     */
    private SurfaceSurfaceInterference3D[] makeTwoCurve() {
        double step = 2 * Math.PI / (nst - 1);
        double[] dA = new double[3];
        Vec3D[] uvVec = getUVVector();
        Pnt3D ePnt;
        Vec3D evec;
        Vec3D evec2;
        double ework;
        double t;
        Pnt3D[] pArray1 = new Pnt3D[nst];
        Pnt3D[] pArray2 = new Pnt3D[nst];
        double dX0, dX1;
        double[] dX;
        dA[2] = 1.0 - w1w2 * w1w2;
        for (int i = 0; i < nst; i++) {
            t = -Math.PI + i * step;
            ePnt = uvPointFromT(sOrg, uvVec[0], uvVec[1], sCyl.radius(), t);
            evec = sAxis.dir().subtract(bAxis.dir().multiply(w1w2));
            evec2 = ePnt.subtract(bOrg);
            dA[1] = 2.0 * evec.dot(evec2);
            ework = evec2.dot(bAxis.dir());
            dA[0] = evec2.dot(evec2) - (ework * ework) - (bCyl.radius() * bCyl.radius());
            RealPolynomial poly = new RealPolynomial(dA);
            if ((dX = poly.getAlwaysRootsIfQuadric()) == null) {
                throw new ExceptionGeometryFatal();
            }
            if (dX.length == 1) dX0 = dX1 = dX[0]; else if (dX[0] < dX[1]) {
                dX0 = dX[1];
                dX1 = dX[0];
            } else {
                dX0 = dX[0];
                dX1 = dX[1];
            }
            pArray1[i] = ePnt.add(sAxis.dir().multiply(dX0));
            pArray2[i] = ePnt.add(sAxis.dir().multiply(dX1));
        }
        Polyline3D pol = new Polyline3D(pArray1);
        IntersectionCurve3D ints1 = bCyl.curveToIntersectionCurve(pol, sCyl, doExchange);
        pol = new Polyline3D(pArray2);
        IntersectionCurve3D ints2 = bCyl.curveToIntersectionCurve(pol, sCyl, doExchange);
        SurfaceSurfaceInterference3D[] sol = { ints1, ints2 };
        return sol;
    }

    /**
     *  V     _   m      (1  _)      
     *
     * @return           _
     */
    private SurfaceSurfaceInterference3D[] makeOnePoint() {
        if (bLineSCylIntsP.length != 1) throw new ExceptionGeometryFatal();
        SurfaceSurfaceInterferenceList intf = new SurfaceSurfaceInterferenceList(bCyl, sCyl);
        Pnt3D point = bLineSCylIntsP[0].coordinates();
        double[] paramsA = bCyl.pointToParameter(point);
        double[] paramsB = sCyl.pointToParameter(point);
        intf.addAsIntersectionPoint(point, paramsA[0], paramsA[1], paramsB[0], paramsB[1]);
        return intf.toSurfaceSurfaceInterference3DArray(doExchange);
    }

    /**
     *  V     _   m      (8 ^    )      
     * <p>
     * makeTwoCurve()   \ b h      
     * </p>
     * @return               z  
     */
    private SurfaceSurfaceInterference3D[] makeEightFigureCurve() {
        return makeTwoCurve();
    }

    /**
     *  V     _   m      (1    R    )      
     * <p>
     * 1   R             B
     *  V     _                       A         p     A
     *    R             |     C   \         B
     * </p>
     * @return               z  
     */
    private SurfaceSurfaceInterference3D[] makeOneCurve() {
        int myNst = 2 * nst - 1;
        double[] dA = new double[3];
        Vec3D[] uvVec = getUVVector();
        Pnt3D ePnt;
        Vec3D evec;
        Vec3D evec2;
        double ework;
        double t;
        RealPolynomial poly;
        double dX0, dX1;
        double dX[];
        Pnt3D[] pArray = new Pnt3D[myNst];
        evec = bLineSCylIntsP[0].subtract(sOrg);
        evec = evec.project(uvVec[2]).normalized();
        double t0 = Math.acos(evec.dot(uvVec[0]));
        double step = (2.0 * t0) / (nst - 1);
        dA[2] = 1.0 - w1w2 * w1w2;
        for (int i = 0; i < (nst - 1); i++) {
            t = (-t0) + i * step;
            ePnt = uvPointFromT(sOrg, uvVec[0], uvVec[1], sCyl.radius(), t);
            evec = sAxis.dir().subtract(bAxis.dir().multiply(w1w2));
            evec2 = ePnt.subtract(bOrg);
            dA[1] = 2.0 * evec.dot(evec2);
            ework = evec2.dot(bAxis.dir());
            dA[0] = evec2.dot(evec2) - (ework * ework) - (bCyl.radius() * bCyl.radius());
            poly = new RealPolynomial(dA);
            if ((dX = poly.getAlwaysRootsIfQuadric()) == null) {
                throw new ExceptionGeometryFatal();
            }
            if (dX.length == 1) dX0 = dX1 = dX[0]; else if (dX[0] < dX[1]) {
                dX0 = dX[1];
                dX1 = dX[0];
            } else {
                dX0 = dX[0];
                dX1 = dX[1];
            }
            pArray[i] = ePnt.add(sAxis.dir().multiply(dX0));
            pArray[myNst - 1 - i] = ePnt.add(sAxis.dir().multiply(dX1));
        }
        t = t0;
        ePnt = uvPointFromT(sOrg, uvVec[0], uvVec[1], sCyl.radius(), t);
        evec = sAxis.dir().subtract(bAxis.dir().multiply(w1w2));
        evec2 = ePnt.subtract(bOrg);
        dA[1] = 2.0 * evec.dot(evec2);
        ework = evec2.dot(bAxis.dir());
        dA[0] = evec2.dot(evec2) - (ework * ework) - (bCyl.radius() * bCyl.radius());
        poly = new RealPolynomial(dA);
        if ((dX = poly.getAlwaysRootsIfQuadric()) == null) {
            throw new ExceptionGeometryFatal();
        }
        if (dX.length == 1) dX0 = dX1 = dX[0]; else if (dX[0] < dX[1]) {
            dX0 = dX[1];
            dX1 = dX[0];
        } else {
            dX0 = dX[0];
            dX1 = dX[1];
        }
        pArray[nst - 1] = ePnt.add(sAxis.dir().multiply(dX0));
        Polyline3D pol = new Polyline3D(pArray);
        IntersectionCurve3D ints = bCyl.curveToIntersectionCurve(pol, sCyl, doExchange);
        SurfaceSurfaceInterference3D[] sol = { ints };
        return sol;
    }

    private SurfaceSurfaceInterference3D[] noIntersectionCurve() {
        return new SurfaceSurfaceInterference3D[0];
    }

    /**
     *  V     _   m              
     * <p>
     * 2     V     _     u   W               s   A
     *                        @   g     A               B
     * </p>
     * @param cyl1        V     _1
     * @param cyl2        V     _2
     * @param doExchange  V     _                     t   O
     * @return                  z  
     */
    static SurfaceSurfaceInterference3D[] intersection(CylindricalSurface3D cyl1, CylindricalSurface3D cyl2, boolean doExchange) throws ExceptionGeometryIndefiniteSolution {
        SurfaceSurfaceInterference3D[] result;
        IntsCylCyl3D doObj = new IntsCylCyl3D(cyl1, cyl2, doExchange);
        switch(doObj.getRelation()) {
            case COINCIDENT:
                throw new ExceptionGeometryIndefiniteSolution(cyl1);
            case PARALLEL_NO_TOUCH:
                return doObj.noIntersectionCurve();
            case PARALLEL_TOUCH:
                return doObj.makeOneLine();
            case PARALLEL_INTERSECT:
                return doObj.makeTwoLine();
            case INTERSECT_SAME_RADIUS:
                return doObj.makeTwoEllipse();
            case NO_INTERSECT_OUT_OF_LINE:
                return doObj.noIntersectionCurve();
            case NO_INTERSECT_BETWEEN_LINE:
                return doObj.makeTwoCurve();
            case NO_INTERSECT_TOUCH_OUTSIDE:
                return doObj.makeOnePoint();
            case NO_INTERSECT_TOUCH_INSIDE:
                return doObj.makeEightFigureCurve();
            case NO_INTERSECT_LINE_INTERSECT:
                return doObj.makeOneCurve();
        }
        throw new ExceptionGeometryFatal();
    }
}
