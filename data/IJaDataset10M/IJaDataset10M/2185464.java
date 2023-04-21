package geovista.touchgraph.graphelements;

import geovista.touchgraph.Node;

/**
 * TGForEachNodePair: A dummy object for iterating through pairs of nodes
 * 
 * @author Alexander Shapiro
 * 
 */
public abstract class TGForEachNodePair {

    public void beforeInnerLoop(Node n1) {
    }

    public void afterInnerLoop(Node n1) {
    }

    public abstract void forEachNodePair(Node n1, Node n2);
}
