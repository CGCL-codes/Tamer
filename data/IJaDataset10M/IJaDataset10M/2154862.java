package osdb.redstone.xmlrpc;

/**
 *  The exception thrown by the XML-RPC library in case of serialization or
 *  network problems. If a call is successfully made but the remote XML-RPC service
 *  returns a fault message, an XmlRpcFault exception will be thrown.
 *
 *  @author Greger Olsson
 */
public class XmlRpcException extends RuntimeException {

    /**
     *  Creates a new exception with the supplied message.
     *
     *  @param message The exception message.
     */
    public XmlRpcException(String message) {
        super(message);
    }

    /**
     *  Creates a new exception with the supplied message.
     *  The supplied cause will be attached to the exception.
     *  
     *  @param message The error message.
     *  @param cause The original cause leading to the exception.
     */
    public XmlRpcException(String message, Throwable cause) {
        super(message, cause);
    }

    /** <describe> */
    private static final long serialVersionUID = 3257844394139596598L;
}
