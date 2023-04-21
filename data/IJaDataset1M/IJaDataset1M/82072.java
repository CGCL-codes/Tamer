package org.dyndns.doujindb.conf;

/**  
* PropertyException.java
* @author  nozomu
* @version 1.0
*/
@SuppressWarnings("serial")
public class PropertyException extends RuntimeException {

    public PropertyException() {
    }

    public PropertyException(String message) {
        super(message);
    }

    public PropertyException(Throwable cause) {
        super(cause);
    }

    public PropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
