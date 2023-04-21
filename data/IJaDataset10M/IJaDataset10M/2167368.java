package javamath.util.math;

import javamath.util.math.*;
import CA.sfu.cecm.openmath.lang.*;
import java.util.Vector;
import java.util.Enumeration;

/**
 * This is the ancestor of all Java mathematical objects that can be
 * converted to OpenMath objects using the PolyMath library.
 *
 * @author Craig A. Struble and Andrew Solomon
 * @version $Revision: 1.1 $ $Date: 2000/10/29 05:53:09 $
 */
public class MathObject implements MathInterface {

    public OMObject toOMObject() {
        return null;
    }

    public String asASCII() {
        return null;
    }

    public String asHTML() {
        return null;
    }

    public String asMathML() {
        return null;
    }

    public String asOpenMath() {
        return null;
    }

    public String asLaTeX() {
        return null;
    }
}
