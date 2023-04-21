package org.apache.isis.extensions.jpa.runtime.persistence.objectstore.load.none;

import org.apache.isis.extensions.jpa.runtime.persistence.objectstore.JpaObjectStoreAbstractTestCase;
import org.apache.isis.extensions.jpa.runtime.persistence.oid.JpaOid;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.spec.ObjectSpecification;
import org.apache.isis.runtime.context.IsisContext;
import org.apache.isis.runtime.persistence.ObjectNotFoundException;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class GivenNonExistentWhenGetObjectTest extends JpaObjectStoreAbstractTestCase {

    @Test(expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionIfObjectDoesNotExist() throws Exception {
        setUpServicesAndOpenSession(new SimpleObjectRepository());
        final long primaryKey = 1001L;
        final JpaOid jpaOid = JpaOid.createPersistent(SimpleObject.class, primaryKey);
        final ObjectSpecification simpleObjectSpec = getJavaReflector().loadSpecification(SimpleObject.class);
        IsisContext.getTransactionManager().startTransaction();
        @SuppressWarnings("unused") final ObjectAdapter adapter = getJpaObjectStore().getObject(jpaOid, simpleObjectSpec);
        IsisContext.getTransactionManager().endTransaction();
    }
}
