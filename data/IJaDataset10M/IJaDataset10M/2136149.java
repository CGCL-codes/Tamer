package joelib2.math.symmetry;

/**
 * TransformationAtom.
 *
 * @.author     Serguei Patchkovskii
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.7 $, $Date: 2005/02/17 16:48:36 $
 */
public class TransformationAtom {

    private static final int UNDEFINED = 0;

    private static final int ROTATE_REFLECT_ATOM = 1;

    private static final int MIRROR_ATOM = 2;

    private static final int INVERT_ATOM = 3;

    private static final int ROTATE_ATOM = 4;

    public int method = UNDEFINED;

    private String methodName;

    /**
     *  Constructor for the TransformationAtom object
     *
     */
    public TransformationAtom(String _methodName) throws SymmetryException {
        methodName = _methodName;
        if (!existsMethod()) {
            throw new SymmetryException("Method " + methodName + " does not exist in class " + Symmetry.class.getName());
        }
    }

    public void callTransformation(Symmetry invoker, SymmetryElement _el, SymAtom _from, SymAtom _to) throws SymmetryException {
        switch(method) {
            case ROTATE_REFLECT_ATOM:
                invoker.rotateReflectAtom(_el, _from, _to);
                break;
            case MIRROR_ATOM:
                invoker.mirrorAtom(_el, _from, _to);
                break;
            case INVERT_ATOM:
                invoker.invertAtom(_el, _from, _to);
                break;
            case ROTATE_ATOM:
                invoker.rotateAtom(_el, _from, _to);
                break;
        }
    }

    public boolean equals(Object obj) {
        if ((obj instanceof TransformationAtom) && (obj != null)) {
            return ((TransformationAtom) obj).method == method;
        }
        return false;
    }

    public int hashCode() {
        return this.method;
    }

    private boolean existsMethod() {
        if (methodName.equals("rotateReflectAtom")) {
            method = ROTATE_REFLECT_ATOM;
            return true;
        } else if (methodName.equals("mirrorAtom")) {
            method = MIRROR_ATOM;
            return true;
        } else if (methodName.equals("invertAtom")) {
            method = INVERT_ATOM;
            return true;
        } else if (methodName.equals("rotateAtom")) {
            method = ROTATE_ATOM;
            return true;
        }
        return false;
    }
}
