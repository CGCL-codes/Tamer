package org.zzdict.utils;

/**
 * WrongPropertyException
 * 
 * Throw this exception when required property is missing, or property type is
 * wrong.
 * 
 * @author zzh
 * 
 */
public class WrongPropertyException extends Exception {

    /**
	 * Constructor of WrongPropertyException class
	 * 
	 * @param message
	 *            exception message
	 */
    public WrongPropertyException(String message) {
        super(message);
    }

    private static final long serialVersionUID = -7114193572881066308L;
}
