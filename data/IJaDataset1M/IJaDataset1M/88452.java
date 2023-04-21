package com.bluemarsh.jswat.core.expr;

import com.bluemarsh.jswat.parser.node.Token;

/**
 * Class LeftParen is a placeholder on the operator stack during parsing.
 *
 * @author  Nathan Fiedler
 */
class LeftParen extends OperatorNode {

    /**
     * Constructs a OperatorNode associated with the given token.
     *
     * @param  node  lexical token.
     */
    LeftParen(Token node) {
        super(node);
    }

    @Override
    public boolean isSentinel() {
        return true;
    }

    @Override
    public int precedence() {
        return 1;
    }
}
