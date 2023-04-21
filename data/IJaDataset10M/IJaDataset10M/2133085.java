package jp.go.ipa.jgcl;

/**
 * 2           _           R       \       N   X
 *
 * @version $Revision: 1.3 $, $Date: 2000/04/26 09:38:56 $
 * @author Information-technology Promotion Agency, Japan
 */
abstract class FreeformSurfaceWithGivenControlPointsArray2D extends ParametricSurface2D {

    /**
     *      _   d     \  3     z  
     * <p>
     *              3          L     A
     * [][][0], [][][1]                W l   ^                  
     * </p>
     * @serial
     */
    protected double[][][] controlPointsArray;

    /**
     *  L        
     * @serial
     */
    boolean isRational;

    /**
     *      _(   d  )  3     z         ^          / L         \ z    
     * <p>
     *  ^         z     x W G           _   z                                  
     * </p>
     *
     * @param cpArray	     _ A d     \   z  
     */
    protected FreeformSurfaceWithGivenControlPointsArray2D(double[][][] cpArray) {
        super();
        controlPointsArray = cpArray;
        isRational = (controlPointsArray[0][0].length == 3) ? true : false;
    }

    /**
     *      _         i [     double    O     z           l      
     *
     * @param isPoly	       `          ?
     * @param uSize	 z               v f  
     * @param vSize	 z               v f  
     * @return		     _         i [     z  
     */
    protected static double[][][] allocateDoubleArray(boolean isPoly, int uSize, int vSize) {
        return new double[uSize][vSize][(isPoly) ? 2 : 3];
    }

    protected double[][][] toDoubleArray() {
        return controlPointsArray;
    }

    /**
     * (i, j)           _   X           
     * 
     * @param i		U       C   f b N X(i    )
     * @param j		V       C   f b N X(j    )
     * @return		     _   X     
     */
    public double xAt(int i, int j) {
        if (isRational != true) return controlPointsArray[i][j][0]; else return controlPointsArray[i][j][0] / controlPointsArray[i][j][2];
    }

    /**
     * (i, j)           _   Y           
     * 
     * @param i		U       C   f b N X(i    )
     * @param j		V       C   f b N X(j    )
     * @return		     _   Y     
     * @return		     _
     */
    public double yAt(int i, int j) {
        if (isRational != true) return controlPointsArray[i][j][1]; else return controlPointsArray[i][j][1] / controlPointsArray[i][j][2];
    }

    /**
     * (i, j)       d        
     *                    AInvalidArgumentValue        (?)
     * 
     * @param i		U       C   f b N X(i    )
     * @param j		V       C   f b N X(j    )
     * @return		 d  
     */
    public double weightAt(int i, int j) {
        if (isRational != true) throw new ExceptionGeometryInvalidArgumentValue(); else return controlPointsArray[i][j][2];
    }

    /**
     * U           _          
     * 
     * @return		U           _    
     */
    public int uNControlPoints() {
        return controlPointsArray.length;
    }

    /**
     * V           _          
     * 
     * @return		V           _    
     */
    public int vNControlPoints() {
        return controlPointsArray[0].length;
    }

    /**
     *      _          
     * 
     * @return		     _    
     */
    public int nControlPoints() {
        return uNControlPoints() * vNControlPoints();
    }

    /**
     *  L   `              
     *
     * @return		 L   `      true
     */
    public boolean isRational() {
        return isRational;
    }

    /**
     *        `              
     *
     * @return		       `      true
     */
    public boolean isPolynomial() {
        return !isRational;
    }

    /**
     *  L           _     v Z    
     *
     * @param d0	         _
     */
    protected void convRational0Deriv(double[] d0) {
        for (int i = 0; i < 2; i++) d0[i] /= d0[2];
    }

    /**
     *  L                         v Z    
     *
     * @param d0D	         _
     * @param du	U                  
     * @param dv	V                  
     */
    protected void convRational1Deriv(double[] d0, double[] du, double[] dv) {
        convRational0Deriv(d0);
        for (int i = 0; i < 2; i++) {
            du[i] = (du[i] - (du[2] * d0[i])) / d0[2];
            dv[i] = (dv[i] - (dv[2] * d0[i])) / d0[2];
        }
    }

    /**
     *  L                     A                     v Z    
     *
     * @param d0D	         _
     * @param du	U                  
     * @param dv	V                  
     * @param duv	U                  
     * @param duv	UV                      
     * @param dvv	V                  
     */
    protected void convRational2Deriv(double[] d0, double[] du, double[] dv, double[] duu, double[] duv, double[] dvv) {
        convRational1Deriv(d0, du, dv);
        for (int i = 0; i < 2; i++) {
            duu[i] = (duu[i] - (duu[2] * d0[i]) - (2.0 * du[2] * du[i])) / d0[2];
            duv[i] = (duv[i] - (duv[2] * d0[i]) - (du[2] * dv[i]) - (dv[2] * du[i])) / d0[2];
            dvv[i] = (dvv[i] - (dvv[2] * d0[i]) - (2.0 * dv[2] * dv[i])) / d0[2];
        }
    }

    /**
     *                            `        
     *
     * @return		                           `
     * @see		EnclosingBox2D
     */
    EnclosingBox2D approximateEnclosingBox() {
        double min_crd_x;
        double min_crd_y;
        double max_crd_x;
        double max_crd_y;
        int uN = uNControlPoints();
        int vN = vNControlPoints();
        double x, y;
        int i, j;
        max_crd_x = min_crd_x = xAt(0, 0);
        max_crd_y = min_crd_y = yAt(0, 0);
        for (i = 1; i < uN; i++) {
            x = xAt(i, 0);
            y = yAt(i, 0);
            if (x < min_crd_x) min_crd_x = x; else if (x > max_crd_x) max_crd_x = x;
            if (y < min_crd_y) min_crd_y = y; else if (y > max_crd_y) max_crd_y = y;
        }
        for (j = 1; j < vN; j++) {
            for (i = 0; i < uN; i++) {
                x = xAt(i, j);
                y = yAt(i, j);
                if (x < min_crd_x) min_crd_x = x; else if (x > max_crd_x) max_crd_x = x;
                if (y < min_crd_y) min_crd_y = y; else if (y > max_crd_y) max_crd_y = y;
            }
        }
        return new EnclosingBox2D(min_crd_x, min_crd_y, max_crd_x, max_crd_y);
    }

    /**
     *    R `              
     */
    public boolean isFreeform() {
        return true;
    }
}
