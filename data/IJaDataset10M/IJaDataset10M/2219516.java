package de.humanfork.treemerge.tree;

/**
 * A full T tree.
 */
public class FullTTree extends FullTree<TMergeNode> {

    /**
     * The Constructor.
     *
     * @param treeType the tree type
     * @param root the root
     */
    public FullTTree(final TMergeNode root, final TreeType treeType) {
        super(root, treeType);
        if ((treeType != TreeType.T1) && (treeType != TreeType.T2)) throw new IllegalArgumentException("t trees can be of type t1 or t2 but not of type " + treeType);
    }

    /**
     * The Constructor.
     *
     * @param treeType the tree type
     */
    public FullTTree(final TreeType treeType) {
        super(treeType);
        if ((treeType != TreeType.T1) && (treeType != TreeType.T2)) throw new IllegalArgumentException("t trees can be of type t1 or t2 but not of type " + treeType);
    }
}
