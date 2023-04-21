package com.sun.opengl.impl.glu.nurbs;

/**
 * Class for arc tesselation
 * @author Tomas Hrasky
 *
 */
public class ArcTesselator {

    /**
   * Makes given arc an bezier arc
   * @param arc arc to work with
   * @param s1 minimum s param
   * @param s2 maximum s param
   * @param t1 minimum t param
   * @param t2 maximum s param
   */
    public void bezier(Arc arc, float s1, float s2, float t1, float t2) {
        TrimVertex[] p = new TrimVertex[2];
        p[0] = new TrimVertex();
        p[1] = new TrimVertex();
        arc.pwlArc = new PwlArc(2, p);
        p[0].param[0] = s1;
        p[0].param[1] = s2;
        p[1].param[0] = t1;
        p[1].param[1] = t2;
        arc.setbezier();
    }

    /**
   * Empty method
   * @param newright arc to work with
   * @param s first tail
   * @param t2 second tail
   * @param t1 third tail
   * @param f stepsize
   */
    public void pwl_right(Arc newright, float s, float t1, float t2, float f) {
    }

    /**
   * Empty method
   * @param newright arc to work with
   * @param s first tail
   * @param t2 second tail
   * @param t1 third tail
   * @param f stepsize
   */
    public void pwl_left(Arc newright, float s, float t2, float t1, float f) {
    }
}
