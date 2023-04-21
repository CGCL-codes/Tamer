package org.starobjects.jpa.runtime.persistence.objectstore.load.simple;

import org.jmock.integration.junit4.JMock;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;

/**
 * Note that tests are in superclass.
 */
@RunWith(JMock.class)
public class GivenSimpleWhenResolveImmediatelyTest extends AbstractTestCase {

    protected NakedObject loadAdapter() {
        NakedObject adapter = retrieveObject(SimpleObject.class, simpleObjectPk);
        jpaObjectStore.resolveImmediately(adapter);
        return adapter;
    }
}
