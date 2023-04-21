package org.rubato.math.module;

import org.rubato.base.RubatoException;

/**
 * This exception is thrown whenever a module element operation fails due
 * to a wrong domain. The exception contains two pieces of information.
 * The <i>expected</i> module indicates the domain that the element was to be
 * part of. The <i>received</i> module is the domain that the actual element is
 * part of.
 */
public final class DomainException extends RubatoException {

    /**
     * Creates a DomainException.
     * 
     * @param message  indicates the reason for the exception
     * @param expected the module (or element of that module) that was required
     * @param received the actual module (or element of that module)
     */
    public DomainException(String message, Module expected, Module received) {
        super(message);
        this.expected = expected;
        this.received = received;
    }

    /**
     * Creates a DomainException.
     * A message is generated from <code>expected</code> and <code>received</code>.
     * 
     * @param expected the module (or element of that module) that was required
     * @param received the actual module (or element of that module)
     */
    public DomainException(Module expected, Module received) {
        this("Expected domain " + expected + ", got " + received + ".", expected, received);
    }

    /**
     * Returns the module that was required.
     */
    public Module getExpectedDomain() {
        return expected;
    }

    /**
     * Returns the actual module.
     */
    public Module getReceivedDomain() {
        return received;
    }

    private Module expected;

    private Module received;
}
