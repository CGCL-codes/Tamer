package com.divosa.eformulieren.util.exception;

/**
 * Exception throws when a date is of an unknown format in date handling operations.
 * 
 * @author Bart Ottenkamp
 */
public class DateFormatInvalidException extends DivosaUtilException {

    /**
     * Constructor.
     * @param message Error message.
     */
    public DateFormatInvalidException(final String message) {
        super(message);
    }
}
