package edu.mit.lcs.haystack.adenine.interpreter;

import edu.mit.lcs.haystack.adenine.SyntaxException;

/**
 * @version 	1.0
 * @author		Dennis Quan
 */
public class UnboundIdentifierException extends SyntaxException {

    /**
	 * Constructor for UnboundIdentifierException.
	 * @param s
	 */
    public UnboundIdentifierException(String s) {
        super("Unbound identifier: " + s);
    }
}
