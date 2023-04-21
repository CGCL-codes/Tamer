package org.simpleframework.util;

/**
 * The <code>FormatException</code> is used to create exceptions that
 * can use a template string for the message. Each format exception
 * will accept a string and an ordered list of variables which can 
 * be used to complete the exception message.
 * 
 * @author Niall Gallagher
 */
public class FormatException extends Exception {

    /**
    * Constructor for the <code>FormatException</code> this requires
    * a template message and an ordered list of values that are to
    * be inserted in to the provided template to form the error.
    * 
    * @param template this is the template string to be modified
    * @param list this is the list of values that are to be inserted
    */
    public FormatException(String template, Object... list) {
        super(String.format(template, list));
    }

    /**
    * Constructor for the <code>FormatException</code> this requires
    * a template message and an ordered list of values that are to
    * be inserted in to the provided template to form the error.
    * 
    * @param cause this is the original cause of the exception
    * @param template this is the template string to be modified
    * @param list this is the list of values that are to be inserted
    */
    public FormatException(Throwable cause, String template, Object... list) {
        super(String.format(template, list), cause);
    }
}
