package com.huntersoftwaregroup.genericdataaccess;

import com.huntersoftwaregroup.genericdataaccess.provider.GenericDataObjectProvider;

/**
 * Generic Data Object
 *
 * @author Hasani Hunter
 * @created 02/15/07
 */
public interface GenericDataObject {

    /**
     * Returns the provider that this object is registered with.
     *
     * @return GenericDataObjectProvider
     * @created 02/15/07
     */
    public GenericDataObjectProvider getProvider();
}
