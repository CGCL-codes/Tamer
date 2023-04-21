package com.versant.core.jdo.sco.detached;

import com.versant.core.jdo.sco.*;
import com.versant.core.metadata.FieldMetaData;
import com.versant.core.metadata.MDStaticUtils;
import com.versant.core.metadata.MDStatics;
import com.versant.core.common.BindingSupportImpl;

/**
 * This registry map the SCO types to their factories.
 */
public class DetachSCOFactoryRegistry {

    /**
     * Get the factory for a simple SCO type.
     */
    public VersantSCOFactory getJdoGenieSCOFactory(FieldMetaData fmd) {
        if (fmd.category == MDStatics.CATEGORY_SIMPLE && fmd.typeCode == MDStatics.DATE) {
            return new DetachSCODateFactory();
        }
        return null;
    }

    /**
     * Get the factory for a SCO collection.
     */
    public VersantSCOCollectionFactory getJDOGenieSCOCollectionFactory(FieldMetaData fmd) {
        if (fmd.category == MDStatics.CATEGORY_COLLECTION) {
            switch(fmd.typeCode) {
                case MDStatics.HASHSET:
                case MDStatics.SET:
                    return new DetachSCOHashSetFactory();
                case MDStatics.TREESET:
                case MDStatics.SORTEDSET:
                    return new DetachSCOTreeSetFactory();
                case MDStatics.COLLECTION:
                case MDStatics.LIST:
                case MDStatics.ARRAYLIST:
                    return new DetachSCOArrayListFactory();
                case MDStatics.LINKEDLIST:
                    return new DetachSCOLinkedListFactory();
                case MDStatics.VECTOR:
                    return new DetachSCOVectorFactory();
                default:
                    throw BindingSupportImpl.getInstance().notImplemented("Creating a SCO instance for field " + fmd.getName() + " of type " + MDStaticUtils.toSimpleName(fmd.typeCode) + " is not supported");
            }
        }
        return null;
    }

    /**
     * Get the factory for a SCO map.
     */
    public VersantSCOMapFactory getJDOGenieSCOMapFactory(FieldMetaData fmd) {
        if (fmd.category == MDStatics.CATEGORY_MAP) {
            switch(fmd.typeCode) {
                case MDStatics.MAP:
                case MDStatics.HASHMAP:
                    return new DetachSCOHashMapFactory();
                case MDStatics.HASHTABLE:
                    return new DetachSCOHashtableFactory();
                case MDStatics.TREEMAP:
                case MDStatics.SORTEDMAP:
                    return new DetachSCOTreeMapFactory();
                default:
                    throw BindingSupportImpl.getInstance().internal("Type code not supported as map. TypeCode = " + MDStaticUtils.toSimpleName(fmd.typeCode));
            }
        }
        return null;
    }
}
