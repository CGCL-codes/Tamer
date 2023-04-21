package com.bluemarsh.jswat.expr;

import com.bluemarsh.jswat.parser.java.node.Token;

/**
 * Class LeftBracket is a placeholder on the operator stack during
 * parsing.
 *
 * @author  Nathan Fiedler
 */
class LeftBracket extends OperatorNode {

    /**
     * Constructs a OperatorNode associated with the given token.
     *
     * @param  node  lexical token.
     */
    public LeftBracket(Token node) {
        super(node);
    }

    /**
     * Returns true if this operator does not do any operation but
     * instead acts as a sentinel, delineating portions of an
     * expression. This includes (), [], and commas.
     *
     * @return  true if sentinel, false otherwise.
     */
    public boolean isSentinel() {
        return true;
    }

    /**
     * Returns this operator's precedence value. The lower the value
     * the higher the precedence. The values are equivalent to those
     * described in the Java Language Reference book (2nd ed.), p 106.
     *
     * @return  precedence value.
     */
    public int precedence() {
        return 1;
    }
}
