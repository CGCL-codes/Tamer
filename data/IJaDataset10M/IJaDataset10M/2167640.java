package com.foursoft.fourever.objectmodel.exception;

/**
 * Exception thrown when an attempt is made to introduce a new binding to a type
 * which would require evolution of already created instances of the type.
 * 
 */
public class InstanceEvolutionNotSupportedException extends Exception {

    /**
	 * Constructs a InstanceEvolutionNotSupportedException
	 */
    public InstanceEvolutionNotSupportedException() {
        super();
    }

    /**
	 * Constructs a InstanceEvolutionNotSupportedException
	 * 
	 * @param message
	 *            the exception message
	 */
    public InstanceEvolutionNotSupportedException(String message) {
        super(message);
    }
}
