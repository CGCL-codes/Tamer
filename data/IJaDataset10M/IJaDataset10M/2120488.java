package edu.uis.csc478.spring09.threeoxen.application;

/**
 * User: Chris Logan
 * Date: Apr 15, 2009
 * Time: 3:18:20 PM
 */
public class ApplicationException extends Exception {

    public ApplicationException(String message, Exception e) {
        super(message, e);
    }

    public ApplicationException(String message) {
        super(message);
    }
}
