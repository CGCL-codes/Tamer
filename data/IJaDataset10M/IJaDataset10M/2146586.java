package net.sf.sasl.language.placeholder.syntax;

import java.util.ArrayList;
import java.util.List;

/**
 * Root class for all abstract syntax tree nodes of the placeholder grammar.
 * 
 * @author Philipp Förmer
 * @since 0.0.1 (sasl-common-aspect-library)
 */
public abstract class ASTNode {

    /**
	 * How often {@link #INDENT_STRING} should be repeated in front of each line
	 * per tree depth.
	 * 
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public static final int INDENT_PER_DEPTH = 4;

    /**
	 * Indent string which will be used to pretty print an abstract syntax tree.
	 * 
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public static final String INDENT_STRING = " ";

    /**
	 * The parent node of this node.
	 * 
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    private ASTNode parentNode;

    /**
	 * Creates an empty root node.
	 * 
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public ASTNode() {
    }

    /**
	 * Creates a node that has the given parent node has its parent node.
	 * 
	 * @param parentNode
	 *            null or non null
	 */
    public ASTNode(ASTNode parentNode) {
        setParentNode(parentNode);
    }

    /**
	 * Returns the parent node of this node.
	 * 
	 * @return null or non null
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public ASTNode getParentNode() {
        return parentNode;
    }

    /**
	 * Sets the parent node of this node.
	 * 
	 * @param parentNode
	 *            null or non null
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public void setParentNode(ASTNode parentNode) {
        this.parentNode = parentNode;
    }

    /**
	 * Returns true if this node is a root node. False else.
	 * 
	 * @return true or false
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public boolean isRootNode() {
        return (parentNode == null);
    }

    /**
	 * Returns true if this node is a leaf node. False else.
	 * 
	 * @return true or false.
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public boolean isLeafNode() {
        return true;
    }

    /**
	 * Returns a list of all child nodes of this node.
	 * 
	 * @return non null list.
	 * @since 0.0.1 (sasl-common-aspect-library)
	 */
    public List<ASTNode> getChildNodes() {
        return new ArrayList<ASTNode>();
    }

    /**
	 * Returns a human readable string representation of the tree spanned of by
	 * this node.
	 * 
	 * @param indent
	 *            how often {@link #INDENT_STRING} should be printed in front of
	 *            each new line at this tree depth.
	 * @return non null string
	 * @throws IllegalArgumentException
	 *             if parameter indent is lesser than 0.
	 */
    public abstract String prettyPrint(int indent) throws IllegalArgumentException;

    /**
	 * Returns the same output as calling {@link #prettyPrint(int)} with
	 * indent=0.
	 * 
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return prettyPrint(0);
    }
}
