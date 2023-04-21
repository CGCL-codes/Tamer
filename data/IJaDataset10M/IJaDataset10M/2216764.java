package org.acegisecurity.domain;

import org.acegisecurity.AcegiSecurityException;

/**
 * Abstract superclass for all exceptions related to domain object support
 * subproject.
 *
 * @author Ben Alex
 * @version $Id: DomainException.java,v 1.2 2005/11/17 00:56:47 benalex Exp $
 */
public abstract class DomainException extends AcegiSecurityException {

    /**
     * Constructs a <code>DomainException</code> with the specified message and
     * root cause.
     *
     * @param msg the detail message
     * @param t the root cause
     */
    public DomainException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Constructs a <code>DomainException</code> with the specified message and
     * no root cause.
     *
     * @param msg the detail message
     */
    public DomainException(String msg) {
        super(msg);
    }
}
