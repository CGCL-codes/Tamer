package com.saugstation.sbev;

/**
 * @author ms
 *
 * Exception thrown by Validator 
 */
public class ValidatorException extends Exception {

    /**
   * New ValidatorException 
   */
    public ValidatorException() {
        super();
    }

    /**
   * New ValidatorException with exception description 
   */
    public ValidatorException(String message) {
        super(message);
    }
}
