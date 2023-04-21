package org.codehaus.groovy.grails.exceptions;

/**
 * <p>Occurs when creation of the Grails domain from the Grails domain classes fails</p>
 * 
 * @author Graeme Rocher
 * @since 06-Jul-2005
 */
public class GrailsDomainException extends GrailsException {

    public GrailsDomainException() {
        super();
    }

    public GrailsDomainException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public GrailsDomainException(String arg0) {
        super(arg0);
    }

    public GrailsDomainException(Throwable arg0) {
        super(arg0);
    }
}
