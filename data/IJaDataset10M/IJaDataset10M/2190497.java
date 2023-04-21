package org.apache.isis.extensions.jpa.runtime.persistence.objectstore.load.oneToOneEager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.spec.feature.ObjectAssociation;
import org.hibernate.proxy.LazyInitializer;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class GivenOneToOneBasicEagerNoJoinWhenResolveFieldTest extends Fixture {

    protected ObjectAdapter loadAdapter() {
        final ObjectAdapter adapter = retrieveObject(ReferencingObject.class, referencingObjectPK);
        getJpaObjectStore().resolveImmediately(adapter);
        return adapter;
    }

    @Test
    public void referencedObjectIsInitialized() throws Exception {
        final ObjectAdapter adapter = loadAdapter();
        final ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        final ReferencedObjectA referencedObjectA = referencingObject.getReferencedObjectA();
        assertThat(referencedObjectA, not(is(LazyInitializer.class)));
        final ObjectAdapter referencedObjectAdapter = getAdapterManager().adapterFor(referencedObjectA);
        assertThat(referencedObjectAdapter.getResolveState().isResolved(), is(true));
        final ObjectAssociation referencedObjectAAssociation = referencingObjectSpec.getAssociation("referencedObjectA");
        getJpaObjectStore().resolveField(adapter, referencedObjectAAssociation);
        assertThat(referencedObjectAdapter.getResolveState().isResolved(), is(true));
    }
}
