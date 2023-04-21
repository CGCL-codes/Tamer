package deesel.parser;

/**
 * This interface exists for the benefit of JJTree the JavaCC tool used for the
 * primary G grammar and parser generation.
 */
public interface Node extends ASTNode {

    /**
     * This member is called after the node has been made the current node.  It
     * indicates that child nodes can now be added to it.
     */
    void jjtOpen();

    /**
     * This member is called after all the child nodes have been added.
     */
    void jjtClose();

    /**
     * This method is used to inform the node of its parent.
     *
     * @param parent the parent node.
     */
    void jjtSetParent(ASTNode parent);

    /**
     * Returns the parent of this node (identical to getParent()).
     *
     * @return the parent node or null.
     */
    ASTNode jjtGetParent();

    /**
     * This member tells the node to add its argument to the node's list of
     * children.
     *
     * @param node the node to add.
     * @param n    the position to add it at.
     */
    void jjtAddChild(ASTNode node, int n);

    /**
     * This member returns a child node.  The children are numbered from zero,
     * left to right.
     *
     * @param n the position to look at for the child.
     * @return the child node at the nth position.
     */
    ASTNode jjtGetChild(int n);
}
