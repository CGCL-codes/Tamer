package jogamp.opengl.glu.nurbs;

/**
 * Class rendering curves with OpenGL
 * @author Tomáš Hráský
 *
 */
public interface CurveEvaluator {

    /**
   * Pushes eval bit
   */
    public void bgnmap1f();

    /**
   * Pops all OpenGL attributes
   */
    public void endmap1f();

    /**
   * Initializes opengl evaluator
   * @param type curve type
   * @param ulo lowest u
   * @param uhi highest u
   * @param stride control point coords
   * @param order curve order
   * @param ps control points
   */
    public void map1f(int type, float ulo, float uhi, int stride, int order, CArrayOfFloats ps);

    /**
   * Calls opengl enable
   * @param type what to enable
   */
    public void enable(int type);

    /**
   * Calls glMapGrid1f
   * @param nu steps
   * @param u1 low u
   * @param u2 high u
   */
    public void mapgrid1f(int nu, float u1, float u2);

    /**
   * Evaluates a curve using glEvalMesh1f
   * @param style Backend.N_MESHFILL/N_MESHLINE/N_MESHPOINT
   * @param from lowest param
   * @param to highest param
   */
    public void mapmesh1f(int style, int from, int to);
}
