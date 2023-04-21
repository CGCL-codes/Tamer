package riverbed.jelan.parser.softparser;

import riverbed.jelan.lexer.Token;

/**
 * An interface to receive tokens.
 */
public interface TokenVisitor {

    /**
     * Invoked to indicate the presence of a token.
     * 
     * @param token a Token that is present
     */
    void visit(Token token);
}
