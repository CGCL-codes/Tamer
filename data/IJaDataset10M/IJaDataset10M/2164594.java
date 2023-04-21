package matuya.sjm.parse.tokens;

/**
 * Objects of this class represent a type of token, such
 * as "number" or "word".
 * 
 * @author Steven J. Metsker
 *
 * @version 1.0 
 */
public class TokenType {

    protected String name;

    /**
 * Creates a token type of the given name.
 */
    public TokenType(String name) {
        this.name = name;
    }
}
