package net.sf.epfe.core.exceptions;

/**
 * 
 * @since 1.1.0
 * @author Christoph
 */
public class EPFELexerInitException extends EPFEParseException {

    /**
	 * 
	 */
    public EPFELexerInitException() {
    }

    /**
	 * @param aMessage
	 */
    public EPFELexerInitException(String aMessage) {
        super(aMessage);
    }

    /**
	 * @param aCause
	 */
    public EPFELexerInitException(Throwable aCause) {
        super(aCause);
    }

    /**
	 * @param aMessage
	 * @param aCause
	 */
    public EPFELexerInitException(String aMessage, Throwable aCause) {
        super(aMessage, aCause);
    }
}
