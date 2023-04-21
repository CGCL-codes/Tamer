package jolie.lang.parse;

import jolie.lang.parse.context.ParsingContext;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import jolie.lang.parse.context.URIParsingContext;

/** Skeleton implementation of a parser based on {@link jolie.lang.parse.Scanner}.
 * Note that the parsing process is not re-entrant.
 * @author Fabrizio Montesi
 * @see Scanner
 */
public abstract class AbstractParser {

    private Scanner scanner;

    protected Scanner.Token token;

    private final List<Scanner.Token> tokens = new LinkedList<Scanner.Token>();

    /** Constructor
	 * 
	 * @param scanner The scanner to use during the parsing procedure.
	 */
    public AbstractParser(Scanner scanner) {
        this.scanner = scanner;
    }

    protected void addTokens(Collection<Scanner.Token> tokens) {
        this.tokens.addAll(tokens);
    }

    /** Gets a new token.
	 * 
	 * @throws IOException If the internal scanner raises one.
	 */
    protected void getToken() throws IOException {
        if (tokens.size() > 0) {
            token = tokens.remove(0);
        } else {
            token = scanner.getToken();
        }
    }

    /**
	 * Returns the Scanner object used by this parser.
	 * @return The Scanner used by this parser.
	 */
    public Scanner scanner() {
        return scanner;
    }

    protected void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public ParsingContext getContext() {
        return new URIParsingContext(scanner.source(), scanner.line());
    }

    /**
	 * Eats the current token, asserting its type.
	 * Calling eat( type, errorMessage ) is equivalent to call subsequently
	 * tokenAssert( type, errorMessage ) and getToken().
	 * @param type The type of the token to eat.
	 * @param errorMessage The error message to display in case of a wrong token type.
	 * @throws ParserException If the token type is wrong.
	 * @throws IOException If the internal scanner raises one.
	 */
    protected void eat(Scanner.TokenType type, String errorMessage) throws ParserException, IOException {
        assertToken(type, errorMessage);
        getToken();
    }

    protected void eatKeyword(String keyword, String errorMessage) throws ParserException, IOException {
        assertToken(Scanner.TokenType.ID, errorMessage);
        if (!token.content().equals(keyword)) {
            throwException(errorMessage);
        }
        getToken();
    }

    /**
	 * Asserts the current token type.
	 * @param type The token type to assert.
	 * @param errorMessage The error message to display in case of a wrong token type.
	 * @throws ParserException If the token type is wrong.
	 */
    protected void assertToken(Scanner.TokenType type, String errorMessage) throws ParserException {
        if (token.isNot(type)) throwException(errorMessage);
    }

    /**
	 * Shortcut to throw a correctly formed ParserException.
	 * @param mesg The message to insert in the ParserException.
	 * @throws ParserException Everytime, as its the purpose of this method.
	 */
    protected void throwException(String mesg) throws ParserException {
        String m = mesg + ". Found token type " + token.type().toString();
        if (!token.content().equals("")) {
            m += ", token content " + token.content();
        }
        throw new ParserException(scanner.sourceName(), scanner.line(), m);
    }

    /**
	 * Shortcut to throw a correctly formed ParserException, getting the message from an existing exception.
	 * @param exception The exception to get the message from.
	 * @throws ParserException Everytime, as its the purpose of this method.
	 */
    protected void throwException(Exception exception) throws ParserException {
        throw new ParserException(scanner.sourceName(), scanner.line(), exception.getMessage());
    }
}
