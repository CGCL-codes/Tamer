package com.acv.connector.exception;

/**
 *
 * The Exception class for bus request building.
 * @author Bin Chen
 *
 */
public class BusRequestBuilderException extends Exception {

    /**
	 *
	 */
    private static final long serialVersionUID = 4817323354621797271L;

    public BusRequestBuilderException() {
        super();
    }

    public BusRequestBuilderException(String error) {
        super(error);
    }

    public BusRequestBuilderException(Exception e) {
        super(e);
    }
}
