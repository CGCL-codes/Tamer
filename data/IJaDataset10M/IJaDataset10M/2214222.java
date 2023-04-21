package frsf.cidisi.faia.solver.search;

import java.util.Vector;

/**
 * @author Jorge M. Roa
 * @version 1.0
 * @created 08-Mar-2007 13:16:04
 */
public class DepthFirstSearch extends Strategy {

    public DepthFirstSearch() {
    }

    public void addNodesToExpand(Vector<NTree> nodes) {
        for (NTree nt : nodes) {
            nt.setCost(nt.getParent().getCost() - 1);
        }
        nodesToExpand.addAll(nodes);
    }

    public void addNodeToExpand(NTree node) {
        node.setCost(node.getParent().getCost() - 1);
        nodesToExpand.add(node);
    }

    @Override
    public String getStrategyName() {
        return "Depth First (Primero en profundidad)";
    }
}
