package astgen;

/**
 * Represents an abstract grammar token.
 * 
 * @author rgrig 
 */
public class AgToken implements Token {

    private String rep;

    /** The tokens in an AG. */
    public enum Type {

        ENUM, EQ, COLON, SEMICOLON, SUPERTYPE, BANG, LP, RP, LB, RB, COMMA, ID, WS, NL, COMMENT, ERROR
    }

    /** The type of token. */
    public Type type;

    public AgToken(Type type, String rep) {
        this.rep = rep;
        this.type = type;
    }

    @Override
    public String rep() {
        return rep;
    }

    /** Is this syntactically meaningful? */
    public boolean isGood() {
        return type != Type.COMMENT && type != Type.WS && type != Type.NL;
    }
}
