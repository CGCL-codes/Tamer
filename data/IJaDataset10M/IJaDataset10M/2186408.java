package org.dom4j;

/**
 * <p>
 * <code>InvalidXPathException</code> is thrown when an invalid XPath
 * expression is used to traverse an XML document
 * </p>
 * 
 * @version $Revision: 1.6.2.1 $
 */
public class InvalidXPathException extends IllegalArgumentException {

    /** The <code>serialVersionUID</code>. */
    private static final long serialVersionUID = 3257009869058881592L;

    public InvalidXPathException(String xpath) {
        super("Invalid XPath expression: " + xpath);
    }

    public InvalidXPathException(String xpath, String reason) {
        super("Invalid XPath expression: " + xpath + " " + reason);
    }

    public InvalidXPathException(String xpath, Throwable t) {
        super("Invalid XPath expression: '" + xpath + "'. Caused by: " + t.getMessage());
    }
}
