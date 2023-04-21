package ru.amse.baltijsky.javascheme.tree;

import ru.amse.baltijsky.javascheme.nodeshape.ActionNodeShape;
import ru.amse.baltijsky.javascheme.nodeshape.INodeShape;

/**
 * Represents a node of the internal tree holding the generated java scheme that contains several nested constructions
 * but is limited to zero or one next node in the program execution flow. Similar to <code>the NestingNode</code>,
 * a node of such type contains a link to only the first child (in the terms of program execution flow) in
 * each of the nested constructions.
 *
 * @see ru.amse.baltijsky.javascheme.tree.NestingNode for more information about child links (a single-child case).
 */
public class TryNode extends NestingNode {

    private boolean hasCatchClauses = true;

    private boolean hasFinally = false;

    private FinallyNode finallyNode;

    /**
     * Default constructor. Creates a node without previous or parent (nesting) node.
     */
    public TryNode() {
        super();
    }

    /**
     * Constructor. Initializes the nesting link and sets <code>.next</code> link in the previous node.
     *
     * @param parentNode the node that nests our node
     * @param prevNode   the node for which to set the <code>.next</code> link
     */
    public TryNode(SchemaNode parentNode, SchemaNode prevNode) {
        super(parentNode, prevNode);
    }

    /**
     * Constructs a new node without previous or nesting node and with information about the corresponding code.
     *
     * @param s code associated with the node
     */
    public TryNode(String[] s) {
        super(s);
    }

    /**
     * Constructs a new node with full information about the nesting and previous nodes and the corresponding code.
     *
     * @param parentNode link to the parent (nesting) node
     * @param prevNode   link to the node that is previous in the sense of program flow
     * @param s          code associated with the node
     */
    public TryNode(SchemaNode parentNode, SchemaNode prevNode, String[] s) {
        super(parentNode, prevNode, s);
    }

    public INodeShape getShape() {
        return new ActionNodeShape(getCodeAsString());
    }

    @Override
    public INodeShape getClosingShape() {
        String msg = hasCatchClauses ? "catch" : "finally";
        return new ActionNodeShape(msg);
    }

    public NodeType getNodeType() {
        return NodeType.TRY;
    }

    public boolean isHasCatchClauses() {
        return hasCatchClauses;
    }

    public void setHasCatchClauses(boolean hasCatchClauses) {
        this.hasCatchClauses = hasCatchClauses;
    }

    public boolean isHasFinally() {
        return hasFinally;
    }

    public void setHasFinally(boolean hasFinally) {
        this.hasFinally = hasFinally;
    }

    public FinallyNode getFinallyNode() {
        return finallyNode;
    }

    public void setFinallyNode(FinallyNode finallyNode) {
        this.finallyNode = finallyNode;
    }

    @Override
    public <T, C, E extends Throwable> T accept(TreeVisitor<T, C, E> visitor, C context) throws E {
        return visitor.visit(this, context);
    }
}
