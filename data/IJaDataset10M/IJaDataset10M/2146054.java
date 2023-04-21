package de.parsemis.algorithms.gaston;

import de.parsemis.graph.HPGraph;

/**
 * This class is for distinguish a node Refinement from a cycle Refinement.
 * 
 * @author Marc Woerlein (woerlein@informatik.uni-erlangen.de)
 */
public class CycleRefinement extends Refinement {

    /**
	 * creates a new CycleRefinement
	 * 
	 * @param nodeA
	 *            node this refinement connects
	 * @param edgeLabel
	 *            the label of the new edge
	 * @param nodeB
	 *            node this refinement connects
	 */
    public CycleRefinement(final int nodeA, final int edgeLabel, final int nodeB) {
        super(nodeA, edgeLabel, nodeB);
    }

    @Override
    public Refinement clone(final int nodeB) {
        return new CycleRefinement(this.nodeA, this.edgeLabel, this.nodeB);
    }

    @Override
    public int compareTo(final Refinement o) {
        if (!o.isCycleRefinement()) {
            return 1;
        }
        if (o.nodeA != nodeA) {
            return o.nodeA - nodeA;
        }
        if (o.nodeB != nodeB) {
            return o.nodeB - nodeB;
        }
        return o.edgeLabel - edgeLabel;
    }

    @Override
    public int getNodeB() {
        return nodeB;
    }

    @Override
    public int getToLabel() {
        return HPGraph.NO_NODE;
    }

    @Override
    public boolean isCycleRefinement() {
        return true;
    }
}
