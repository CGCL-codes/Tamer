package org.coos.javaframe;

/**
 * 
 * @author Alf Kristian St?yle
 */
public class NotInitializedException extends Exception {

    /**
     *
     *
     */
    public NotInitializedException() {
        super();
    }

    /**
	 * 
	 * @param message
	 */
    public NotInitializedException(String message) {
        super(message);
    }
}
