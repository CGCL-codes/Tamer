package tms.client.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This is thrown when an exception occurred when attempting to retrieve some data. 
 * 
 * @author Werner Liebenberg
 * @author Wildrich Fourie
 * @author Martin Schlemmer
 */
public class DataOperationException extends Exception implements IsSerializable {

    private static final long serialVersionUID = -2964944756740619695L;

    public DataOperationException() {
    }

    public DataOperationException(String message) {
        super(message);
    }

    public DataOperationException(Throwable cause) {
        super(cause);
        this.setStackTrace(cause.getStackTrace());
    }

    public DataOperationException(String message, Throwable cause) {
        super(message, cause);
        this.setStackTrace(cause.getStackTrace());
    }
}
