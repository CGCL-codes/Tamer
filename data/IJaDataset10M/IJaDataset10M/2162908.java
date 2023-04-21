package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import jopt.csp.spi.arcalgorithm.graph.arc.SetArc;
import jopt.csp.spi.arcalgorithm.graph.node.Node;

/**
 * Abstract base arc for generic set arcs
 */
public abstract class GenericSetArc extends GenericArc implements SetArc {

    /**
     * Constructor
     *
     * @param   sources     Source nodes in equation
     * @param   targets     Target nodes in equation
     */
    public GenericSetArc(Node[] sources, Node[] targets) {
        super(sources, targets);
    }

    /**
     * Returns a value representing the complexity of the arc
     */
    public int getComplexity() {
        return 2;
    }
}
