package com.avaje.ebean.server.deploy.meta;

import com.avaje.ebean.Query;
import com.avaje.ebean.bean.BeanCollection.ModifyListenMode;
import com.avaje.ebean.server.type.ScalarType;

public class DeployBeanPropertySimpleCollection<T> extends DeployBeanPropertyAssocMany<T> {

    private final ScalarType<T> collectionScalarType;

    public DeployBeanPropertySimpleCollection(DeployBeanDescriptor<?> desc, Class<T> targetType, ScalarType<T> scalarType, Query.Type queryType) {
        super(desc, targetType, queryType);
        this.collectionScalarType = scalarType;
        this.modifyListenMode = ModifyListenMode.ALL;
    }

    /**
     * Return the scalarType of the collection elements.
     */
    public ScalarType<T> getCollectionScalarType() {
        return collectionScalarType;
    }

    /**
     * Returns false as never a ManyToMany.
     */
    @Override
    public boolean isManyToMany() {
        return false;
    }

    /**
     * Returns true as always Unidirectional.
     */
    @Override
    public boolean isUnidirectional() {
        return true;
    }
}
