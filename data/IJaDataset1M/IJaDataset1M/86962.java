package de.tud.eclipse.plugins.controlflow.parsing;

/**
 * An exception that is thrown if the parser has encountered an error in the document.
 * Recommended for using and subclassing inside parser implementations.
 * If the exception is thrown on a specific line, use <b>DocumentLineParserException</b> instead.
 * 
 * @author Leo Nobach
 * 
 */
public class DocumentParserException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7100902862668579657L;

    public DocumentParserException(String message) {
        super(message);
    }
}
