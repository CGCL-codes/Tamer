package org.starobjects.jpa.runtime.persistence.objectstore.load.oneToManyEagerJoin;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.AggregatedOid;

public abstract class AbstractTestCase extends Fixture {

    protected abstract NakedObject loadAdapter();

    @Test
    public void shouldResolveReferencingObject() throws Exception {
        NakedObject adapter = loadAdapter();
        assertThat(adapter.getResolveState().isResolved(), is(true));
    }

    @Test
    public void shouldCreateAdapterForNonEmptyCollection() throws Exception {
        NakedObject adapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        Set<ReferencedObjectA> referencedObjectAs = referencingObject.getReferencedObjectAs();
        NakedObject referencedObjectsAdapter = getAdapterManager().getAdapterFor(referencedObjectAs);
        assertThat(referencedObjectsAdapter, is(notNullValue()));
    }

    @Test
    public void shouldCreateAdapterForEmptyCollection() throws Exception {
        NakedObject adapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        Set<ReferencedObjectB> referencedObjectBs = referencingObject.getReferencedObjectBs();
        NakedObject referencedObjectsAdapter = getAdapterManager().getAdapterFor(referencedObjectBs);
        assertThat(referencedObjectsAdapter, is(notNullValue()));
    }

    @Test
    public void adapterOidForNonEmptyCollectionShouldBeAggregated() throws Exception {
        NakedObject adapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        Set<ReferencedObjectA> referencedObjects = referencingObject.getReferencedObjectAs();
        NakedObject referencedObjectsAdapter = getAdapterManager().getAdapterFor(referencedObjects);
        assertThat(referencedObjectsAdapter.getOid(), is(AggregatedOid.class));
    }

    @Test
    public void adapterOidForEmptyCollectionShouldBeAggregated() throws Exception {
        NakedObject adapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        Set<ReferencedObjectB> referencedObjects = referencingObject.getReferencedObjectBs();
        NakedObject referencedObjectsAdapter = getAdapterManager().getAdapterFor(referencedObjects);
        assertThat(referencedObjectsAdapter.getOid(), is(AggregatedOid.class));
    }

    @Test
    public void nonEmptyCollectionAdapterShouldBeResolved() throws Exception {
        NakedObject referencingObjectAdapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) referencingObjectAdapter.getObject();
        Set<ReferencedObjectA> referencedObjects = referencingObject.getReferencedObjectAs();
        NakedObject referencedObjectsAdapter = getAdapterManager().getAdapterFor(referencedObjects);
        assertThat(referencedObjectsAdapter.getResolveState().isResolved(), is(true));
    }

    @Test
    public void emptyCollectionAdapterShouldBeResolved() throws Exception {
        NakedObject referencingObjectAdapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) referencingObjectAdapter.getObject();
        Set<ReferencedObjectB> referencedObjects = referencingObject.getReferencedObjectBs();
        NakedObject referencedObjectsAdapter = getAdapterManager().getAdapterFor(referencedObjects);
        assertThat(referencedObjectsAdapter.getResolveState().isResolved(), is(true));
    }

    @Test
    public void shouldResolveReferencingObjectAndReferencedObjectsWhenThereAreSome() throws Exception {
        NakedObject adapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        assertThat(referencingObject.getId(), is(seedReferencingObject.getId()));
        assertThat(referencingObject.getDescription(), is(seedReferencingObject.getDescription()));
        Set<ReferencedObjectA> referencedObjects = referencingObject.getReferencedObjectAs();
        assertThat(referencedObjects.size(), is(NUMBER_REFERENCED_OBJECTS));
        Set<Serializable> primaryKeys = new HashSet<Serializable>();
        for (ReferencedObjectA ro : referencedObjects) {
            primaryKeys.add(ro.getId());
        }
        for (int i = 0; i < NUMBER_REFERENCED_OBJECTS; i++) {
            assertThat(primaryKeys.contains(referencedObjectPrefixPK + i), is(true));
        }
    }

    @Test
    public void canResolveReferencingObjectButReferencedObjectsIfEmptyCollection() throws Exception {
        NakedObject adapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        Set<ReferencedObjectB> referencedObjects = referencingObject.getReferencedObjectBs();
        assertThat(referencedObjects.size(), is(0));
    }

    @Test
    public void shouldMarkAdaptersOfReferencedObjectsAsResolved() throws Exception {
        NakedObject referencingObjectAdapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) referencingObjectAdapter.getObject();
        Set<NakedObject> adapterReferencedObjects = asAdapters(referencingObject.getReferencedObjectAs());
        for (NakedObject adapter : adapterReferencedObjects) {
            assertThat(adapter.isPersistent(), is(true));
            assertThat(adapter.getResolveState().isResolved(), is(true));
        }
    }

    @Test
    public void shouldTriggerUniqueOnPostLoadEventsForEachLoadedObject() throws Exception {
        @SuppressWarnings("unused") NakedObject adapter = loadAdapter();
        assertThat(loadPostEvents.size(), is(NUMBER_REFERENCED_OBJECTS + 1));
        assertNotSame(loadPostEvents.get(0), loadPostEvents.get(1));
        assertNotSame(loadPostEvents.get(0), loadPostEvents.get(2));
        assertNotSame(loadPostEvents.get(0), loadPostEvents.get(3));
        assertNotSame(loadPostEvents.get(1), loadPostEvents.get(2));
        assertNotSame(loadPostEvents.get(1), loadPostEvents.get(3));
        assertNotSame(loadPostEvents.get(2), loadPostEvents.get(3));
    }
}
