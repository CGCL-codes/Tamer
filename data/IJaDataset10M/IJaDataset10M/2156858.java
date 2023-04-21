package com.google.appengine.datanucleus.jdo;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.TestUtils;
import com.google.appengine.datanucleus.Utils;
import com.google.appengine.datanucleus.test.Flight;
import com.google.appengine.datanucleus.test.HasKeyAncestorStringPkJDO;
import com.google.appengine.datanucleus.test.HasKeyPkJDO;
import com.google.appengine.datanucleus.test.HasMultiValuePropsJDO;
import com.google.appengine.datanucleus.test.HasStringAncestorStringPkJDO;
import com.google.appengine.datanucleus.test.HasVersionWithFieldJDO;
import com.google.appengine.datanucleus.test.Name;
import com.google.appengine.datanucleus.test.NullDataJDO;
import com.google.appengine.datanucleus.test.Person;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOHelper;
import javax.jdo.JDOOptimisticVerificationException;

/**
 * @author Erick Armbrust <earmbrust@google.com>
 * @author Max Ross <maxr@google.com>
 */
public class JDOUpdateTest extends JDOTestCase {

    private static final String DEFAULT_VERSION_PROPERTY_NAME = "VERSION";

    public void testSimpleUpdate() throws EntityNotFoundException {
        Key key = ds.put(Flight.newFlightEntity("1", "yam", "bam", 1, 2));
        String keyStr = KeyFactory.keyToString(key);
        beginTxn();
        Flight flight = pm.getObjectById(Flight.class, keyStr);
        assertEquals(keyStr, flight.getId());
        assertEquals("yam", flight.getOrigin());
        assertEquals("bam", flight.getDest());
        assertEquals("1", flight.getName());
        assertEquals(1, flight.getYou());
        assertEquals(2, flight.getMe());
        flight.setName("2");
        commitTxn();
        Entity flightCheck = ds.get(key);
        assertEquals("yam", flightCheck.getProperty("origin"));
        assertEquals("bam", flightCheck.getProperty("dest"));
        assertEquals("2", flightCheck.getProperty("name"));
        assertEquals(1L, flightCheck.getProperty("you"));
        assertEquals(2L, flightCheck.getProperty("me"));
        assertEquals(2L, flightCheck.getProperty(DEFAULT_VERSION_PROPERTY_NAME));
    }

    public void testSimpleUpdateWithNamedKey() throws EntityNotFoundException {
        Key key = ds.put(Flight.newFlightEntity("named key", "1", "yam", "bam", 1, 2));
        String keyStr = KeyFactory.keyToString(key);
        beginTxn();
        Flight flight = pm.getObjectById(Flight.class, keyStr);
        assertEquals(keyStr, flight.getId());
        assertEquals("yam", flight.getOrigin());
        assertEquals("bam", flight.getDest());
        assertEquals("1", flight.getName());
        assertEquals(1, flight.getYou());
        assertEquals(2, flight.getMe());
        flight.setName("2");
        commitTxn();
        Entity flightCheck = ds.get(key);
        assertEquals("yam", flightCheck.getProperty("origin"));
        assertEquals("bam", flightCheck.getProperty("dest"));
        assertEquals("2", flightCheck.getProperty("name"));
        assertEquals(1L, flightCheck.getProperty("you"));
        assertEquals(2L, flightCheck.getProperty("me"));
        assertEquals(2L, flightCheck.getProperty(DEFAULT_VERSION_PROPERTY_NAME));
        assertEquals("named key", flightCheck.getKey().getName());
    }

    public void testOptimisticLocking_Update_NoField() {
        switchDatasource(PersistenceManagerFactoryName.nontransactional);
        Entity flightEntity = Flight.newFlightEntity("1", "yam", "bam", 1, 2);
        Key key = ds.put(flightEntity);
        String keyStr = KeyFactory.keyToString(key);
        beginTxn();
        Flight flight = pm.getObjectById(Flight.class, keyStr);
        flight.setName("2");
        flightEntity.setProperty(DEFAULT_VERSION_PROPERTY_NAME, 2L);
        ds.put(flightEntity);
        try {
            commitTxn();
            fail("expected optimistic exception");
        } catch (JDOOptimisticVerificationException jove) {
        }
    }

