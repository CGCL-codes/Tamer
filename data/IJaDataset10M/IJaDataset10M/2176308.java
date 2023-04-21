package org.codehaus.groovy.grails.exceptions;

/**
 * <p>Occurs when the creation of a new instance fails.
 * 
 * @author Steven Devijver
 * @since Jul 2, 2005
 */
public class NewInstanceCreationException extends GrailsException {

    public NewInstanceCreationException() {
        super();
    }

    public NewInstanceCreationException(String arg0) {
        super(arg0);
    }

    public NewInstanceCreationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public NewInstanceCreationException(Throwable arg0) {
        super(arg0);
    }
}
