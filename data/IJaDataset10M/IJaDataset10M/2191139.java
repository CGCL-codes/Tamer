package org.jdiameter.client.api.io;

/**
 * Signals that an transport exception has occurred in a during initialize
 * transport element.
 * 
 * @author erick.svenson@yahoo.com
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class TransportException extends Exception {

    /**
     * Error code
     */
    TransportError code;

    /**
     * Create instance of class with predefined parameters
     * @param message error message
     * @param code error code
     */
    public TransportException(String message, TransportError code) {
        super(message);
        this.code = code;
    }

    /**
     * Create instance of class with predefined parameters
     * @param message error message
     * @param code error code
     * @param cause error cause
     */
    public TransportException(String message, TransportError code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Create instance of class with predefined parameters
     * @param code error code
     * @param cause error cause
     */
    public TransportException(TransportError code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    /**
     * Return code of error
     * @return  code of error
     */
    public TransportError getCode() {
        return code;
    }
}
