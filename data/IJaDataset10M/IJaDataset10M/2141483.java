package org.apache.isis.extensions.jpa.runtime.persistence.objectstore.load.manyToOneEagerJoin;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.spec.feature.ObjectAssociation;
import org.hibernate.proxy.LazyInitializer;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class GivenManyToOneWithEagerJoinWhenResolveFieldTest extends Fixture {

    protected ObjectAdapter loadAdapter() {
        final ObjectAdapter adapter = retrieveObject(ReferencingObject.class, referencingObjectPK);
        getJpaObjectStore().resolveImmediately(adapter);
        return adapter;
    }

    @Test
    public void referencedObjectIsInitialized() throws Exception {
        final ObjectAdapter adapter = loadAdapter();
        final ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        final ReferencedObjectA referencedObject = referencingObject.getReferencedObjectA();
        assertThat(referencedObject, not(is(LazyInitializer.class)));
        final ObjectAdapter referencedObjectAdapter = getAdapterManager().adapterFor(referencedObject);
        assertThat(referencedObjectAdapter.getResolveState().isResolved(), is(true));
        final ObjectAssociation referencedObjectAssociation = referencingObjectSpec.getAssociation("referencedObjectA");
        getJpaObjectStore().resolveField(adapter, referencedObjectAssociation);
        assertThat(referencedObjectAdapter.getResolveState().isResolved(), is(true));
    }

    @Test
    public void nullReferencedObjectIsNull() throws Exception {
        final ObjectAdapter adapter = loadAdapter();
        final ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        final ReferencedObjectB referencedObject = referencingObject.getReferencedObjectB();
        assertThat(referencedObject, is(nullValue()));
        final ObjectAssociation referencedObjectAssociation = referencingObjectSpec.getAssociation("referencedObjectB");
        getJpaObjectStore().resolveField(adapter, referencedObjectAssociation);
        assertThat(referencedObject, is(nullValue()));
    }
}
