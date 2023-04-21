package org.gzigzag.flowing;

import org.gzigzag.*;

/** An Flowing Clang primitive.
 */
public abstract class Primitive {

    String rcsid = "$Id: Primitive.java,v 1.3 2000/10/13 13:52:24 bfallenstein Exp $";

    protected Data execute(Data params) {
        return null;
    }

    public Data execute(Data params, ZZSpace space) {
        return execute(params);
    }

    public static void count(Data pars, int n) {
        if (n != pars.len()) miscount();
    }

    public static void miscount() {
        throw new ZZError("Wrong parameter count");
    }

    /** Read a direction or step count from a parameter array.
     * If the pos is out of the bounds of the array, +1 is assumed, so that
     * the dir can optionally be left out.
     * <p>
     * (This, obviously, is just a convenience function.)
     */
    static int readsteps(Data pars, int pos) {
        if (pos < pars.len()) {
            String stepstr = pars.s(pos);
            if (stepstr.equals("+")) return 1; else if (stepstr.equals("-")) return -1; else return pars.i(pos);
        } else {
            return 1;
        }
    }
}
