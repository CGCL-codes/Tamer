package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.NumArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericNumConstant;
import jopt.csp.spi.util.MutableNumber;
import jopt.csp.spi.util.NumberMath;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Zi = Xi * Yi, Zi < Xj * Yi, Zi > Xj * Yk, etc.
 */
public class GenericNumProdArc extends GenericNumArc implements NumArc {

    private MutableNumber min = new MutableNumber();

    private MutableNumber max = new MutableNumber();

    private MutableNumber v1 = new MutableNumber();

    private MutableNumber v2 = new MutableNumber();

    private MutableNumber v3 = new MutableNumber();

    private MutableNumber v4 = new MutableNumber();

    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumProdArc(Node x, Node y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }

    /**
     * Constructor
     *
     * @param   x           X constant in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumProdArc(Number x, Node y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }

    /**
     * Constructor
     *
     * @param   x           X constant in equation
     * @param   y           Y variable in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumProdArc(GenericNumConstant x, Node y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }

    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   y           Y constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumProdArc(Node x, Number y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }

    /**
     * Constructor
     *
     * @param   x           X variable in equation
     * @param   y           Y constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumProdArc(Node x, GenericNumConstant y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }

    /**
     * Constructor
     *
     * @param   x           X constant in equation
     * @param   y           Y constant in equation
     * @param   z           Z variable in equation
     * @param   nodeType    Node variable type (Integer, Long, Float, Decimal)
     * @param   arcType     Arc relation type (Eq, Lt, Gt, etc.)
     */
    public GenericNumProdArc(Number x, Number y, Node z, int nodeType, int arcType) {
        super(x, y, z, nodeType, arcType);
    }

    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        switch(arcType) {
            case GEQ:
                NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                NumberMath.min(v1, v2, v3, v4, min);
                setMinZ(min);
                break;
            case GT:
                NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                NumberMath.min(v1, v2, v3, v4, min);
                NumberMath.next(min, getPrecisionZ(), min);
                setMinZ(min);
                break;
            case LEQ:
                NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                NumberMath.max(v1, v2, v3, v4, max);
                setMaxZ(max);
                break;
            case LT:
                NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                NumberMath.max(v1, v2, v3, v4, max);
                NumberMath.previous(max, getPrecisionZ(), max);
                setMaxZ(max);
                break;
            case EQ:
                NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                NumberMath.multiplyNoInvalid(getLargestMinX(), getLargestMinY(), nodeType, v2);
                NumberMath.multiplyNoInvalid(getSmallestMaxX(), getLargestMinY(), nodeType, v3);
                NumberMath.multiplyNoInvalid(getLargestMinX(), getSmallestMaxY(), nodeType, v4);
                NumberMath.min(v1, v2, v3, v4, min);
                NumberMath.max(v1, v2, v3, v4, max);
                setRangeZ(min, max);
                break;
            case NEQ:
                if (isBoundX() && isBoundY()) {
                    NumberMath.multiplyNoInvalid(getSmallestMaxX(), getSmallestMaxY(), nodeType, v1);
                    removeValueZ(v1);
                }
        }
    }
}
