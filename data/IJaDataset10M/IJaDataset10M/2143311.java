package com.google.appengine.datanucleus.jpa;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.test.UnidirectionalOneToOneSubclassesJPA.SubChild;
import com.google.appengine.datanucleus.test.UnidirectionalOneToOneSubclassesJPA.SubParentWithSubChild;
import com.google.appengine.datanucleus.test.UnidirectionalOneToOneSubclassesJPA.SubParentWithSuperChild;
import com.google.appengine.datanucleus.test.UnidirectionalOneToOneSubclassesJPA.SuperChild;
import com.google.appengine.datanucleus.test.UnidirectionalOneToOneSubclassesJPA.SuperParentWithSubChild;
import com.google.appengine.datanucleus.test.UnidirectionalOneToOneSubclassesJPA.SuperParentWithSuperChild;

/**
 * @author Max Ross <max.ross@gmail.com>
 */
public class JPAUnidirectionalOneToOneSubclassTest extends JPATestCase {

    public void testSubParentWithSubChild() throws EntityNotFoundException {
        SubParentWithSubChild parent = new SubParentWithSubChild();
        parent.setSuperParentString("super parent string");
        parent.setSubParentString("sub parent string");
        SubChild subChild = new SubChild();
        subChild.setAString("a string");
        subChild.setBString("b string");
        parent.setSuperParentSubChild(subChild);
        beginTxn();
        em.persist(parent);
        commitTxn();
        Entity parentEntity = ds.get(KeyFactory.createKey(kindForClass(parent.getClass()), parent.getId()));
        Entity superParentSubChildEntity = ds.get(subChild.getId());
        assertEquals(3, parentEntity.getProperties().size());
        assertEquals("super parent string", parentEntity.getProperty("superParentString"));
        assertEquals("sub parent string", parentEntity.getProperty("subParentString"));
        assertEquals(superParentSubChildEntity.getKey(), parentEntity.getProperty("subChild_id"));
        assertEquals(2, superParentSubChildEntity.getProperties().size());
        assertEquals("a string", superParentSubChildEntity.getProperty("aString"));
        assertEquals("b string", superParentSubChildEntity.getProperty("bString"));
        beginTxn();
        parent = em.find(parent.getClass(), parent.getId());
        assertEquals("super parent string", parent.getSuperParentString());
        assertEquals("sub parent string", parent.getSubParentString());
        assertEquals(subChild.getId(), parent.getSuperParentSubChild().getId());
        commitTxn();
        beginTxn();
        subChild = em.find(subChild.getClass(), subChild.getId());
        assertEquals("a string", subChild.getAString());
        assertEquals("b string", subChild.getBString());
        commitTxn();
        beginTxn();
        em.remove(em.merge(parent));
        commitTxn();
        assertEquals(0, countForClass(parent.getClass()));
        assertEquals(0, countForClass(subChild.getClass()));
    }

    public void testSubParentWithSuperChild() throws EntityNotFoundException {
        SubParentWithSuperChild parent = new SubParentWithSuperChild();
        parent.setSuperParentString("super parent string");
        parent.setSubParentString("sub parent string");
        SuperChild superChild = new SuperChild();
        superChild.setAString("a string");
        parent.setSuperParentSuperChild(superChild);
        beginTxn();
        em.persist(parent);
        commitTxn();
        Entity parentEntity = ds.get(KeyFactory.createKey(kindForClass(parent.getClass()), parent.getId()));
        Entity superParentSuperChildEntity = ds.get(superChild.getId());
        assertEquals(3, parentEntity.getProperties().size());
        assertEquals("super parent string", parentEntity.getProperty("superParentString"));
        assertEquals("sub parent string", parentEntity.getProperty("subParentString"));
        assertEquals(superParentSuperChildEntity.getKey(), parentEntity.getProperty("superChild_id"));
        assertEquals(1, superParentSuperChildEntity.getProperties().size());
        assertEquals("a string", superParentSuperChildEntity.getProperty("aString"));
        beginTxn();
        parent = em.find(parent.getClass(), parent.getId());
        assertEquals("super parent string", parent.getSuperParentString());
        assertEquals("sub parent string", parent.getSubParentString());
        assertEquals(superChild.getId(), parent.getSuperParentSuperChild().getId());
        commitTxn();
        beginTxn();
        superChild = em.find(superChild.getClass(), superChild.getId());
        assertEquals("a string", superChild.getAString());
        commitTxn();
        beginTxn();
        em.remove(em.merge(parent));
        commitTxn();
        assertEquals(0, countForClass(parent.getClass()));
        assertEquals(0, countForClass(superChild.getClass()));
    }

