package org.hibernate.test.event.collection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import junit.framework.Test;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.event.AbstractCollectionEvent;
import org.hibernate.junit.functional.FunctionalTestCase;
import org.hibernate.junit.functional.FunctionalTestClassTestSuite;
import org.hibernate.test.event.collection.association.bidirectional.manytomany.ChildWithBidirectionalManyToMany;
import org.hibernate.test.event.collection.association.unidirectional.ParentWithCollectionOfEntities;

/**
 *
 * @author Gail Badner
 *
 * These tests are known to fail. When the functionality is corrected, the
 * corresponding method will be moved into AbstractCollectionEventTest.
 */
public class BrokenCollectionEventTest extends FunctionalTestCase {

    public BrokenCollectionEventTest(String string) {
        super(string);
    }

    public static Test suite() {
        return new FunctionalTestClassTestSuite(BrokenCollectionEventTest.class);
    }

    public String[] getMappings() {
        return new String[] { "event/collection/association/unidirectional/onetomany/UnidirectionalOneToManySetMapping.hbm.xml" };
    }

    public ParentWithCollection createParent(String name) {
        return new ParentWithCollectionOfEntities(name);
    }

    public Collection createCollection() {
        return new HashSet();
    }

    protected void cleanupTest() {
        ParentWithCollection dummyParent = createParent("dummyParent");
        dummyParent.setChildren(createCollection());
        Child dummyChild = dummyParent.addChild("dummyChild");
        Session s = openSession();
        Transaction tx = s.beginTransaction();
        List children = s.createCriteria(dummyChild.getClass()).list();
        List parents = s.createCriteria(dummyParent.getClass()).list();
        for (Iterator it = parents.iterator(); it.hasNext(); ) {
            ParentWithCollection parent = (ParentWithCollection) it.next();
            parent.clearChildren();
            s.delete(parent);
        }
        for (Iterator it = children.iterator(); it.hasNext(); ) {
            s.delete(it.next());
        }
        tx.commit();
        s.close();
    }

    public void testUpdateDetachedParentNoChildrenToNullFailureExpected() {
        CollectionListeners listeners = new CollectionListeners(getSessions());
        ParentWithCollection parent = createParentWithNoChildren("parent");
        listeners.clear();
        assertEquals(0, parent.getChildren().size());
        Session s = openSession();
        Transaction tx = s.beginTransaction();
        Collection oldCollection = parent.getChildren();
        parent.newChildren(null);
        s.update(parent);
        tx.commit();
        s.close();
        int index = 0;
        checkResult(listeners, listeners.getPreCollectionRemoveListener(), parent, oldCollection, index++);
        checkResult(listeners, listeners.getPostCollectionRemoveListener(), parent, oldCollection, index++);
        checkResult(listeners, listeners.getPreCollectionRecreateListener(), parent, index++);
        checkResult(listeners, listeners.getPostCollectionRecreateListener(), parent, index++);
        checkNumberOfResults(listeners, index);
    }

    public void testSaveParentNullChildrenFailureExpected() {
        CollectionListeners listeners = new CollectionListeners(getSessions());
        ParentWithCollection parent = createParentWithNullChildren("parent");
        assertNull(parent.getChildren());
        int index = 0;
        checkResult(listeners, listeners.getPreCollectionRecreateListener(), parent, index++);
        checkResult(listeners, listeners.getPostCollectionRecreateListener(), parent, index++);
        checkNumberOfResults(listeners, index);
        listeners.clear();
        Session s = openSession();
        Transaction tx = s.beginTransaction();
        parent = (ParentWithCollection) s.get(parent.getClass(), parent.getId());
        tx.commit();
        s.close();
        assertNotNull(parent.getChildren());
        checkNumberOfResults(listeners, 0);
    }

    public void testUpdateParentNoChildrenToNullFailureExpected() {
        CollectionListeners listeners = new CollectionListeners(getSessions());
        ParentWithCollection parent = createParentWithNoChildren("parent");
        listeners.clear();
        assertEquals(0, parent.getChildren().size());
        Session s = openSession();
        Transaction tx = s.beginTransaction();
        parent = (ParentWithCollection) s.get(parent.getClass(), parent.getId());
        Collection oldCollection = parent.getChildren();
        parent.newChildren(null);
        tx.commit();
        s.close();
        int index = 0;
        if (((PersistentCollection) oldCollection).wasInitialized()) {
            checkResult(listeners, listeners.getInitializeCollectionListener(), parent, oldCollection, index++);
        }
        checkResult(listeners, listeners.getPreCollectionRemoveListener(), parent, oldCollection, index++);
        checkResult(listeners, listeners.getPostCollectionRemoveListener(), parent, oldCollection, index++);
        checkResult(listeners, listeners.getPreCollectionRecreateListener(), parent, index++);
        checkResult(listeners, listeners.getPostCollectionRecreateListener(), parent, index++);
        checkNumberOfResults(listeners, index);
    }

    private ParentWithCollection createParentWithNullChildren(String parentName) {
        Session s = openSession();
        Transaction tx = s.beginTransaction();
        ParentWithCollection parent = createParent(parentName);
        s.save(parent);
        tx.commit();
        s.close();
        return parent;
    }

    private ParentWithCollection createParentWithNoChildren(String parentName) {
        Session s = openSession();
        Transaction tx = s.beginTransaction();
        ParentWithCollection parent = createParent(parentName);
        parent.setChildren(createCollection());
        s.save(parent);
        tx.commit();
        s.close();
        return parent;
    }

    private ParentWithCollection createParentWithOneChild(String parentName, String ChildName) {
        Session s = openSession();
        Transaction tx = s.beginTransaction();
        ParentWithCollection parent = createParent(parentName);
        parent.setChildren(createCollection());
        parent.addChild(ChildName);
        s.save(parent);
        tx.commit();
        s.close();
        return parent;
    }

    protected void checkResult(CollectionListeners listeners, CollectionListeners.Listener listenerExpected, ParentWithCollection parent, int index) {
        checkResult(listeners, listenerExpected, parent, parent.getChildren(), index);
    }

    protected void checkResult(CollectionListeners listeners, CollectionListeners.Listener listenerExpected, ChildWithBidirectionalManyToMany child, int index) {
        checkResult(listeners, listenerExpected, child, child.getParents(), index);
    }

    protected void checkResult(CollectionListeners listeners, CollectionListeners.Listener listenerExpected, Entity ownerExpected, Collection collExpected, int index) {
        assertSame(listenerExpected, listeners.getListenersCalled().get(index));
        assertSame(ownerExpected, ((AbstractCollectionEvent) listeners.getEvents().get(index)).getAffectedOwnerOrNull());
        assertEquals(ownerExpected.getId(), ((AbstractCollectionEvent) listeners.getEvents().get(index)).getAffectedOwnerIdOrNull());
        assertEquals(ownerExpected.getClass().getName(), ((AbstractCollectionEvent) listeners.getEvents().get(index)).getAffectedOwnerEntityName());
        assertSame(collExpected, ((AbstractCollectionEvent) listeners.getEvents().get(index)).getCollection());
    }

    private void checkNumberOfResults(CollectionListeners listeners, int nEventsExpected) {
        assertEquals(nEventsExpected, listeners.getListenersCalled().size());
        assertEquals(nEventsExpected, listeners.getEvents().size());
    }
}
