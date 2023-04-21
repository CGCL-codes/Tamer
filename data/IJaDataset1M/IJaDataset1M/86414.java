package org.matheclipse.parser.client.ast;

/**
 * A node for a parsed string (i.e. delimited by double quotes)
 * 
 */
public class StringNode extends ASTNode {

    public StringNode(final String value) {
        super(value);
    }
}
