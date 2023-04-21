package org.nakedobjects.metamodel.specloader.collectiontyperegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionTypeRegistryDefault extends CollectionTypeRegistryAbstract {

    private final List<Class<?>> collectionTypes = new ArrayList<Class<?>>();

    private Class<?>[] collectionTypesAsArray = new Class[0];

    /**
     * Inbuilt support for {@link Collection} as a collection type.
     * 
     * <p>
     * Note that this includes any subclasses.
     */
    public CollectionTypeRegistryDefault() {
        addCollectionType(Collection.class);
    }

    /**
     * Plan is for this to be promoted to API at some stage.
     */
    private void addCollectionType(final Class<?> collectionType) {
        collectionTypes.add(collectionType);
        collectionTypesAsArray = collectionTypes.toArray(new Class[0]);
    }

    public boolean isCollectionType(final Class<?> cls) {
        return java.util.Collection.class.isAssignableFrom(cls);
    }

    public boolean isArrayType(final Class<?> cls) {
        return cls.isArray();
    }

    public Class<?>[] getCollectionType() {
        return collectionTypesAsArray;
    }
}
