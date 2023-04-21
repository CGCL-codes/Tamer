package org.nakedobjects.object.loader;

import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.reflect.NameConvertor;
import org.nakedobjects.utility.Assert;

/**
 * Provides a simple key for reliably getting hold of the adapter for an 'internal' collection used by a
 * domain object. Combines the parent object (the domain object) and the collection field to create an
 * unchanging key.
 */
class InternalCollectionKey {

    private final NakedObject parent;

    private final String fieldName;

    public InternalCollectionKey(NakedObject parent, String fieldName) {
        Assert.assertNotNull("parent", parent);
        Assert.assertNotNull("fieldName", fieldName);
        this.parent = parent;
        this.fieldName = NameConvertor.simpleName(fieldName);
    }

    public int hashCode() {
        return parent.hashCode() + fieldName.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof InternalCollectionKey) {
            InternalCollectionKey other = (InternalCollectionKey) obj;
            return other.parent.equals(parent) && other.fieldName.equals(fieldName);
        } else {
            return false;
        }
    }
}
