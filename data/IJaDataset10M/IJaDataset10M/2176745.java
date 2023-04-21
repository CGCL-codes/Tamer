package com.google.code.ei4j.exceptions;

/**
 * Exception thrown if the input format is unknown.
 */
public class UnknownInputFormatException extends UnknownFormatException {

    public UnknownInputFormatException() {
        super("Image format is unknown");
    }
}
