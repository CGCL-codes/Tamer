package net.sf.traser.common.identification;

import net.sf.traser.common.Identifier;

/**
 * Implementations of this interface can be used to resolve identifiers 
 * conform to certain numbering schemes.
 * @author szmarcell
 */
public interface Resolver {

    /**
     * Implementation of this method should return the identifier if they are 
     * capable of decoding the URI of the hosting server from it, or throw a 
     * SchemeException otherwise.
     * @param rep the string rep of the identifier.
     * @return the identifier resolved.
     * @throws net.sf.traser.common.identification.SchemeException if cannot 
     * decode.
     */
    Identifier resolve(String rep) throws SchemeException;

    /**
     * Tries to resolve the given string as an identifier using custom parsing rules,
     * but in difference to resolve(String) this method does not throw an
     * exception or makes log entries when the format is incorrect but
     * returns with null. It should be particularly useful for GUI text fields,
     * where the user enters an identifier as string and the GUI reflects that
     * the entered string is resolvable or not.
     * @param rep the string representation of a potential identifier, can be null
     * @return the resolved identifier or null, if rep is null or cannot be resolved
     */
    Identifier tryResolve(String rep);
}
