package org.ucl.xpath;

/**
 * Error caused by JFlex
 */
public class JFlexError extends XPathError {

    /**
	 * Constructor for JFlex error.
	 * @param reason is the reason for the error.
 	 */
    public JFlexError(String reason) {
        super(reason);
    }
}
