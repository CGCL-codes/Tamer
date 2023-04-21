package net.sf.jtreemap.ktreemap;

/**
 * Tree builder for a KTreeMap.
 *
 * @author Laurent Dutheil
 */
public abstract class TreeMapNodeBuilder {

    private TreeMapNode root;

    /**
   * Add a branch to the tree. <BR>
   * If the parent is null, the build node become the root if and only if the
   * tree have no root yet. If the parent is null and if the root is already
   * build, the node will NOT be added to the tree.
   *
   * @param value Value of the branch
   * @param parent father of the branch
   * @return the created node
   */
    public TreeMapNode buildBranch(Object value, TreeMapNode parent) {
        TreeMapNode node = new TreeMapNode(value);
        if (parent != null) {
            parent.add(node);
        } else if (this.root == null) {
            this.root = node;
        }
        return node;
    }

    /**
   * add a leaf to the tree. <BR>
   * If the parent is null, the build node become the root if and only if the
   * tree have no root yet. If the parent is null and if the root is already
   * build, the node will NOT be added to the tree.
   *
   * @param value Value of the leaf
   * @param parent father of the leaf
   * @return the created node
   */
    public TreeMapNode buildLeaf(Object value, TreeMapNode parent) {
        TreeMapNode node = new TreeMapNode(value, getWeight(value));
        if (parent != null) {
            parent.add(node);
        } else if (this.root == null) {
            this.root = node;
        }
        return node;
    }

    /**
   * get the build tree.
   *
   * @return the root of the tree
   */
    public TreeMapNode getRoot() {
        return this.root;
    }

    /**
   * Return the weight of an Object. <BR>
   * This method have to be extended
   *
   * @param value the object (generaly, the TreeMapNode.getValue())
   * @return the weight of the value
   */
    public abstract double getWeight(Object value);
}
