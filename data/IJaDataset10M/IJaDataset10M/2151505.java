package org.tuotoo.util;

/**
 * This exception is thrown if an error occurs while parsing an XML structure.
 * 
 * @author Wendolsky
 */
public class XMLParseException extends Exception {

    /**
     * A constant that means that the document to parse has a wrong root tag.
     */
    public static final String ROOT_TAG = "##__root__##";

    /**
     * A constant that means that a node to parse was null.
     */
    public static final String NODE_NULL_TAG = "##__null__##";

    /**
     * Creates a new exception.
     * 
     * @param a_strTagName
     *            the name of the tag where the exception occured
     * @param a_strMessage
     *            an additional message for a detailed description of this
     *            exception
     */
    public XMLParseException(String a_strTagName, String a_strMessage) {
        super(parseTagName(a_strTagName) + getMessage(a_strMessage));
    }

    /**
     * Creates a new exception.
     * 
     * @param a_strTagName
     *            the name of the tag where the exception occured
     */
    public XMLParseException(String a_strTagName) {
        this(a_strTagName, null);
    }

    private static String getMessage(String a_strMessage) {
        if (a_strMessage == null) {
            return "";
        }
        return a_strMessage;
    }

    /**
     * Creates an error message from the given tag name.
     * 
     * @param a_strTagName
     *            a tag name to parse
     * @return the parsed tag name
     */
    private static String parseTagName(String a_strTagName) {
        String strParseError = "Error while parsing XML ";
        if (a_strTagName == null) {
            strParseError = "";
        } else if (a_strTagName.equals(ROOT_TAG)) {
            strParseError += "document root! ";
        } else if (a_strTagName.endsWith(NODE_NULL_TAG)) {
            strParseError += "- node is null! ";
        } else {
            strParseError += "node '" + a_strTagName + "'! ";
        }
        return strParseError;
    }
}
