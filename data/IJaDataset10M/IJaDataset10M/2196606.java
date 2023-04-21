package binf.ai.search.uninformed;

import binf.ai.search.framework.TreeSearch;
import binf.ai.search.nodestore.NodeStore;
import binf.ai.search.nodestore.Stack;
import binf.ai.search.problem.Problem;

/**
 * implementeert een diepte eerst tree zoek algoritme met limiet
 */
public class DepthFirstTreeSearchWithLimit extends TreeSearch {

    private static NodeStore getOpenList() {
        return new Stack();
    }

    /**
     * creeert een instatie
     * @param problem het Problem object
     * @param limit de diepte limiet
     */
    public DepthFirstTreeSearchWithLimit(Problem problem, int limit) {
        super(problem, getOpenList(), limit);
    }
}
