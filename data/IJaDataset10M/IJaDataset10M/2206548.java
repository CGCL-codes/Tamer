package org.starobjects.jpa.runtime.persistence.objectstore.load.oneToOneBidirLazyLazyFromFk;

import org.jmock.integration.junit4.JMock;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.starobjects.jpa.runtime.persistence.oid.JpaOid;

/**
 * Note that tests are in superclass.
 */
@RunWith(JMock.class)
public class GivenOneToOneBidirWithLazyLazyWhenGetObject extends AbstractTestCase {

    protected NakedObject loadAdapter() {
        JpaOid referencingObjectOid = JpaOid.createPersistent(ReferencingObject.class, referencingObjectPK);
        return jpaObjectStore.getObject(referencingObjectOid, referencingObjectSpec);
    }
}
