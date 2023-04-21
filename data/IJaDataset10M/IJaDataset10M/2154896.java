package org.npsnet.v.services.system;

import org.npsnet.v.kernel.ExtensibleServiceProvider;
import org.npsnet.v.kernel.PropertyBearer;

/**
 * The property transfer service.
 *
 * @author Andrzej Kapolka
 */
public interface PropertyTransferProvider extends ExtensibleServiceProvider {

    /**
     * Registers a new property transferrer for a specified property.
     *
     * @param propertyClass the property that the transferrer transfers
     * @param pt the new property transferrer
     */
    public void registerPropertyTransferrer(Class propertyClass, PropertyTransferrer pt);

    /**
     * Deregisters a property transferrer for a specified property.
     *
     * @param propertyClass the property that the transferrer transferred
     * @param pt the property transferrer to deregister
     */
    public void deregisterPropertyTransferrer(Class propertyClass, PropertyTransferrer pt);

    /**
     * Transfers all compatible properties from one property bearer to another.
     *
     * @param fromBearer the source property bearer
     * @param toBearer the destination property bearer
     */
    public void transferProperties(PropertyBearer fromBearer, PropertyBearer toBearer);
}
