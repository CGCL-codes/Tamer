package com.versant.core.common;

import com.versant.core.metadata.ClassMetaData;
import java.io.Serializable;

/**
 * Factory for OID and State instances. These must be Serializable so they
 * can be sent with the meta data to remote clients.
 */
public interface StateAndOIDFactory extends Serializable {

    /**
     * Create a new empty OID for cmd.
     *
     * @param resolved Is this a resolved OID?
     * @see OID#isResolved
     * @see OID#resolve
     */
    public OID createOID(ClassMetaData cmd, boolean resolved);

    /**
     * Create a new empty State for cmd.
     */
    public State createState(ClassMetaData cmd);

    /**
     * Create a new NewObjectOID.
     */
    public NewObjectOID createNewObjectOID(ClassMetaData cmd);

    /**
     * Create a new untyped OID if the store supports these or throw an
     * unsupported option exception if not.
     */
    public OID createUntypedOID();
}
