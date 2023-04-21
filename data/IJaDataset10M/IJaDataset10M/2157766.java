package org.p4pp.p3p.document;

/**
 * Signals that a P3P policy (file) was invalid.
 * The calling program
 * should not treat this as if it would treat a "request".
 *
 * @author <a href="mailto:budzyn@ti.informatik.uni-kiel.de">Nikolaj Budzyn</a>
 * @see Exception
 */
public class InvalidPolicyException extends Exception {

    public InvalidPolicyException() {
    }

    public InvalidPolicyException(String msg) {
        super(msg);
    }
}
