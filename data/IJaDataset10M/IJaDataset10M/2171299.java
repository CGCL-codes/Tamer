package com.logica.smpp.pdu;

/**
 * @author Logica Mobile Networks SMPP Open Source Team
 * @version 1.0, 11 Jun 2001
 */
public class WrongLengthOfStringException extends PDUException {

    public WrongLengthOfStringException() {
        super("The string is shorter or longer than required.");
    }

    public WrongLengthOfStringException(int min, int max, int actual) {
        super("The string is shorter or longer than required: " + " min=" + min + " max=" + max + " actual=" + actual + ".");
    }
}
