package de.hbrs.inf.atarrabi.action.datacite;

/**
 * Exception describing errors while fetching DataCite XML.
 * 
 * @author Florian Quadt
 */
public class DataCiteXmlException extends Exception {

    private static final long serialVersionUID = -102926590401051752L;

    public DataCiteXmlException() {
        super();
    }

    public DataCiteXmlException(String message) {
        super(message);
    }

    public DataCiteXmlException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
