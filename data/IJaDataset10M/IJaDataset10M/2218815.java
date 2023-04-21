package org.datanucleus.identity;

import org.datanucleus.store.ExecutionContext;

/**
 * Translator for object identities where the user wants to input identities that are not strict
 * JDO standard, so this converts them.
 */
public interface IdentityTranslator {

    /**
     * Method to translate the object into the identity.
     * @param ec ExecutionContext
     * @param obj The object
     * @return The identity
     */
    Object getIdentity(ExecutionContext ec, Object obj);
}
