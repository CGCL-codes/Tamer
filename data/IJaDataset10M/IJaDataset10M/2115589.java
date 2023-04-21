package net.groovysips.jdiff.delta;

import net.groovysips.jdiff.CompositeDelta;

/**
 * @author Alex Shneyderman
 * @since 0.3
 */
public class CollectionDelta extends AbstractCompositeDelta implements CompositeDelta {

    private String propertyName;

    private Class collectionClass;

    public String getPropertyName() {
        return propertyName;
    }

    public Class getCollectionClass() {
        return collectionClass;
    }

    public CollectionDelta(String propertyName, Class collectionClass) {
        this.propertyName = propertyName;
        this.collectionClass = collectionClass;
    }

    /**
     * A collections delta might have to do the following:
     *
     *   1. Create a collection and assign it to the target field.
     *   2. 
     */
    @Override
    public String toString() {
        return "CollectionDelta{" + "collectionClass=" + collectionClass + ", propertyName='" + propertyName + '\'' + '}';
    }
}
