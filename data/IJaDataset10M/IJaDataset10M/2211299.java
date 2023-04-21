package org.apache.isis.extensions.jpa.runtime.persistence.objectstore.load.simple;

import org.apache.isis.extensions.jpa.runtime.persistence.oid.JpaOid;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.jmock.integration.junit4.JMock;
import org.junit.runner.RunWith;

/**
 * Note that tests are in superclass.
 */
@RunWith(JMock.class)
public class GivenSimpleWhenGetObjectTest extends AbstractTestCase {

    @Override
    protected ObjectAdapter loadAdapter() {
        final JpaOid simpleObjectOid = JpaOid.createPersistent(SimpleObject.class, simpleObjectPk);
        return getJpaObjectStore().getObject(simpleObjectOid, simpleObjectSpec);
    }
}
