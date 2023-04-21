package org.dllearner.gui;

import java.util.Comparator;
import org.dllearner.algorithms.SearchTreeNode;

/**
 * Takes a comparator for a specific search tree node type and generalises
 * it to a comparator for all search tree nodes.
 * 
 * @author Jens Lehmann
 *
 */
public class SearchTreeNodeCmpWrapper implements Comparator<SearchTreeNode> {

    private Comparator<SearchTreeNode> cmp;

    @SuppressWarnings("unchecked")
    public SearchTreeNodeCmpWrapper(Comparator<? extends SearchTreeNode> cmp) {
        this.cmp = (Comparator<SearchTreeNode>) cmp;
    }

    @Override
    public int compare(SearchTreeNode o1, SearchTreeNode o2) {
        return cmp.compare(o1, o2);
    }
}
