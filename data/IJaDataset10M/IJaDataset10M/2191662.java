package org.nakedobjects.plugins.htmlviewer.request;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.NakedObjectActionNoop;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.plugins.html.action.ActionException;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestProxyNakedCollection;
import org.nakedobjects.runtime.testsystem.TestProxyVersion;

public class ContextTest extends ProxyJunit3TestCase {

    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = new Context(null);
    }

    public void testExceptionThrownWhenNoCollectionForIdentity() {
        context.mapCollection(system.createPersistentTestCollection());
        try {
            assertNull(context.getMappedCollection("112"));
            fail();
        } catch (final ActionException expected) {
        }
    }

    public void testExceptionThrownWhenNoObjectForIdentity() {
        context.mapAction(new NakedObjectActionNoop());
        try {
            assertNull(context.getMappedAction("112"));
            fail();
        } catch (final ActionException expected) {
        }
    }

    public void testExceptionThrownWhenNoActionForIdentity() {
        final NakedObject object = system.createPersistentTestObject();
        context.mapObject(object);
        try {
            assertNull(context.getMappedObject("112"));
            fail();
        } catch (final ActionException expected) {
        }
    }

    public void testIdentityUsedToLookupObject() {
        final NakedObject object = system.createPersistentTestObject();
        final String id = context.mapObject(object);
        assertEquals(object, context.getMappedObject(id));
    }

    public void testLookedUpObjectHasDifferentVersion() {
        final NakedObject object = system.createPersistentTestObject();
        final String id = context.mapObject(object);
        object.setOptimisticLock(new TestProxyVersion(5));
        context.getMappedObject(id);
        assertEquals("Reloaded object " + object.titleString(), context.getMessage(1));
    }

    public void testIdentityUsedToLookupAction() {
        final NakedObjectActionNoop action = new NakedObjectActionNoop();
        final String id = context.mapAction(action);
        assertEquals(action, context.getMappedAction(id));
    }

    public void testRegisteredCollectionReturnSameIdentityForSameCollection() {
        final TestProxyNakedCollection collection = system.createPersistentTestCollection();
        final String id = context.mapCollection(collection);
        final String id2 = context.mapCollection(collection);
        assertEquals(id, id2);
    }

    public void testRegisteredObjectReturnSameIdentityForSameObject() {
        final NakedObject object = system.createPersistentTestObject();
        final String id = context.mapObject(object);
        final String id2 = context.mapObject(object);
        assertEquals(id, id2);
    }

    public void testTransientObjectReturnSameIdentityForSameObject() {
        final NakedObject object = system.createTransientTestObject();
        final String id = context.mapObject(object);
        final String id2 = context.mapObject(object);
        assertEquals(id, id2);
    }

    public void testRegisteredObjectReturnDifferentIdentityForDifferentObject() {
        final NakedObject dummyNakedObject = system.createPersistentTestObject();
        final NakedObject dummyNakedObject2 = system.createPersistentTestObject();
        final String id = context.mapObject(dummyNakedObject);
        final String id2 = context.mapObject(dummyNakedObject2);
        assertNotSame(id, id2);
    }

    public void testRegisteredActionReturnSameIdentityForSameAction() {
        final NakedObjectActionNoop action = new NakedObjectActionNoop();
        final String id = context.mapAction(action);
        final String id2 = context.mapAction(action);
        assertEquals(id, id2);
    }

    public void testRegisteredActionReturnDifferentIdentityForDifferentAction() {
        final String id = context.mapAction(new NakedObjectActionNoop());
        final String id2 = context.mapAction(new NakedObjectActionNoop());
        assertNotSame(id, id2);
    }

    public void testPersistentObjectsRestoredAsGhostToObjectLoader() {
        final NakedObject object = system.createPersistentTestObject();
        context.mapObject(object);
        final Oid oid = object.getOid();
        system.resetLoader();
        assertNull("loader still has the object", getAdapterManager().getAdapterFor(oid));
        context.restoreAllObjectsToLoader();
        assertNotNull("loaders is missing the object", getAdapterManager().getAdapterFor(oid));
        final NakedObject newAdapter = getAdapterManager().getAdapterFor(oid);
        assertNotSame("expect the loader to have a new adapter", object, newAdapter);
        assertEquals("expect oids to match", object.getOid(), newAdapter.getOid());
        assertNotSame(object.getObject(), newAdapter.getObject());
        assertEquals(object.getObject().getClass(), newAdapter.getObject().getClass());
        assertEquals("expect versions to match", object.getVersion(), newAdapter.getVersion());
        assertEquals(ResolveState.GHOST, newAdapter.getResolveState());
    }
}
