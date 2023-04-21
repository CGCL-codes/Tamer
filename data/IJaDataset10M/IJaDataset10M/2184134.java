package org.avis.util;

/**
 * Thrown when an illegal option name or value is used in a
 * configuration.
 * 
 * @author Matthew Phillips
 */
public class IllegalConfigOptionException extends IllegalArgumentException {

    public IllegalConfigOptionException(String message) {
        super(message);
    }

    public IllegalConfigOptionException(String option, String message) {
        this("Error in option \"" + option + "\": " + message);
    }
}
