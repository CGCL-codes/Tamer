package com.db4o.internal;

import com.db4o.foundation.*;

/**
 * @exclude
 */
public interface ReferenceSystem {

    public void addNewReference(ObjectReference ref);

    public void addExistingReference(ObjectReference ref);

    public void commit();

    public ObjectReference referenceForId(int id);

    public ObjectReference referenceForObject(Object obj);

    public void removeReference(ObjectReference ref);

    public void rollback();

    public void traverseReferences(Visitor4 visitor);
}
