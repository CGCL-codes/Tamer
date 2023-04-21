package org.codehaus.groovy.grails.validation.exceptions;

import org.codehaus.groovy.grails.exceptions.GrailsException;

/**
 * An exception thrown when an error occurs applying a constraint to a property 
 * 
 * @author Graeme Rocher
 * @since 10-Nov-2005
 */
public class ConstraintException extends GrailsException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4866968714197516789L;

    public ConstraintException() {
        super();
    }

    public ConstraintException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ConstraintException(String arg0) {
        super(arg0);
    }

    public ConstraintException(Throwable arg0) {
        super(arg0);
    }
}
