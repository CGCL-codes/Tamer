package org.starobjects.jpa.runtime.persistence.objectstore.load.manyToOneEager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import org.hibernate.proxy.LazyInitializer;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;

@RunWith(JMock.class)
public class GivenManyToOneBasicEagerNoJoinWhenResolveField extends Fixture {

    protected NakedObject loadAdapter() {
        NakedObject adapter = retrieveObject(ReferencingObject.class, referencingObjectPK);
        jpaObjectStore.resolveImmediately(adapter);
        return adapter;
    }

    @Test
    public void referencedObjectIsInitialized() throws Exception {
        NakedObject adapter = loadAdapter();
        ReferencingObject referencingObject = (ReferencingObject) adapter.getObject();
        ReferencedObjectA referencedObjectA = referencingObject.getReferencedObjectA();
        assertThat(referencedObjectA, not(is(LazyInitializer.class)));
        NakedObject referencedObjectAdapter = getAdapterManager().adapterFor(referencedObjectA);
        assertThat(referencedObjectAdapter.getResolveState().isResolved(), is(true));
        NakedObjectAssociation referencedObjectAAssociation = referencingObjectSpec.getAssociation("referencedObjectA");
        jpaObjectStore.resolveField(adapter, referencedObjectAAssociation);
        assertThat(referencedObjectAdapter.getResolveState().isResolved(), is(true));
    }
}
