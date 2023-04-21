package com.ibm.realtime.flexotask.template;

/**
 * Exception that is thrown for all errors during Flexotask validation
 */
public class FlexotaskValidationException extends Exception {

    static final long serialVersionUID = 1;

    public FlexotaskValidationException() {
        super();
    }

    public FlexotaskValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public FlexotaskValidationException(String msg) {
        super(msg);
    }
}
