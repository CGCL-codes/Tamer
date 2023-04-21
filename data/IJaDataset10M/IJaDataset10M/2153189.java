package IC.Parser;

public class NonTerminatedQuoteError extends LexicalError {

    private static final long serialVersionUID = 2505412187113465043L;

    public NonTerminatedQuoteError(String message, int line, int startColumn, Object value) {
        super(message, line, startColumn, value);
    }
}
