package prajna.viz.prefuse;

import prajna.viz.display.TreeDisplay;

/**
 * Extension of the PrefuseGraphDisplay which adds support for Trees. The
 * Prefuse Tree type is an extension of the Prefuse Graph type, therefore a
 * Tree display can benefit from much of the graph dispaly algorithms.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 * @param <N> The node class for the trees and graphs
 * @param <E> The edge class for graph edges
 */
public abstract class PrefuseTreeDisplay<N, E> extends PrefuseGraphDisplay<N, E> implements TreeDisplay<N> {

    private static final long serialVersionUID = -7621022957066102321L;

    private prajna.util.Tree<N> tree;

    private prefuse.data.Tree preTree;

    /**
     * Get the Prefuse Tree
     * 
     * @return the prefuse Tree
     */
    protected prefuse.data.Tree getPrefuseTree() {
        return preTree;
    }

    /**
     * Get the tree displayed by this display
     * 
     * @return the tree currently displayed
     */
    public prajna.util.Tree<N> getTree() {
        return tree;
    }

    /**
     * Set the tree displayed by this display
     * 
     * @param newTree the tree to display
     */
    public void setTree(prajna.util.Tree<N> newTree) {
        tree = newTree;
        preTree = getConverter().convertTree(tree);
        displayGraph(preTree);
    }

    /**
     * update the tree currently displayed, showing new or expanded nodes or
     * collapsing nodes appropriately
     */
    public void updateTree() {
    }
}
