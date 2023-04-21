package com.sun.org.apache.xerces.internal.impl.xs.identity;

import com.sun.org.apache.xerces.internal.xs.XSIDCDefinition;

/**
 * Schema key reference identity constraint.
 *
 * @xerces.internal 
 *
 * @author Andy Clark, IBM
 * @version $Id: KeyRef.java,v 1.2.6.1 2005/09/08 07:37:21 sunithareddy Exp $
 */
public class KeyRef extends IdentityConstraint {

    /** The key (or unique) being referred to. */
    protected UniqueOrKey fKey;

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
