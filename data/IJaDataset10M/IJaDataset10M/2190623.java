package org.simpleframework.http.transport;

import org.simpleframework.util.FormatException;

/**
 * The <code>TransportException</code> object is thrown when there 
 * is a problem with the transport. Typically this is done thrown if
 * there is a problem reading or writing to the transport.
 * 
 * @author Niall Gallagher
 */
public class TransportException extends FormatException {

    /**
    * Constructor for the <code>TransportException</code> object. If
    * there is a problem sending or reading from a transport then it
    * will throw a transport exception to report the error.
    * 
    * @param message this is the message associated with the error
    * @param list this is the list of parameters for the template
    */
    public TransportException(String message, Object... list) {
        super(message, list);
    }
}
