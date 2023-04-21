package org.eclipse.core.internal.registry;

/**
 * An object which has the general characteristics of all the nestable elements
 * in a plug-in manifest.
 */
public abstract class RegistryObject implements KeyedElement {

    private int objectId = RegistryObjectManager.UNKNOWN;

    protected int[] children = RegistryObjectManager.EMPTY_INT_ARRAY;

    protected int extraDataOffset = -1;

    void setRawChildren(int[] values) {
        children = values;
    }

    int[] getRawChildren() {
        return children;
    }

    void setObjectId(int value) {
        objectId = value;
    }

    int getObjectId() {
        return objectId;
    }

    public int getKeyHashCode() {
        return objectId;
    }

    public Object getKey() {
        return new Integer(objectId);
    }

    public boolean compare(KeyedElement other) {
        return objectId == ((RegistryObject) other).objectId;
    }
}
