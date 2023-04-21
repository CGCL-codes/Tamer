package calclipse.lib.math.rpn;

/**
 * A lexical analyzer used by an RPNParser.
 * @author T. Sommerland
 */
public interface RPNScanner {

    /**
     * Resets the scanner.
     * @param expression the expression to scan
     */
    void reset(String expression) throws RPNException;

    /**
     * Called prior to {@link #next()}
     * to see if there are more tokens to parse.
     * @return true if there are more tokens.
     */
    boolean hasNext() throws RPNException;

    Fragment next() throws RPNException;
}
