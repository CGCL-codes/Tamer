package org.jtools.mvc.impl.meta;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.enaf.data.annotations.EnafServer;
import org.jtools.mvc.impl.util.CollectionFactory;
import org.jtools.mvc.meta.MVCEntityMetaData;
import org.jtools.mvc.meta.MVCServerMetaData;

/**
 * TODO type-description
 * @author <a href="mailto:rainer.noack@jtools.org">Rainer Noack</a>
 */
public class SimpleMVCServerMetaData<T_EntityId> implements MVCServerMetaData<T_EntityId> {

    private Map<T_EntityId, MVCEntityMetaData<T_EntityId, ?, ?>> entityMetaDataById;

    private Class<T_EntityId> mvcEntityIdClass;

    public MVCEntityMetaData<T_EntityId, ?, ?> getMVCEntity(T_EntityId dataSetId) {
        return entityMetaDataById.get(dataSetId);
    }

    public Class<T_EntityId> getMVCEntityIdClass() {
        return mvcEntityIdClass;
    }

    public SimpleMVCServerMetaData(Class<T_EntityId> mvcEntityIdClass, MVCEntityMetaData<T_EntityId, ?, ?>... metaData) {
        this.mvcEntityIdClass = mvcEntityIdClass;
        this.entityMetaDataById = CollectionFactory.getCollectionFactory().createMap(mvcEntityIdClass);
        if (metaData != null) for (MVCEntityMetaData<T_EntityId, ?, ?> set : metaData) entityMetaDataById.put(set.getMVCEntityId(), set);
    }
}
