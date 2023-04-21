package org.destecs.script.ast.node;

/**
 * {@code Token} is the superclass of all tokens is the AST.
 */
@SuppressWarnings("nls")
public abstract class Token extends Node implements IToken {

    private static final long serialVersionUID = 1L;

    private String text;

    private int line;

    private int pos;

    /**
	 * Returns the {@link NodeEnum} corresponding to the
	 * type of this {@link Node} node.
	 * @return the {@link NodeEnum} for this node
	 */
    @Override
    public NodeEnum kindNode() {
        return NodeEnum.TOKEN;
    }

    /**
	 * Returns the text from the input file from which this token was made.
	 * @return the text from the input file from which this token was made
	 */
    public String getText() {
        return this.text;
    }

    /**
	 * Sets the text of this token.
	 * @param text the new text of this token
	 */
    public void setText(String text) {
        this.text = text;
    }

    /**
	 * Returns the line number information of this token.
	 * @return the line number information of this token
	 */
    public int getLine() {
        return this.line;
    }

    /**
	 * Sets the line number information of this token.
	 * @param line the new line number information of this token
	 */
    public void setLine(int line) {
        this.line = line;
    }

    /**
	 * Returns the position information of this token.
	 * @return the position information of this token
	 */
    public int getPos() {
        return this.pos;
    }

    /**
	 * Sets the position information of this token.
	 * @param pos the new position information of this token
	 */
    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
	 * Implements the {@link INode#removeChild(INode)} method. Since tokens have no
	 * children, it always throws a {@link RuntimeException}.
	 * @param child the child node to be removed from this {@link Token} node
	 * @throws RuntimeException if {@code child} is not a child of this {@link Token} node
	 */
    @Override
    public void removeChild(INode child) {
        throw new RuntimeException("Not a child.");
    }
}
