package org.starobjects.jpa.runtime.persistence.objectstore.load.oneToManyLazy;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.Set;
import org.hibernate.collection.PersistentCollection;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;

@RunWith(JMock.class)
public class GivenOneToManyWithLazyWhenResolveField extends Fixture {

    protected NakedObject loadAdapter() {
        NakedObject adapter = retrieveObject(ReferencingObject.class, referencingObjectPK);
        jpaObjectStore.resolveImmediately(adapter);
        return adapter;
    }

    @Test
    public void nonEmptyCollectionIsInitialized() throws Exception {
        NakedObject adapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        Set<ReferencedObjectA> referencedObjects = referencingObject.getReferencedObjectAs();
        PersistentCollection collection = (PersistentCollection) referencedObjects;
        assertThat(collection.wasInitialized(), is(false));
        NakedObject referencedObjectAdapter = getAdapterManager().adapterFor(referencedObjects);
        assertThat(referencedObjectAdapter.getResolveState().isGhost(), is(true));
        NakedObjectAssociation referencedObjectsAssociation = referencingObjectSpec.getAssociation("referencedObjectAs");
        jpaObjectStore.resolveField(adapter, referencedObjectsAssociation);
        assertThat(collection.wasInitialized(), is(true));
        assertThat(referencedObjectAdapter.getResolveState().isResolved(), is(true));
    }

    @Test
    public void emptyCollectionIsInitialized() throws Exception {
        NakedObject adapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        Set<ReferencedObjectB> referencedObjects = referencingObject.getReferencedObjectBs();
        PersistentCollection collection = (PersistentCollection) referencedObjects;
        assertThat(collection.wasInitialized(), is(false));
        NakedObject referencedObjectAdapter = getAdapterManager().adapterFor(referencedObjects);
        assertThat(referencedObjectAdapter.getResolveState().isGhost(), is(true));
        NakedObjectAssociation referencedObjectsAssociation = referencingObjectSpec.getAssociation("referencedObjectBs");
        jpaObjectStore.resolveField(adapter, referencedObjectsAssociation);
        assertThat(collection.wasInitialized(), is(true));
        assertThat(referencedObjectAdapter.getResolveState().isResolved(), is(true));
    }
}