    public void testOptimisticLocking_Attach_NoField() {
        switchDatasource(PersistenceManagerFactoryName.nontransactional);
        Entity flightEntity = Flight.newFlightEntity("1", "yam", "bam", 1, 2);
        Key key = ds.put(flightEntity);
        String keyStr = KeyFactory.keyToString(key);
        beginTxn();
        Flight flight = pm.detachCopy(pm.getObjectById(Flight.class, keyStr));
        commitTxn();
        beginTxn();
        flight.setName("2");
        pm.makePersistent(flight);
        flightEntity.setProperty(DEFAULT_VERSION_PROPERTY_NAME, 2L);
        ds.put(flightEntity);
        try {
            commitTxn();
            fail("expected optimistic exception");
        } catch (JDOOptimisticVerificationException jove) {
        }
    }

    public void testOptimisticLocking_Delete_NoField() {
        switchDatasource(PersistenceManagerFactoryName.nontransactional);
        Entity flightEntity = Flight.newFlightEntity("1", "yam", "bam", 1, 2);
        Key key = ds.put(flightEntity);
        String keyStr = KeyFactory.keyToString(key);
        beginTxn();
        Flight flight = pm.getObjectById(Flight.class, keyStr);
        flight.setName("2");
        flightEntity.setProperty(DEFAULT_VERSION_PROPERTY_NAME, 2L);
        ds.delete(key);
        try {
            commitTxn();
            fail("expected optimistic exception");
        } catch (JDOOptimisticVerificationException jove) {
        }
    }

    public void testOptimisticLocking_Update_HasVersionField() {
        switchDatasource(PersistenceManagerFactoryName.nontransactional);
        Entity entity = new Entity(HasVersionWithFieldJDO.class.getSimpleName());
        entity.setProperty(DEFAULT_VERSION_PROPERTY_NAME, 1L);
        Key key = ds.put(entity);
        String keyStr = KeyFactory.keyToString(key);
        beginTxn();
        HasVersionWithFieldJDO hvwf = pm.getObjectById(HasVersionWithFieldJDO.class, keyStr);
        hvwf.setValue("value");
        commitTxn();
        beginTxn();
        hvwf = pm.getObjectById(HasVersionWithFieldJDO.class, keyStr);
        assertEquals(2L, hvwf.getVersion());
        entity.setProperty(DEFAULT_VERSION_PROPERTY_NAME, 3L);
        hvwf.setValue("another value");
        entity.setProperty(DEFAULT_VERSION_PROPERTY_NAME, 7L);
        ds.put(entity);
        try {
            commitTxn();
            fail("expected optimistic exception");
        } catch (JDOOptimisticVerificationException jove) {
        }
        assertEquals(2L, JDOHelper.getVersion(hvwf));
    }

    public void testOptimisticLocking_Delete_HasVersionField() {
        switchDatasource(PersistenceManagerFactoryName.nontransactional);
        Entity entity = new Entity(HasVersionWithFieldJDO.class.getSimpleName());
        entity.setProperty(DEFAULT_VERSION_PROPERTY_NAME, 1L);
        Key key = ds.put(entity);
        String keyStr = KeyFactory.keyToString(key);
        beginTxn();
        HasVersionWithFieldJDO hvwf = pm.getObjectById(HasVersionWithFieldJDO.class, keyStr);
        ds.delete(key);
        hvwf.setValue("value");
        try {
            commitTxn();
            fail("expected optimistic exception");
        } catch (JDOOptimisticVerificationException jove) {
        }
        assertEquals(1L, JDOHelper.getVersion(hvwf));
    }

    public void testNonTransactionalUpdate() throws EntityNotFoundException {
        switchDatasource(PersistenceManagerFactoryName.nontransactional);
        Key key = ds.put(Flight.newFlightEntity("1", "yam", "bam", 1, 2));
        Flight f = pm.getObjectById(Flight.class, KeyFactory.keyToString(key));
        f.setYou(77);
        pm.close();
        Entity flightEntity = ds.get(key);
        assertEquals(77L, flightEntity.getProperty("you"));
        pm = pmf.getPersistenceManager();
    }

