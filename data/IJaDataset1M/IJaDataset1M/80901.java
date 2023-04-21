package gesser.gals.ebnf.parser.tokens;

/**
 * @author Carlos Gesser
 */
public class PipeToken extends PunctuationToken {

    public PipeToken(int position) {
        super(TokenId.PIPE, position);
    }

    public String getLexeme() {
        return "|";
    }
}
