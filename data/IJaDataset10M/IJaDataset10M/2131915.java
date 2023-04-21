package de.mogwai.common.webservice.rest;

/**
 * Exception.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-06-17 15:06:47 $
 */
public class WebserviceException extends Exception {

    /**
     * Constructor.
     * 
     * @param aMessage
     *                the message
     * @param aException
     *                the wrapped exception
     */
    public WebserviceException(String aMessage, Exception aException) {
        super(aMessage, aException);
    }
}
