package structures.trees;

/**
 * Node of a Binary Tree
 */
public class TreeNode {

    private TreeNode parent;

    private TreeNode left;

    private TreeNode right;

    private ITreeValue value;

    private int level;

    /**
     * Constructor for a root tree node
     * @param Value to store in the node
     */
    public TreeNode(ITreeValue value) {
        this.parent = null;
        this.left = null;
        this.right = null;
        this.value = value;
        this.level = 0;
    }

    /**
     * Constructor for a tree node
     * @param value Value to store in the tree node
     * @param p Parent tree node
     */
    public TreeNode(ITreeValue value, TreeNode p) {
        this.parent = p;
        this.left = null;
        this.right = null;
        this.value = value;
        this.level = this.parent.getLevel() + 1;
    }

    /**
     * Sets the parent node
     * @param parent Parent node
     */
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    /**
     * Sets the node on the right
     * @param right Node on the right
     */
    public void setRight(TreeNode right) {
        this.right = right;
    }

    /**
     * Sets the node on the left
     * @param left Node on the left
     */
    public void setLeft(TreeNode left) {
        this.left = left;
    }

    /**
     * Sets value to be stored in the tree node
     * @param value Value to be stored in the tree node
     */
    public void setValue(ITreeValue value) {
        this.value = value;
    }

    /**
     * Returns the value stored in the node
     * @return The value stored in the node
     */
    public ITreeValue getValue() {
        return this.value;
    }

    /**
     * Returns the left node
     * @return The left node
     */
    public TreeNode getLeft() {
        return this.left;
    }

    /**
     * Returns the parent node
     * @return The parent node
     */
    public TreeNode getParent() {
        return this.parent;
    }

    /**
     * Returns the right node
     * @return The right node
     */
    public TreeNode getRight() {
        return this.right;
    }

    /**
     * Returns the current level the tree node is at
     * @return Current tree node level
     */
    public int getLevel() {
        return this.level;
    }
}
