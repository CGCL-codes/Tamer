package org.ucl.xpath;

/**
 * This exception is thrown when there is a problem with an XPath exception.
 */
public class XPathException extends Exception {

    private String _reason;

    /**
 	 * Constructor for XPathException
	 * @param reason Is the reason why the exception has been thrown.
 	 */
    public XPathException(String reason) {
        _reason = reason;
    }

    /**
	 * The reason why the exception has been thrown.
	 * @return the reason why the exception has been throw. 
	 */
    public String reason() {
        return _reason;
    }
}
