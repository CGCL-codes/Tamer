package de.schlund.pfixcore.webservice.jaxws.generate;

/**
 * Exception class for Javascript stub generation errors
 * 
 * @author mleidig@schlund.de
 */
public class Wsdl2JsException extends Exception {

    private static final long serialVersionUID = 6091746543269599068L;

    public Wsdl2JsException(String msg) {
        super(msg);
    }

    public Wsdl2JsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
