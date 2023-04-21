package org.equanda.tapestry5.data;

/**
 * This class is used by the TreeTable component to flatten a given Tree structure
 * so that it can be represented in an html table.
 * <p/>
 * A Tree is nested, an html table is flat and this class represent each node of the
 * tree, but without nesting. The TreeTable component can then convert a Tree structure
 * (each Tree can contain other Tree childs) into a list of FlattenedTree objects.
 * The FlattenedTree still remember the position of the node in the original tree  in order
 * to render the tree correctly in html (reference to javascript used
 * by TreeTable component for details).
 *
 * @author Geert Mergan
 */
public class FlattenedTree implements java.io.Serializable {

    private Tree tree;

    private String dotId;

    private int depth;

    public FlattenedTree(Tree tree, int depth, String dotId) {
        super();
        this.tree = tree;
        this.dotId = dotId;
        this.depth = depth;
    }

    public Tree getTree() {
        return tree;
    }

    public String getDotId() {
        return dotId;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isLeaf() {
        return tree.isLeaf();
    }
}
