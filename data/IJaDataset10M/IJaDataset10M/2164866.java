package org.jtoc.convertor.cpunit;

/**
 * The base Exception in org.jtoc.convertor. When catched, the user could
 * get message like the following one that would help the user find the problem :
 * <pre>
 * (FileName.java:123) Message content
 * </pre>
 * 
 */
public class JtocException extends Exception {

    private static final long serialVersionUID = -3004067126281077952L;

    /**
	 * 
	 * @param message Error Message
	 * @param fileName the name of the file being parsed
	 * @param lineNumber the number of the line caused this Exception 
	 */
    public JtocException(String message, String fileName, int lineNumber) {
        super('(' + fileName + ':' + lineNumber + ") " + message);
    }
}
