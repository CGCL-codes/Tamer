package org.nakedobjects.nof.persist.objectstore;

import junit.framework.TestCase;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.nakedobjects.noa.adapter.ResolveState;
import org.nakedobjects.noa.reflect.NakedObjectField;
import org.nakedobjects.nof.persist.DummyPersistAlgorithm;
import org.nakedobjects.testing.TestSpecification;
import org.nakedobjects.testing.TestSystem;
import test.org.nakedobjects.object.MockNakedObject;
import test.org.nakedobjects.object.persistence.defaults.DummyOidGenerator;

public class LocalObjectManagerTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(LocalObjectManagerTest.class);
    }

    private ObjectStorePersistor persistor;

    private TestSpecification objectSpecification;

    private MockObjectStore objectStore;

    private MockNakedObject testNakedObject;

    private TestSystem system;

    protected void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        system = new TestSystem();
        persistor = new ObjectStorePersistor();
        objectStore = new MockObjectStore();
        persistor.setObjectStore(objectStore);
        persistor.setPersistAlgorithm(new DummyPersistAlgorithm());
        persistor.setOidGenerator(new DummyOidGenerator());
        system.setObjectPersistor(persistor);
        system.init();
        testNakedObject = new MockNakedObject();
        objectSpecification = new TestSpecification();
        testNakedObject.setupSpecification(objectSpecification);
    }

    protected void tearDown() throws Exception {
        system.shutdown();
    }

    public void testAbort() {
        persistor.startTransaction();
        persistor.destroyObject(testNakedObject);
        persistor.abortTransaction();
        objectStore.assertAction(0, "startTransaction");
        objectStore.assertAction(1, "destroyObject " + testNakedObject);
        objectStore.assertAction(2, "abortTransaction");
        objectStore.assertLastAction(2);
    }

    public void testEndTransaction() {
        persistor.startTransaction();
        persistor.destroyObject(testNakedObject);
        persistor.endTransaction();
        objectStore.assertAction(0, "startTransaction");
        objectStore.assertAction(1, "destroyObject " + testNakedObject);
        objectStore.assertAction(2, "endTransaction");
        objectStore.assertAction(3, "run DestroyObjectCommand " + testNakedObject);
    }

    public void testDestroy() {
        objectSpecification.fields = new NakedObjectField[0];
        persistor.startTransaction();
        persistor.destroyObject(testNakedObject);
        persistor.endTransaction();
        objectStore.assertAction(0, "startTransaction");
        objectStore.assertAction(1, "destroyObject " + testNakedObject);
        objectStore.assertAction(2, "endTransaction");
        objectStore.assertAction(3, "run DestroyObjectCommand " + testNakedObject);
        assertEquals(4, objectStore.getActions().size());
    }

    public void testMakePersistent() {
        objectSpecification.fields = new NakedObjectField[0];
        testNakedObject.setupResolveState(ResolveState.TRANSIENT);
        persistor.startTransaction();
        persistor.makePersistent(testNakedObject);
        persistor.endTransaction();
        objectStore.assertAction(0, "startTransaction");
        objectStore.assertAction(1, "createObject " + testNakedObject);
        objectStore.assertAction(2, "endTransaction");
        objectStore.assertAction(3, "run CreateObjectCommand " + testNakedObject);
        assertEquals(4, objectStore.getActions().size());
    }
}
