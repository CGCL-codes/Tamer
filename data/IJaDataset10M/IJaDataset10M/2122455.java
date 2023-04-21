package com.oat.gui;

/**
 * Description: 
 *  
 * Date: 21/08/2007<br/>
 * @author Jason Brownlee 
 *
 * <br/>
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class GUIException extends RuntimeException {

    public GUIException() {
        super();
    }

    public GUIException(String message, Throwable cause) {
        super(message, cause);
    }

    public GUIException(String message) {
        super(message);
    }

    public GUIException(Throwable cause) {
        super(cause);
    }
}
