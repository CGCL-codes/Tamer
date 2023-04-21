package jopt.csp.spi.arcalgorithm.constraint.set;

import jopt.csp.spi.arcalgorithm.constraint.AbstractConstraint;
import jopt.csp.spi.arcalgorithm.graph.arc.Arc;
import jopt.csp.spi.arcalgorithm.graph.arc.binary.BinarySetSupersetArc;
import jopt.csp.spi.arcalgorithm.graph.arc.hyper.HyperSetIntersectionArc;
import jopt.csp.spi.arcalgorithm.graph.node.SetNode;

/**
 * Constraint representing intersection( sources ) = target
 */
public class EqIntersection<T> extends SetConstraint<T> {

    @SuppressWarnings("unchecked")
    public EqIntersection(SetVariable<T> x, SetVariable<T> y, SetVariable<T> z) {
        this(new SetVariable[] { x, y }, z);
    }

    @SuppressWarnings("unchecked")
    public EqIntersection(SetVariable<T> sources[], SetVariable<T> target) {
        super(sources, new SetVariable[] { target });
    }

    /**
     * Creates an array of arcs representing constraint
     */
    protected Arc[] createArcs() {
        SetNode<T> sourceNodes[] = getSetSourceNodes();
        SetNode<T> targetNodes[] = getSetTargetNodes();
        Arc arcs[] = new Arc[sourceNodes.length + 1];
        arcs[0] = new HyperSetIntersectionArc<T>(sourceNodes, targetNodes[0]);
        for (int i = 1; i < arcs.length; i++) arcs[i] = new BinarySetSupersetArc<T>(targetNodes[0], sourceNodes[i - 1], false);
        return arcs;
    }

    protected boolean isViolated() {
        return false;
    }

    protected AbstractConstraint createOpposite() {
        return null;
    }
}
