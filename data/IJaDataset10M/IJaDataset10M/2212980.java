package jogamp.opengl.glu.nurbs;

/**
 * NURBS curve object
 * @author Tomáš Hráský
 *
 */
public class O_nurbscurve {

    /**
   * List of bezier curves
   */
    public Quilt bezier_curves;

    /**
   * Curve type
   */
    public int type;

    /**
   * Was curve used ?
   */
    public boolean used;

    /**
   * Parent curve
   */
    public O_curve owner;

    /**
   * Next curve in list
   */
    public O_nurbscurve next;

    /**
   * Makes new O_nurbscurve
   * @param realType type of curve 
   */
    public O_nurbscurve(int realType) {
        this.type = realType;
        this.owner = null;
        this.next = null;
        this.used = false;
    }
}
