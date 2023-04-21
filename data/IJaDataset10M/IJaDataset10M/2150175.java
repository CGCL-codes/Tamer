package org.datanucleus.store.mapped.mapping;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.store.ObjectProvider;
import org.datanucleus.store.mapped.DatastoreContainerObject;

/**
 * Mapping for a PersistenceCapable object stored in an embedded collection within a
 * PersistenceCapable object. Provides mapping for a single Java type
 * (the element PC type) to multiple datastore columns.
 */
public class EmbeddedElementPCMapping extends EmbeddedMapping {

    /**
     * Initialize this JavaTypeMapping with the given DatastoreAdapter for
     * the given FieldMetaData.
     * @param container The datastore container storing this mapping (if any)
     * @param clr the ClassLoaderResolver
     * @param fmd FieldMetaData for the field to be mapped (if any)
     */
    public void initialize(AbstractMemberMetaData fmd, DatastoreContainerObject container, ClassLoaderResolver clr) {
        initialize(fmd, container, clr, fmd.getElementMetaData().getEmbeddedMetaData(), fmd.getCollection().getElementType(), ObjectProvider.EMBEDDED_COLLECTION_ELEMENT_PC);
    }
}
