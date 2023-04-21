package org.nakedobjects.runtime.persistence.objectstore;

import static org.junit.Assert.assertEquals;
import java.util.Collections;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.services.ServicesInjectorDefault;
import org.nakedobjects.metamodel.services.container.DomainObjectContainerDefault;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManagerExtended;
import org.nakedobjects.runtime.persistence.internal.RuntimeContextFromSession;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactoryBasic;
import org.nakedobjects.runtime.persistence.objectstore.algorithm.dummy.DummyPersistAlgorithm;
import org.nakedobjects.runtime.persistence.objectstore.transaction.ObjectStoreTransactionManager;
import org.nakedobjects.runtime.testsystem.TestProxyOidGenerator;
import org.nakedobjects.runtime.testsystem.TestProxySystem;

@RunWith(JMock.class)
public class PersistenceSessionObjectStoreTest {

    private Mockery mockery = new JUnit4Mockery();

    private PersistenceSessionFactory mockPersistenceSessionFactory;

    private PersistenceSessionObjectStore persistenceSession;

    private ObjectStoreTransactionManager transactionManager;

    private ObjectStoreSpy objectStore;

    private NakedObject testNakedObject;

    private TestProxySystem system;

    private ServicesInjectorDefault servicesInjector;

    private AdapterManagerExtended adapterManager;

    private AdapterFactory adapterFactory;

    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        mockPersistenceSessionFactory = mockery.mock(PersistenceSessionFactory.class);
        system = new TestProxySystem();
        objectStore = new ObjectStoreSpy();
        RuntimeContextFromSession runtimeContext = new RuntimeContextFromSession();
        DomainObjectContainerDefault container = new DomainObjectContainerDefault();
        runtimeContext.injectInto(container);
        runtimeContext.setContainer(container);
        servicesInjector = new ServicesInjectorDefault();
        servicesInjector.setContainer(container);
        adapterManager = (AdapterManagerExtended) system.getAdapterManager();
        adapterFactory = system.getAdapterFactory();
        persistenceSession = new PersistenceSessionObjectStore(mockPersistenceSessionFactory, adapterFactory, new ObjectFactoryBasic(), servicesInjector, new TestProxyOidGenerator(), adapterManager, new DummyPersistAlgorithm(), objectStore);
        transactionManager = new ObjectStoreTransactionManager(persistenceSession, objectStore);
        transactionManager.injectInto(persistenceSession);
        servicesInjector.setServices(Collections.emptyList());
        persistenceSession.setSpecificationLoader(system.getReflector());
        system.setPersistenceSession(persistenceSession);
        system.init();
        testNakedObject = system.createPersistentTestObject();
    }

    @After
    public void tearDown() throws Exception {
        system.shutdown();
    }

    @Test
    public void testAbort() {
        objectStore.reset();
        transactionManager.startTransaction();
        persistenceSession.destroyObject(testNakedObject);
        transactionManager.abortTransaction();
        objectStore.assertAction(0, "startTransaction");
        objectStore.assertAction(1, "destroyObject " + testNakedObject);
        objectStore.assertAction(2, "abortTransaction");
        objectStore.assertLastAction(2);
    }

    @Test
    public void testDestroy() {
        objectStore.reset();
        final String action = "destroyObject " + testNakedObject;
        transactionManager.startTransaction();
        persistenceSession.destroyObject(testNakedObject);
        transactionManager.endTransaction();
        objectStore.assertAction(0, "startTransaction");
        objectStore.assertAction(1, action);
        objectStore.assertAction(2, "execute DestroyObjectCommand " + testNakedObject);
        objectStore.assertAction(3, "endTransaction");
        assertEquals(4, objectStore.getActions().size());
    }

    public void testMakePersistent() {
        testNakedObject = system.createTransientTestObject();
        objectStore.reset();
        transactionManager.startTransaction();
        persistenceSession.makePersistent(testNakedObject);
        transactionManager.endTransaction();
        objectStore.assertAction(0, "startTransaction");
        objectStore.assertAction(1, "createObject " + testNakedObject);
        objectStore.assertAction(2, "endTransaction");
        objectStore.assertAction(3, "run CreateObjectCommand " + testNakedObject);
        assertEquals(4, objectStore.getActions().size());
    }
}