    public void testSuperParentWithSuperChild() throws EntityNotFoundException {
        SuperParentWithSuperChild parent = new SuperParentWithSuperChild();
        parent.setSuperParentString("super parent string");
        SuperChild superChild = new SuperChild();
        superChild.setAString("a string");
        parent.setSuperParentSuperChild(superChild);
        beginTxn();
        em.persist(parent);
        commitTxn();
        Entity parentEntity = ds.get(KeyFactory.createKey(kindForClass(parent.getClass()), parent.getId()));
        Entity superParentSuperChildEntity = ds.get(superChild.getId());
        assertEquals(2, parentEntity.getProperties().size());
        assertEquals("super parent string", parentEntity.getProperty("superParentString"));
        assertEquals(superParentSuperChildEntity.getKey(), parentEntity.getProperty("superChild_id"));
        assertEquals(1, superParentSuperChildEntity.getProperties().size());
        assertEquals("a string", superParentSuperChildEntity.getProperty("aString"));
        beginTxn();
        parent = em.find(parent.getClass(), parent.getId());
        assertEquals("super parent string", parent.getSuperParentString());
        assertEquals(superChild.getId(), parent.getSuperParentSuperChild().getId());
        commitTxn();
        beginTxn();
        superChild = em.find(superChild.getClass(), superChild.getId());
        assertEquals("a string", superChild.getAString());
        commitTxn();
        beginTxn();
        em.remove(em.merge(parent));
        commitTxn();
        assertEquals(0, countForClass(parent.getClass()));
        assertEquals(0, countForClass(superChild.getClass()));
    }

    public void testSuperParentWithSubChild() throws EntityNotFoundException {
        SuperParentWithSubChild parent = new SuperParentWithSubChild();
        parent.setSuperParentString("super parent string");
        SubChild subChild = new SubChild();
        subChild.setAString("a string");
        subChild.setBString("b string");
        parent.setSuperParentSubChild(subChild);
        beginTxn();
        em.persist(parent);
        commitTxn();
        Entity parentEntity = ds.get(KeyFactory.createKey(kindForClass(parent.getClass()), parent.getId()));
        Entity superParentSubChildEntity = ds.get(subChild.getId());
        assertEquals(2, parentEntity.getProperties().size());
        assertEquals("super parent string", parentEntity.getProperty("superParentString"));
        assertEquals(superParentSubChildEntity.getKey(), parentEntity.getProperty("subChild_id"));
        assertEquals(2, superParentSubChildEntity.getProperties().size());
        assertEquals("a string", superParentSubChildEntity.getProperty("aString"));
        assertEquals("b string", superParentSubChildEntity.getProperty("bString"));
        beginTxn();
        parent = em.find(parent.getClass(), parent.getId());
        assertEquals("super parent string", parent.getSuperParentString());
        assertEquals(subChild.getId(), parent.getSuperParentSubChild().getId());
        commitTxn();
        beginTxn();
        subChild = em.find(subChild.getClass(), subChild.getId());
        assertEquals("a string", subChild.getAString());
        assertEquals("b string", subChild.getBString());
        commitTxn();
        beginTxn();
        em.remove(em.merge(parent));
        commitTxn();
        assertEquals(0, countForClass(parent.getClass()));
        assertEquals(0, countForClass(subChild.getClass()));
    }

    public void testWrongChildType() throws IllegalAccessException, InstantiationException {
        SuperParentWithSuperChild parent = new SuperParentWithSuperChild();
        parent.setSuperParentString("a string");
        Object child = SubChild.class.newInstance();
        parent.setSuperParentSuperChild((SuperChild) child);
        beginTxn();
        em.persist(parent);
        try {
            commitTxn();
            fail("expected exception");
        } catch (UnsupportedOperationException uoe) {
        }
    }

    public void testWrongChildType_Update() throws IllegalAccessException, InstantiationException {
        SuperParentWithSuperChild parent = new SuperParentWithSuperChild();
        parent.setSuperParentString("a string");
        beginTxn();
        em.persist(parent);
        commitTxn();
        beginTxn();
        parent = em.find(parent.getClass(), parent.getId());
        Object child = SubChild.class.newInstance();
        parent.setSuperParentSuperChild((SuperChild) child);
        try {
            commitTxn();
            fail("expected exception");
        } catch (UnsupportedOperationException uoe) {
        }
    }
}