    public void testChangeStringPK_SetNonKeyString() throws EntityNotFoundException {
        Key key = ds.put(Flight.newFlightEntity("named key", "1", "yam", "bam", 1, 2));
        String keyStr = KeyFactory.keyToString(key);
        beginTxn();
        Flight flight = pm.getObjectById(Flight.class, keyStr);
        assertEquals(keyStr, flight.getId());
        assertEquals("yam", flight.getOrigin());
        assertEquals("bam", flight.getDest());
        assertEquals("1", flight.getName());
        assertEquals(1, flight.getYou());
        assertEquals(2, flight.getMe());
        flight.setName("2");
        flight.setId("foo");
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
        Entity flightCheck = ds.get(key);
        assertEquals("yam", flightCheck.getProperty("origin"));
        assertEquals("bam", flightCheck.getProperty("dest"));
        assertEquals("1", flightCheck.getProperty("name"));
        assertEquals(1L, flightCheck.getProperty("you"));
        assertEquals(2L, flightCheck.getProperty("me"));
        assertEquals(1L, flightCheck.getProperty(DEFAULT_VERSION_PROPERTY_NAME));
        assertEquals("named key", flightCheck.getKey().getName());
    }

    public void testChangeStringPK_SetNull() throws EntityNotFoundException {
        Key key = ds.put(Flight.newFlightEntity("1", "yam", "bam", 1, 2));
        beginTxn();
        Flight f = pm.getObjectById(Flight.class, KeyFactory.keyToString(key));
        f.setId(null);
        pm.makePersistent(f);
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
        assertEquals(1, countForClass(Flight.class));
    }

    public void testChangePK_SetKeyString() throws EntityNotFoundException {
        Key key = ds.put(Flight.newFlightEntity("1", "yam", "bam", 1, 2));
        beginTxn();
        Flight f = pm.getObjectById(Flight.class, KeyFactory.keyToString(key));
        f.setId(KeyFactory.keyToString(KeyFactory.createKey(Flight.class.getSimpleName(), "yar")));
        f.setYou(77);
        pm.makePersistent(f);
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
        assertEquals(1, countForClass(Flight.class));
        Entity e = ds.get(key);
        assertEquals(1L, e.getProperty("you"));
        assertEquals(1, countForClass(Flight.class));
    }

    public void testChangeKeyPK_SetDifferentKey() throws EntityNotFoundException {
        Key key = ds.put(new Entity(HasKeyPkJDO.class.getSimpleName()));
        beginTxn();
        HasKeyPkJDO pojo = pm.getObjectById(HasKeyPkJDO.class, key);
        pojo.setKey(KeyFactory.createKey(HasKeyPkJDO.class.getSimpleName(), 33));
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
        assertEquals(1, countForClass(HasKeyPkJDO.class));
    }

    public void testChangeKeyPK_SetNull() throws EntityNotFoundException {
        Key key = ds.put(new Entity(HasKeyPkJDO.class.getSimpleName()));
        beginTxn();
        HasKeyPkJDO pojo = pm.getObjectById(HasKeyPkJDO.class, key);
        pojo.setKey(null);
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
        assertEquals(1, countForClass(HasKeyPkJDO.class));
    }

