package org.gcreator.pineapple.pinec.parser.tree;

import org.gcreator.pineapple.pinec.lexer.Token;

/**
 *
 * @author luis
 */
public class SuperReference extends Reference {

    public SuperReference(Token token) {
        line = token.getLine();
        col = token.getCol();
    }

    @Override
    public boolean hasSideEffects() {
        return false;
    }
}
