package com.jogamp.gluegen.runtime;

import com.jogamp.common.os.DynamicLookupHelper;

/**
 *
 * @author Michael Bien
 */
public interface FunctionAddressResolver {

    /**
     * Resolves the name of the function bound to the method and returns the address.
     */
    public long resolve(String name, DynamicLookupHelper lookup);
}
