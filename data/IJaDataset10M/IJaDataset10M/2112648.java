package org.starobjects.jpa.runtime.persistence.objectstore.instances.has;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.starobjects.jpa.runtime.persistence.objectstore.instances.InstancesFixture;

public class GivenSimpleObjectWhenHasInstancesTest extends InstancesFixture {

    @Test
    public void thenShouldReturnTrue() throws Exception {
        assertThat(jpaObjectStore.hasInstances(simpleObjectASpec), is(true));
    }
}
