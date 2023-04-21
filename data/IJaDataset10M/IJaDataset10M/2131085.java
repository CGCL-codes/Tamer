package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.variable.PropagationFailureException;

/**
 * Arc representing Z = (X == Y)
 */
public class GenericBoolEqThreeVarArc extends GenericBoolArc {

    /**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if equal to X
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if equal to Z
     */
    public GenericBoolEqThreeVarArc(Node x, boolean notX, Node y, boolean notY, Node z, boolean notZ) {
        super(x, notX, y, notY, z, notZ);
    }

    /**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if equal to X
     * @param y         y constant of equation
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if equal to Z
     */
    public GenericBoolEqThreeVarArc(Node x, boolean notX, boolean y, Node z, boolean notZ) {
        super(x, notX, new Boolean(y), z, notZ);
    }

    /**
     * Constraint
     * 
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if equal to X
     * @param y         y constant of equation
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if equal to Z
     */
    public GenericBoolEqThreeVarArc(Node x, boolean notX, GenericBooleanConstant y, Node z, boolean notZ) {
        super(x, notX, y, z, notZ);
    }

    /**
     * Constraint
     * 
     * @param x         x constant of equation
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if equal to Z
     */
    public GenericBoolEqThreeVarArc(boolean x, Node y, boolean notY, Node z, boolean notZ) {
        super(new Boolean(x), y, notY, z, notZ);
    }

    /**
     * Constraint
     * 
     * @param x         x constant of equation
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if equal to Z
     */
    public GenericBoolEqThreeVarArc(GenericBooleanConstant x, Node y, boolean notY, Node z, boolean notZ) {
        super(x, y, notY, z, notZ);
    }

    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateCurrentNodes() throws PropagationFailureException {
        if (isAnyXTrue() && isAnyYTrue()) {
            setTargetTrue();
        } else if (isAnyXFalse() && isAnyYFalse()) {
            setTargetTrue();
        } else if (isAnyXTrue() && isAnyYFalse()) {
            setTargetFalse();
        } else if (isAnyXFalse() && isAnyYTrue()) {
            setTargetFalse();
        }
    }
}
