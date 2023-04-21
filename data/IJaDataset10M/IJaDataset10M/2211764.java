package org.apache.isis.extensions.jpa.runtime.persistence.objectstore.load.oneToManyEagerJoin;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.adapter.oid.AggregatedOid;
import org.junit.Test;

public abstract class AbstractTestCase extends Fixture {

    protected abstract ObjectAdapter loadAdapter();

    @Test
    public void shouldResolveReferencingObject() throws Exception {
        final ObjectAdapter adapter = loadAdapter();
        assertThat(adapter.getResolveState().isResolved(), is(true));
    }

    @Test
    public void shouldCreateAdapterForNonEmptyCollection() throws Exception {
        final ObjectAdapter adapter = loadAdapter();
        final ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        final Set<ReferencedObjectA> referencedObjectAs = referencingObject.getReferencedObjectAs();
        final ObjectAdapter referencedObjectsAdapter = getAdapterManager().getAdapterFor(referencedObjectAs);
        assertThat(referencedObjectsAdapter, is(notNullValue()));
    }

    @Test
    public void shouldCreateAdapterForEmptyCollection() throws Exception {
        final ObjectAdapter adapter = loadAdapter();
        final ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        final Set<ReferencedObjectB> referencedObjectBs = referencingObject.getReferencedObjectBs();
        final ObjectAdapter referencedObjectsAdapter = getAdapterManager().getAdapterFor(referencedObjectBs);
        assertThat(referencedObjectsAdapter, is(notNullValue()));
    }

    @Test
    public void adapterOidForNonEmptyCollectionShouldBeAggregated() throws Exception {
        final ObjectAdapter adapter = loadAdapter();
        final ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        final Set<ReferencedObjectA> referencedObjects = referencingObject.getReferencedObjectAs();
        final ObjectAdapter referencedObjectsAdapter = getAdapterManager().getAdapterFor(referencedObjects);
        assertThat(referencedObjectsAdapter.getOid(), is(AggregatedOid.class));
    }

    @Test
    public void adapterOidForEmptyCollectionShouldBeAggregated() throws Exception {
        final ObjectAdapter adapter = loadAdapter();
        final ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        final Set<ReferencedObjectB> referencedObjects = referencingObject.getReferencedObjectBs();
        final ObjectAdapter referencedObjectsAdapter = getAdapterManager().getAdapterFor(referencedObjects);
        assertThat(referencedObjectsAdapter.getOid(), is(AggregatedOid.class));
    }

    @Test
    public void nonEmptyCollectionAdapterShouldBeResolved() throws Exception {
        final ObjectAdapter referencingObjectAdapter = loadAdapter();
        final ReferencingObject referencingObject = (ReferencingObject) referencingObjectAdapter.getObject();
        final Set<ReferencedObjectA> referencedObjects = referencingObject.getReferencedObjectAs();
        final ObjectAdapter referencedObjectsAdapter = getAdapterManager().getAdapterFor(referencedObjects);
        assertThat(referencedObjectsAdapter.getResolveState().isResolved(), is(true));
    }

    @Test
    public void emptyCollectionAdapterShouldBeResolved() throws Exception {
        final ObjectAdapter referencingObjectAdapter = loadAdapter();
        final ReferencingObject referencingObject = (ReferencingObject) referencingObjectAdapter.getObject();
        final Set<ReferencedObjectB> referencedObjects = referencingObject.getReferencedObjectBs();
        final ObjectAdapter referencedObjectsAdapter = getAdapterManager().getAdapterFor(referencedObjects);
        assertThat(referencedObjectsAdapter.getResolveState().isResolved(), is(true));
    }

    @Test
    public void shouldResolveReferencingObjectAndReferencedObjectsWhenThereAreSome() throws Exception {
        final ObjectAdapter adapter = loadAdapter();
        final ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        assertThat(referencingObject.getId(), is(seedReferencingObject.getId()));
        assertThat(referencingObject.getDescription(), is(seedReferencingObject.getDescription()));
        final Set<ReferencedObjectA> referencedObjects = referencingObject.getReferencedObjectAs();
        assertThat(referencedObjects.size(), is(Fixture.NUMBER_REFERENCED_OBJECTS));
        final Set<Serializable> primaryKeys = new HashSet<Serializable>();
        for (final ReferencedObjectA ro : referencedObjects) {
            primaryKeys.add(ro.getId());
        }
        for (int i = 0; i < Fixture.NUMBER_REFERENCED_OBJECTS; i++) {
            assertThat(primaryKeys.contains(referencedObjectPrefixPK + i), is(true));
        }
    }

    @Test
    public void canResolveReferencingObjectButReferencedObjectsIfEmptyCollection() throws Exception {
        final ObjectAdapter adapter = loadAdapter();
        final ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        final Set<ReferencedObjectB> referencedObjects = referencingObject.getReferencedObjectBs();
        assertThat(referencedObjects.size(), is(0));
    }

    @Test
    public void shouldMarkAdaptersOfReferencedObjectsAsResolved() throws Exception {
        final ObjectAdapter referencingObjectAdapter = loadAdapter();
        final ReferencingObject referencingObject = (ReferencingObject) referencingObjectAdapter.getObject();
        final Set<ObjectAdapter> adapterReferencedObjects = asAdapters(referencingObject.getReferencedObjectAs());
        for (final ObjectAdapter adapter : adapterReferencedObjects) {
            assertThat(adapter.isPersistent(), is(true));
            assertThat(adapter.getResolveState().isResolved(), is(true));
        }
    }

    @Test
    public void shouldTriggerUniqueOnPostLoadEventsForEachLoadedObject() throws Exception {
        @SuppressWarnings("unused") final ObjectAdapter adapter = loadAdapter();
        assertThat(getLoadPostEvents().size(), is(Fixture.NUMBER_REFERENCED_OBJECTS + 1));
        assertNotSame(getLoadPostEvents().get(0), getLoadPostEvents().get(1));
        assertNotSame(getLoadPostEvents().get(0), getLoadPostEvents().get(2));
        assertNotSame(getLoadPostEvents().get(0), getLoadPostEvents().get(3));
        assertNotSame(getLoadPostEvents().get(1), getLoadPostEvents().get(2));
        assertNotSame(getLoadPostEvents().get(1), getLoadPostEvents().get(3));
        assertNotSame(getLoadPostEvents().get(2), getLoadPostEvents().get(3));
    }
}
