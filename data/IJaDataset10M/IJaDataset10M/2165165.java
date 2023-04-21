package org.starobjects.jpa.runtime.persistence.objectstore.load.oneToManyEagerJoin;

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
public class GivenOneToManyWithEagerWhenResolveFieldTest extends Fixture {

    protected NakedObject loadAdapter() {
        NakedObject adapter = retrieveObject(ReferencingObject.class, referencingObjectPK);
        jpaObjectStore.resolveImmediately(adapter);
        return adapter;
    }

    @Test
    public void referencedObjectIsInitialized() throws Exception {
        NakedObject adapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        Set<ReferencedObjectA> referencedObjectAs = referencingObject.getReferencedObjectAs();
        PersistentCollection collection = (PersistentCollection) referencedObjectAs;
        assertThat(collection.wasInitialized(), is(true));
        NakedObject referencedObjectAsAdapter = getAdapterManager().adapterFor(referencedObjectAs);
        assertThat(referencedObjectAsAdapter.getResolveState().isResolved(), is(true));
        NakedObjectAssociation referencedObjectAsAssociation = referencingObjectSpec.getAssociation("referencedObjectAs");
        jpaObjectStore.resolveField(adapter, referencedObjectAsAssociation);
        assertThat(collection.wasInitialized(), is(true));
        assertThat(referencedObjectAsAdapter.getResolveState().isResolved(), is(true));
    }

    @Test
    public void emptyCollectionIsInitialized() throws Exception {
        NakedObject adapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        Set<ReferencedObjectB> referencedObjectBs = referencingObject.getReferencedObjectBs();
        PersistentCollection collection = (PersistentCollection) referencedObjectBs;
        assertThat(collection.wasInitialized(), is(true));
        NakedObject referencedObjectBsAdapter = getAdapterManager().adapterFor(referencedObjectBs);
        assertThat(referencedObjectBsAdapter.getResolveState().isResolved(), is(true));
        NakedObjectAssociation referencedObjectBsAssociation = referencingObjectSpec.getAssociation("referencedObjectBs");
        jpaObjectStore.resolveField(adapter, referencedObjectBsAssociation);
        assertThat(collection.wasInitialized(), is(true));
        assertThat(referencedObjectBsAdapter.getResolveState().isResolved(), is(true));
    }
}
