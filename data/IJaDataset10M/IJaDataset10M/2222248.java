package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.xs.XSIDCDefinition;

/**
 * Schema key reference identity constraint.
 *
 * @xerces.internal 
 *
 * @author Andy Clark, IBM
 * @version $Id: KeyRef.java 572110 2007-09-02 19:04:44Z mrglavas $
 */
public class KeyRef extends IdentityConstraint {

    /** The key (or unique) being referred to. */
    protected final UniqueOrKey fKey;

    /** Constructs a keyref with the specified name. */
    public KeyRef(String namespace, String identityConstraintName, String elemName, UniqueOrKey key) {
        super(namespace, identityConstraintName, elemName);
        fKey = key;
        type = IC_KEYREF;
    }

    /** Returns the key being referred to.  */
    public UniqueOrKey getKey() {
        return fKey;
    }

    /**
     * {referenced key} Required if {identity-constraint category} is keyref,
     * forbidden otherwise. An identity-constraint definition with
     * {identity-constraint category} equal to key or unique.
     */
    public XSIDCDefinition getRefKey() {
        return fKey;
    }
}