    public void testUpdateList_Add() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        List<String> list = Utils.newArrayList("a", "b");
        pojo.setStrList(list);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        pojo.getStrList().add("zoom");
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strList")).size());
    }

    public void testUpdateList_Reset() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        List<String> list = Utils.newArrayList("a", "b");
        pojo.setStrList(list);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        list = Utils.newArrayList("a", "b", "zoom");
        pojo.setStrList(list);
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strList")).size());
    }

    public void testUpdateCollection_Add() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        List<Integer> list = Utils.newArrayList(2, 3);
        pojo.setIntColl(list);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        pojo.getIntColl().add(4);
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("intColl")).size());
    }

    public void testUpdateCollection_Reset() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        List<Integer> list = Utils.newArrayList(2, 3);
        pojo.setIntColl(list);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        list = Utils.newArrayList(2, 3, 4);
        pojo.setIntColl(list);
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("intColl")).size());
    }

    public void testUpdateArrayList_Add() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        ArrayList<String> list = Utils.newArrayList("a", "b");
        pojo.setStrArrayList(list);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        pojo.getStrArrayList().add("zoom");
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strArrayList")).size());
    }

    public void testUpdateArrayList_Reset() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        ArrayList<String> list = Utils.newArrayList("a", "b");
        pojo.setStrArrayList(list);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        list = Utils.newArrayList("a", "b", "zoom");
        pojo.setStrArrayList(list);
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strArrayList")).size());
    }

    public void testUpdateLinkedList_Add() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        LinkedList<String> list = Utils.newLinkedList("a", "b");
        pojo.setStrLinkedList(list);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        pojo.getStrLinkedList().add("zoom");
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strLinkedList")).size());
    }

    public void testUpdateLinkedList_Reset() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        LinkedList<String> list = Utils.newLinkedList("a", "b");
        pojo.setStrLinkedList(list);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        list = Utils.newLinkedList("a", "b", "zoom");
        pojo.setStrLinkedList(list);
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strLinkedList")).size());
    }

    public void testUpdateSet_Add() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        Set<String> set = Utils.newHashSet("a", "b");
        pojo.setStrSet(set);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        pojo.getStrSet().add("zoom");
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strSet")).size());
    }

    public void testUpdateSet_Reset() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        Set<String> set = Utils.newHashSet("a", "b");
        pojo.setStrSet(set);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        set = Utils.newHashSet("a", "b", "zoom");
        pojo.setStrSet(set);
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strSet")).size());
    }

    public void testUpdateHashSet_Add() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        HashSet<String> set = Utils.newHashSet("a", "b");
        pojo.setStrHashSet(set);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        pojo.getStrHashSet().add("zoom");
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strHashSet")).size());
    }

    public void testUpdateHashSet_Reset() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        HashSet<String> set = Utils.newHashSet("a", "b");
        pojo.setStrHashSet(set);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        set = Utils.newHashSet("a", "b", "zoom");
        pojo.setStrHashSet(set);
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strHashSet")).size());
    }

    public void testUpdateLinkedHashSet_Add() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        LinkedHashSet<String> set = Utils.newLinkedHashSet("a", "b");
        pojo.setStrLinkedHashSet(set);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        pojo.getStrLinkedHashSet().add("zoom");
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strLinkedHashSet")).size());
    }

    public void testUpdateLinkedHashSet_Reset() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        LinkedHashSet<String> set = Utils.newLinkedHashSet("a", "b");
        pojo.setStrLinkedHashSet(set);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        set = Utils.newLinkedHashSet("a", "b", "zoom");
        pojo.setStrLinkedHashSet(set);
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strLinkedHashSet")).size());
    }

    public void testUpdateTreeSet_Add() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        TreeSet<String> set = Utils.newTreeSet("a", "b");
        pojo.setStrTreeSet(set);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        pojo.getStrTreeSet().add("zoom");
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strTreeSet")).size());
    }

    public void testUpdateTreeSet_Reset() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        TreeSet<String> set = Utils.newTreeSet("a", "b");
        pojo.setStrTreeSet(set);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        set = Utils.newTreeSet("a", "b", "zoom");
        pojo.setStrTreeSet(set);
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strTreeSet")).size());
    }

    public void testUpdateSortedSet_Add() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        SortedSet<String> set = Utils.newTreeSet("a", "b");
        pojo.setStrSortedSet(set);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        pojo.getStrSortedSet().add("zoom");
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strSortedSet")).size());
    }

    public void testUpdateSortedSet_Reset() throws EntityNotFoundException {
        HasMultiValuePropsJDO pojo = new HasMultiValuePropsJDO();
        SortedSet<String> set = Utils.newTreeSet("a", "b");
        pojo.setStrSortedSet(set);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(HasMultiValuePropsJDO.class, pojo.getId());
        set = Utils.newTreeSet("a", "b", "zoom");
        pojo.setStrSortedSet(set);
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("strSortedSet")).size());
    }

    public void testUpdateArray_Reset() throws EntityNotFoundException {
        NullDataJDO pojo = new NullDataJDO();
        String[] array = new String[] { "a", "b" };
        pojo.setArray(array);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(NullDataJDO.class, pojo.getId());
        array = new String[] { "a", "b", "c" };
        pojo.setArray(array);
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals(3, ((List<?>) e.getProperty("array")).size());
    }

    public void xxxtestUpdateArray_ModifyExistingElement() throws EntityNotFoundException {
        NullDataJDO pojo = new NullDataJDO();
        String[] array = new String[] { "a", "b" };
        pojo.setArray(array);
        beginTxn();
        pm.makePersistent(pojo);
        commitTxn();
        pm.close();
        pm = pmf.getPersistenceManager();
        beginTxn();
        pojo = pm.getObjectById(NullDataJDO.class, pojo.getId());
        pojo.getArray()[0] = "c";
        pojo.setArray(array);
        commitTxn();
        Entity e = ds.get(TestUtils.createKey(pojo, pojo.getId()));
        assertEquals("c", ((List<?>) e.getProperty("array")).get(0));
    }

    public void testEmbeddable() throws EntityNotFoundException {
        Person p = new Person();
        p.setName(new Name());
        p.getName().setFirst("jimmy");
        p.getName().setLast("jam");
        p.setAnotherName(new Name());
        p.getAnotherName().setFirst("anotherjimmy");
        p.getAnotherName().setLast("anotherjam");
        makePersistentInTxn(p, TXN_START_END);
        assertNotNull(p.getId());
        beginTxn();
        p = pm.getObjectById(Person.class, p.getId());
        p.getName().setLast("not jam");
        p.getName().setFirst("not jimmy");
        commitTxn();
        Entity entity = ds.get(TestUtils.createKey(p, p.getId()));
        assertNotNull(entity);
        assertEquals("not jimmy", entity.getProperty("first"));
        assertEquals("not jam", entity.getProperty("last"));
        assertEquals("anotherjimmy", entity.getProperty("anotherFirst"));
        assertEquals("anotherjam", entity.getProperty("anotherLast"));
    }

    public void testUpdateStrPrimaryKey_SetNewName() {
        Key key = ds.put(Flight.newFlightEntity("name", "bos", "mia", 3, 4, 44));
        beginTxn();
        Flight f = pm.getObjectById(Flight.class, key.getId());
        f.setId("other");
        pm.makePersistent(f);
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
    }

    public void testUpdateStrPrimaryKey_SetNewKey() {
        Key key = ds.put(Flight.newFlightEntity("name", "bos", "mia", 3, 4, 44));
        beginTxn();
        Flight f = pm.getObjectById(Flight.class, key.getId());
        f.setId(KeyFactory.keyToString(KeyFactory.createKey(key.getKind(), "jimmy")));
        pm.makePersistent(f);
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
    }

    public void testUpdateStrPrimaryKey_NullKey() {
        Key key = ds.put(Flight.newFlightEntity("name", "bos", "mia", 3, 4, 44));
        beginTxn();
        Flight f = pm.getObjectById(Flight.class, key.getId());
        f.setId(null);
        pm.makePersistent(f);
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
    }

    public void testUpdateStrAncestor_SetNewName() {
        Key parentKey = ds.put(new Entity(String.class.getSimpleName()));
        Key key = ds.put(new Entity(HasStringAncestorStringPkJDO.class.getSimpleName(), parentKey));
        beginTxn();
        HasStringAncestorStringPkJDO pojo = pm.getObjectById(HasStringAncestorStringPkJDO.class, KeyFactory.keyToString(key));
        pojo.setAncestorId("other");
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
    }

    public void testUpdateStrAncestor_SetNewKey() {
        Key parentKey = ds.put(new Entity(Flight.class.getSimpleName()));
        Key key = ds.put(new Entity(HasStringAncestorStringPkJDO.class.getSimpleName(), parentKey));
        beginTxn();
        HasStringAncestorStringPkJDO pojo = pm.getObjectById(HasStringAncestorStringPkJDO.class, KeyFactory.keyToString(key));
        pojo.setAncestorId(KeyFactory.keyToString(KeyFactory.createKey(key.getKind(), 33)));
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
    }

    public void testUpdateStrAncestor_NullKey() {
        Key parentKey = ds.put(new Entity(Flight.class.getSimpleName()));
        Key key = ds.put(new Entity(HasStringAncestorStringPkJDO.class.getSimpleName(), parentKey));
        beginTxn();
        HasStringAncestorStringPkJDO pojo = pm.getObjectById(HasStringAncestorStringPkJDO.class, KeyFactory.keyToString(key));
        pojo.setAncestorId(null);
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
    }

    public void testUpdateKeyAncestor_SetNewKey() {
        Key parentKey = ds.put(new Entity(Flight.class.getSimpleName()));
        Key key = ds.put(new Entity(HasKeyAncestorStringPkJDO.class.getSimpleName(), parentKey));
        beginTxn();
        HasKeyAncestorStringPkJDO pojo = pm.getObjectById(HasKeyAncestorStringPkJDO.class, KeyFactory.keyToString(key));
        pojo.setAncestorKey(KeyFactory.createKey(key.getKind(), 33));
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
    }

    public void testUpdateKeyAncestor_NullKey() {
        Key parentKey = ds.put(new Entity(Flight.class.getSimpleName()));
        Key key = ds.put(new Entity(HasKeyAncestorStringPkJDO.class.getSimpleName(), parentKey));
        beginTxn();
        HasKeyAncestorStringPkJDO pojo = pm.getObjectById(HasKeyAncestorStringPkJDO.class, KeyFactory.keyToString(key));
        pojo.setAncestorKey(null);
        try {
            commitTxn();
            fail("expected exception");
        } catch (JDOFatalUserException e) {
            rollbackTxn();
        }
    }
}
