package de.searchworkorange.searchserver.searcher.userAccess.exceptions;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class NoDomainDefinedException extends Exception {

    /**
     *
     * @param arg0
     */
    public NoDomainDefinedException(Throwable arg0) {
        super(arg0);
    }

    /**
     *
     * @param arg0
     * @param arg1
     */
    public NoDomainDefinedException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * 
     * @param arg0
     */
    public NoDomainDefinedException(String arg0) {
        super(arg0);
    }

    public NoDomainDefinedException() {
    }
}
