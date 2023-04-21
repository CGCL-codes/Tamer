package org.apache.isis.extensions.jpa.runtime.persistence.objectstore.mapSpec.idNonPublicGetter;

import org.apache.isis.extensions.jpa.runtime.persistence.objectstore.JpaObjectStoreAbstractTestCase;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class GivenObjectWithNonPublicIdWhenMapSpecsTest extends JpaObjectStoreAbstractTestCase {

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionIfMappedEntityDoesNotHaveAPublicIdProperty() throws Exception {
        setUpServicesAndOpenSession(new SimpleObjectRepository());
    }
}
