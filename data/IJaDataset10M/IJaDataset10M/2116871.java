package org.starobjects.jpa.runtime.persistence.objectstore.instances.get;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryFindAllInstances;
import org.starobjects.jpa.runtime.persistence.objectstore.instances.InstancesFixture;

public class GivenNoSimpleObjectBWhenGetInstances extends InstancesFixture {

    @Test
    public void andSearchForAllInstancesThenShouldFindNone() throws Exception {
        PersistenceQuery allCriteria = new PersistenceQueryFindAllInstances(simpleObjectBSpec);
        NakedObject[] instances = jpaObjectStore.getInstances(allCriteria);
        assertThat(instances.length, is(0));
    }
}
