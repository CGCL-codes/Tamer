package org.codehaus.groovy.grails.scaffolding.exceptions;

import org.codehaus.groovy.grails.exceptions.GrailsException;

/**
 * Exception thrown generally when initialisation of scaffolding fails
 * 
 * @author Graeme Rocher
 * @since 30 Nov 2005
 */
public class ScaffoldingException extends GrailsException {

    public ScaffoldingException() {
        super();
    }

    public ScaffoldingException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ScaffoldingException(String arg0) {
        super(arg0);
    }

    public ScaffoldingException(Throwable arg0) {
        super(arg0);
    }
}
