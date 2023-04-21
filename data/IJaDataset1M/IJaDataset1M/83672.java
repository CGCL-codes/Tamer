package com.maziade.sprites.engine;

/**
 * Created on September 24, 2002, 4:23 PM
 * @author Eric Maziade
 * 
 * Exception thrown when the specified display mode cannot be initialized
 */
public class DisplayModeNotFoundException extends java.lang.Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6849172515255678293L;

    /**
   * Creates a new instance of <code>DisplayModeNotFoundException</code> without detail message.
   */
    public DisplayModeNotFoundException() {
    }

    /**
   * Constructs an instance of <code>DisplayModeNotFoundException</code> with the specified detail message.
   * @param msg the detail message.
   */
    public DisplayModeNotFoundException(String msg) {
        super(msg);
    }
}
