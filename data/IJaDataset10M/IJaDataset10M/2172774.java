package org.restlet.ext.jaxrs.internal.exceptions;

/**
 * This exception is thrown, if a root resource class or a provider has no valid
 * contructor
 * 
 * @author Stephan Koops
 */
public class MissingConstructorException extends JaxRsException {

    private static final long serialVersionUID = 8213720039895185212L;

    /**
     * @param jaxRsClass
     *            the root resource or provider class.
     * @param rrcOrProvider
     *            "root resource class" or "provider"
     */
    public MissingConstructorException(Class<?> jaxRsClass, String rrcOrProvider) {
        super("the " + rrcOrProvider + " " + jaxRsClass.getName() + " has no valid constructor");
    }
}
