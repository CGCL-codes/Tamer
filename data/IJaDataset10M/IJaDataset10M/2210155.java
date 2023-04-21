package com.rapidminer.operator.learner.stringkernel.tree;

import java.util.Comparator;

/**
 * This class sorts the tree by sorting the production-rules.
 * 
 * @author Martin Had
 * @version $Id
 *
 */
public class TreeSortByProduction implements Comparator<Tree> {

    private int depth;

    public TreeSortByProduction() {
        super();
        this.depth = 1;
    }

    public TreeSortByProduction(int depth) {
        super();
        this.depth = depth;
    }

    @Override
    public int compare(Tree tree1, Tree tree2) {
        return tree1.getProduction(depth).compareTo(tree2.getProduction(depth));
    }
}
