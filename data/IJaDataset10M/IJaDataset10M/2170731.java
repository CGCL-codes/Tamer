package org.deft.repository.ast.annotation.linewrapping;

import org.deft.repository.ast.annotation.Ident;
import org.deft.repository.ast.annotation.NodeInformation;

/**
 * Information that can be attached to a <code>TokenNode</code> that holds
 * the virtual coordinates of the <code>Token</code> contained by that <code>TokenNode</code>
 * that are the result of the line wrapping procedure.
 * 
 * @see wrapper.util.CodeLine
 * @see exporter.text.PlainTextExporter
 * @see wrapper.LineWrapper
 * 
 * @author Christoph Seidl
 */
public class LineWrappingInformation extends NodeInformation {

    public static final Ident IDENT = new Ident("lineWrapping", "Line wrapping");

    private int line;

    private int col;

    private int endLine;

    private int endCol;

    /**
	 * Creates a new <code>LineWrappingInformation</code>.
	 * 
	 * @param line The virtual start line of the associated <code>Token</code>.
	 * @param col The virtual start column of the associated <code>Token</code>.
	 * @param endLine The virtual end line of the associated <code>Token</code>.
	 * @param endCol The virtual end column of the associated <code>Token</code>.
	 */
    public LineWrappingInformation(int line, int col, int endLine, int endCol) {
        this.line = line;
        this.col = col;
        this.endLine = endLine;
        this.endCol = endCol;
    }

    @Override
    public Ident getIdent() {
        return IDENT;
    }

    @Override
    public void addContentFromOtherNodeInformation(NodeInformation newInformation) {
    }

    @Override
    public NodeInformation copy() {
        return new LineWrappingInformation(line, col, endLine, endCol);
    }

    /**
	 * Returns the virtual start line of the associated <code>Token</code>.
	 * 
	 * @return The virtual start line of the associated <code>Token</code>.
	 */
    public int getLine() {
        return line;
    }

    /**
	 * Returns the virtual start column of the associated <code>Token</code>.
	 * 
	 * @return The virtual start column of the associated <code>Token</code>.
	 */
    public int getCol() {
        return col;
    }

    /**
	 * Returns the virtual end line of the associated <code>Token</code>.
	 * 
	 * @return The virtual end line of the associated <code>Token</code>.
	 */
    public int getEndLine() {
        return endLine;
    }

    /**
	 * Returns the virtual end column of the associated <code>Token</code>.
	 * 
	 * @return The virtual end column of the associated <code>Token</code>.
	 */
    public int getEndCol() {
        return endCol;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " (" + line + ":" + col + " - " + endLine + ":" + endCol + ")";
    }
}
