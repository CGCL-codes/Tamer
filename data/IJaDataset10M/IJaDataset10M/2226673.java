package raja.io;

/**
 * The NoMatchingTokenException is thrown when no token matches the input.
 *
 * @see Lexer
 * @see ObjectReader
 *
 * @author Emmanuel Fleury
 * @author Gr�goire Sutre
 */
class NoMatchingTokenException extends ReadException {

    NoMatchingTokenException(String s) {
        super(s);
    }
}
