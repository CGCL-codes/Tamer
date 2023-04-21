package org.apache.isis.extensions.jpa.runtime.persistence.objectstore.load.none;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class GivenServiceWhenResolveImmediatelyTest extends Fixture {

    @Test
    public void shouldIgnoreRequestsToResolveServiceObjects() throws Exception {
        final ObjectAdapter serviceAdapter = getAdapterManager().getAdapterFor(simpleObjectRepository);
        assertThat(getLoadPostEvents().size(), is(0));
        getJpaObjectStore().resolveImmediately(serviceAdapter);
        assertThat(getLoadPostEvents().size(), is(0));
    }
}
