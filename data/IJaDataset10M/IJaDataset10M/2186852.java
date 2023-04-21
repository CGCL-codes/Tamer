package jp.go.ipa.jgcl;

/**
 *  R     :              \   N   X B
 * <p>
 *      N   X   C   X ^   X  
 *  P             @   x N g   normal
 *        B
 * </p>
 *
 * @version $Revision: 1.9 $, $Date: 2000/08/11 06:18:47 $
 * @author Information-technology Promotion Agency, Japan
 */
public class CurveCurvature3D extends CurveCurvature {

    /**
     *  P             @   x N g   B
     */
    private final Vec3D normal;

    /**
     *          @   x N g     ^     I u W F N g   \ z     B
     * <p>
     * normal        R   X g   N ^         P                     B
     * </p>
     * <p>
     *      Anormal    null               
     * ExceptionGeometryNullArgument      O           B
     * <p>
     * 
     * @param curvature	    
     * @param normal	   @   x N g  
     * @see	ExceptionGeometryNullArgument
     */
    CurveCurvature3D(double curvature, Vec3D normal) {
        super(curvature);
        if (normal == null) throw new ExceptionGeometryNullArgument();
        this.normal = normal.normalized();
    }

    /**
     *  P             @   x N g         B
     * 
     * @return		 P           @   x N g  
     */
    public Vec3D normal() {
        return normal;
    }

    /**
     *          I u W F N g   A ^             I u W F N g                             B
     * <p>
     *                      Z           A
     *            I u W F N g     @   x N g         p x   p x     e               A
     *            I u W F N g         l   t                 e                 A
     *            I u W F N g                       B
     * </p>
     *
     * @param mate	               I u W F N g
     * @return		this    mate                          true A               false
     */
    public boolean identical(CurveCurvature3D mate) {
        if (!this.normal().identicalDirection(mate.normal())) return false;
        return CurveCurvature.identical(this.curvature(), mate.curvature());
    }
}
