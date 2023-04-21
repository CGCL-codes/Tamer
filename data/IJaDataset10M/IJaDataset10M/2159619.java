package com.huntersoftwaregroup.genericdataaccess.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: hasani
 * Date: Feb 16, 2007
 * Time: 1:45:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class GenericDataObjectNotFoundException extends GenericDataObjectException {

    /**
     * Constructs a GenericDataObjectProviderNotFoundException
     *
     * @param string message
     */
    public GenericDataObjectNotFoundException(final String string) {
        super(string);
    }

    /**
     * Constructs a GenericDataObjectProviderNotFoundException
     *
     * @param string    message
     * @param throwable cause of the exception.
     */
    public GenericDataObjectNotFoundException(final String string, final Throwable throwable) {
        super(string, throwable);
    }

    /**
     * Constructs a GenericDataObjectProviderNotFoundException
     *
     * @param throwable cause of the exception.
     */
    public GenericDataObjectNotFoundException(final Throwable throwable) {
        super(throwable);
    }
}
