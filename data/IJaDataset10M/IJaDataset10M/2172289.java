package org.happy.collections.ver1x0.lists.decorators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.happy.collections.decorators.CacheCollection_1x0;
import org.happy.collections.lists.decorators.CacheList_1x0;
import org.happy.commons.util.ArraysHelper;
import org.happy.commons.util.Arrays_1x0;
import org.happy.commons.util.Random_1x0;
import org.junit.Before;
import org.junit.Test;

public class CacheList_1x0Test {

    protected List<String> referenceList1, referenceList2, referenceList3;

    protected Set<String> referenceSet1, referenceSet2, referenceSet3;

    @Before
    public void setUp() {
        System.out.println("-------------------------------setUp() - Test-------------------------------");
        referenceList1 = new ArrayList<String>();
        referenceList1.add("0");
        referenceList1.add("1");
        referenceList1.add("2");
        referenceList1.add("3");
        referenceList1.add("4");
        referenceList1.add("5");
        referenceList1.add("6");
        referenceList1.add("7");
        referenceList1.add("8");
        referenceList1.add("9");
        referenceList2 = new ArrayList<String>();
        referenceList2.add("foobar");
        referenceList3 = new ArrayList<String>(referenceList1);
        Collections.shuffle((List<String>) referenceList3);
        referenceSet1 = new HashSet<String>(referenceList1);
        referenceSet2 = new HashSet<String>(referenceList2);
        referenceSet3 = new HashSet<String>(referenceList3);
    }

    private CacheCollection_1x0<String> initCollection() {
        CacheCollection_1x0<String> c = new CacheCollection_1x0<String>(0.1F, new ArrayList<String>());
        c = buildTestCollection();
        c.addAll(referenceList3);
        return c;
    }

    private CacheCollection_1x0<String> buildTestCollection() {
        return new CacheCollection_1x0<String>(0.1F, new ArrayList<String>());
    }

    @Test
    public void testEqualsBasic() {
        System.out.println("-------------------------------testEqualsBasic() - Test-------------------------------");
        CacheCollection_1x0<String> collection = initCollection();
        assertFalse("equals(null) is not false", collection.equals(null));
        System.out.println("collection: " + Arrays_1x0.toString(collection.toArray()));
        assertTrue("equals() is not reflective", collection.equals(collection));
        try {
            assertFalse("equals(new Object()) is true !", collection.equals(new Object()));
            collection.equals(referenceList1);
            collection.equals(referenceSet1);
            collection.equals(new Integer(1));
        } catch (ClassCastException eP) {
            fail("equals() is not type safe");
        }
    }

    @Test
    public void testEqualsSymmetry() {
        System.out.println("-------------------------------testEqualsSymmetry() - Test-------------------------------");
        CacheCollection_1x0<String> collection = initCollection();
        testEqualsSymmetry(collection, referenceList1);
        testEqualsSymmetry(collection, referenceList2);
        testEqualsSymmetry(collection, referenceList3);
        testEqualsSymmetry(collection, referenceSet1);
        testEqualsSymmetry(collection, referenceSet2);
        testEqualsSymmetry(collection, referenceSet3);
    }

    protected void testEqualsSymmetry(Collection<String> collection1P, Collection<String> collection2P) {
        System.out.println("-------------------------------testEqualsSymmetry(..) - Test-------------------------------");
        if (collection1P.equals(collection2P)) {
            if (!collection2P.equals(collection1P)) {
                fail("equals() is not symmetric");
            }
        } else {
            if (collection2P.equals(collection1P)) {
                fail("equals() is not symmetric");
            }
        }
    }

    @Test
    public void testContains() {
        System.out.println("-------------------------------testContains() - Test-------------------------------");
        CacheCollection_1x0<String> collection = initCollection();
        assertTrue(collection.contains("0"));
        assertTrue(collection.contains("1"));
        assertTrue(collection.contains("2"));
        assertTrue(collection.contains("3"));
        assertTrue(collection.contains("4"));
        assertTrue(collection.contains("5"));
        assertTrue(collection.contains("6"));
        assertTrue(collection.contains("7"));
        assertTrue(collection.contains("8"));
        assertTrue(collection.contains("9"));
    }

    @Test
    public void testContainsAll() {
        System.out.println("-------------------------------testContainsAll() - Test-------------------------------");
        CacheCollection_1x0<String> collection = initCollection();
        System.out.println("referenceList1: " + Arrays_1x0.toString(referenceList1.toArray()));
        System.out.println("referenceList2: " + Arrays_1x0.toString(referenceList2.toArray()));
        System.out.println("referenceList3: " + Arrays_1x0.toString(referenceList3.toArray()));
        System.out.println("collection: " + Arrays_1x0.toString(collection.toArray()));
        assertTrue(collection.containsAll(referenceList1));
        assertFalse(collection.containsAll(referenceList2));
        assertTrue(collection.containsAll(referenceList3));
    }

    @Test
    public void testAdd() {
        System.out.println("-------------------------------CacheCollection_1x0Test.Add(Object) - Test-------------------------------");
        CacheCollection_1x0<String> collection = initCollection();
        assertTrue("add(\"10\") did not return true", collection.add("10"));
        assertEquals("size() is not incremented after add", referenceList3.size() + 1, collection.size());
        assertTrue("add() and contains() are not coherent", collection.contains("10"));
    }

    @Test
    public void testAdd01() {
        System.out.println("-------------------------------CacheCollection_1x0Test.Add(Object) - Test-------------------------------");
        CacheCollection_1x0<Integer> c = new CacheCollection_1x0<Integer>(1F, new ArrayList<Integer>());
        c.add(1);
        assertEquals(1, c.getCache().size());
    }

    @Test
    public void testAddAll() {
        System.out.println("-------------------------------testAddAll() - Test-------------------------------");
        CacheCollection_1x0<String> collection = initCollection();
        List<String> listP = new ArrayList<String>();
        listP.add("10");
        listP.add("11");
        assertTrue("addAll({\"10\",\"11\"}) did not return true", collection.addAll(listP));
        assertEquals("size() is not modified after addAll", referenceList3.size() + 2, collection.size());
        assertTrue("addAll() and contains() are not coherent", collection.contains("10"));
        assertTrue("addAll() and contains() are not coherent", collection.contains("11"));
    }

    @Test
    public void testRemove() {
        CacheCollection_1x0<String> collection = initCollection();
        assertTrue("remove(\"5\") did not return true", collection.remove("5"));
        assertEquals("size() is not decremented after remove", referenceList3.size() - 1, collection.size());
        assertFalse("remove() and contains() are not coherent", collection.contains("5"));
    }

    @Test
    public void testRemoveAll() {
        CacheCollection_1x0<String> collection = initCollection();
        List<String> listP = new ArrayList<String>();
        listP.add("5");
        listP.add("6");
        assertTrue("removeAll({\"5\",\"6\"}) did not return true", collection.removeAll(listP));
        assertEquals("size() is not modified after removeAll", referenceList3.size() - 2, collection.size());
        assertFalse("removeAll() and contains() are not coherent", collection.contains("5"));
        assertFalse("removeAll() and contains() are not coherent", collection.contains("6"));
    }

    @Test
    public void testRetainAll() {
        System.out.println("-------------------------------testRetainAll() - Test-------------------------------");
        CacheCollection_1x0<String> collection = initCollection();
        List<String> listP = new ArrayList<String>();
        listP.add("5");
        listP.add("6");
        assertTrue("retainAll({\"5\",\"6\"}) did not return true", collection.retainAll(listP));
        assertEquals("size() is not modified after removeAll", 2, collection.size());
        assertFalse("retainAll() and contains() are not coherent", collection.contains("0"));
        assertFalse("retainAll() and contains() are not coherent", collection.contains("1"));
        assertFalse("retainAll() and contains() are not coherent", collection.contains("2"));
        assertFalse("retainAll() and contains() are not coherent", collection.contains("3"));
        assertFalse("retainAll() and contains() are not coherent", collection.contains("4"));
        assertTrue("retainAll() and contains() are not coherent", collection.contains("5"));
        assertTrue("retainAll() and contains() are not coherent", collection.contains("6"));
        assertFalse("retainAll() and contains() are not coherent", collection.contains("7"));
        assertFalse("retainAll() and contains() are not coherent", collection.contains("8"));
        assertFalse("retainAll() and contains() are not coherent", collection.contains("9"));
    }

    @Test
    public void testSize() {
        System.out.println("-------------------------------testSize() - Test-------------------------------");
        CacheCollection_1x0<String> collection = initCollection();
        assertTrue(collection.size() == referenceList1.size());
        assertTrue(collection.size() == referenceList3.size());
        assertFalse(collection.size() == referenceList2.size());
    }

    @Test
    public void testIsEmpty() {
        System.out.println("-------------------------------testIsEmpty() - Test-------------------------------");
        Collection<String> collP = buildTestCollection();
        assertTrue(collP.isEmpty());
        assertEquals(0, collP.size());
        collP.add("0");
        assertFalse(collP.isEmpty());
        assertEquals(1, collP.size());
    }

    @Test
    public void testClear() {
        System.out.println("-------------------------------testClear() - Test-------------------------------");
        CacheCollection_1x0<String> collection = initCollection();
        collection.clear();
        assertTrue(collection.isEmpty());
        assertEquals(0, collection.size());
        assertFalse("clear() and contains() are not coherent", collection.contains("0"));
        assertFalse("clear() and contains() are not coherent", collection.contains("1"));
        assertFalse("clear() and contains() are not coherent", collection.contains("2"));
        assertFalse("clear() and contains() are not coherent", collection.contains("3"));
        assertFalse("clear() and contains() are not coherent", collection.contains("4"));
        assertFalse("clear() and contains() are not coherent", collection.contains("5"));
        assertFalse("clear() and contains() are not coherent", collection.contains("6"));
        assertFalse("clear() and contains() are not coherent", collection.contains("7"));
        assertFalse("clear() and contains() are not coherent", collection.contains("8"));
        assertFalse("clear() and contains() are not coherent", collection.contains("9"));
    }

    @Test
    public void testNullBehaviour() {
        System.out.println("-------------------------------testNullBehaviour() - Test-------------------------------");
        CacheCollection_1x0<String> collection = initCollection();
        assertTrue(collection.add(null));
        assertTrue(collection.contains(null));
        assertTrue(collection.remove(null));
        assertFalse(collection.contains(null));
    }

    @Test
    public void testIterator() {
        System.out.println("-------------------------------testIterator() - Test-------------------------------");
        CacheCollection_1x0<String> collection = initCollection();
        Iterator<String> iP = collection.iterator();
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertFalse(iP.hasNext());
    }

    @Test
    public void testIteratorRemove() {
        System.out.println("-------------------------------testIteratorRemove() - Test-------------------------------");
        CacheCollection_1x0<String> collection = initCollection();
        Object o1P, o2P;
        Iterator<String> iP = collection.iterator();
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(o1P = iP.next()));
        iP.remove();
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(o2P = iP.next()));
        iP.remove();
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertTrue(iP.hasNext());
        assertTrue(collection.contains(iP.next()));
        assertFalse(iP.hasNext());
        assertFalse(collection.contains(o1P));
        assertFalse(collection.contains(o2P));
        assertEquals(referenceList3.size() - 2, collection.size());
    }

    @Test
    public void testRegressionTest() {
        System.out.println("-------------------------------testRegressionTest() - Test-------------------------------");
        int arrayLength = (int) 2e4;
        List<Integer> testElements = Arrays.asList(ArraysHelper.createRandomArray(arrayLength, 0, arrayLength));
        ArrayList<Integer> c = new ArrayList<Integer>();
        CacheCollection_1x0<Integer> cacheC = new CacheCollection_1x0<Integer>(0.01F, c);
        for (Integer e : testElements) {
            int r = Random_1x0.randomInt(100);
            if (r < 20) {
                cacheC.add(e);
                assertTrue(c.contains(e));
            } else if (20 < r && r < 40) {
                int elementNumber1 = countElements(c, e);
                if (elementNumber1 == 0) elementNumber1++;
                cacheC.remove(e);
                int size1 = c.size();
                int size2 = cacheC.getCache().size();
                if (size1 == 0 && size2 != 0) {
                    cacheC.contains(e);
                }
                assertEquals(elementNumber1 - 1, countElements(c, e));
            } else {
                boolean b1 = c.contains(e);
                boolean b2 = cacheC.contains(e);
                assertEquals(b1, b2);
            }
            assertEquals(c.size(), cacheC.size());
        }
    }

    private int countElements(ArrayList<Integer> c, Integer e) {
        int elementNumber = 0;
        for (Integer e2 : c) {
            if (e2.equals(e)) {
                elementNumber++;
            }
        }
        return elementNumber;
    }

    @Test
    public void testListIteratorImpl() {
        System.out.println("-------------------------------testListIteratorImpl() - Test-------------------------------");
        CacheList_1x0<Integer> list = CacheList_1x0.of(0.1F, new ArrayList<Integer>());
        list.add(5);
        list.add(7);
        list.add(null);
        list.add(7);
        assertTrue(list.getCache().containsKey(5));
        ListIterator<Integer> it = list.listIterator();
        try {
            it.remove();
            fail("the exception should be thrown");
        } catch (IllegalStateException e) {
        }
        assertTrue(list.contains(null));
        while (it.hasNext()) {
            Integer elem = (Integer) it.next();
            if (elem != null && elem.equals(5)) it.remove();
        }
        while (it.hasPrevious()) {
            Integer elem = (Integer) it.previous();
            if (elem != null && elem.equals(7)) it.set(11);
        }
        assertEquals(3, list.size());
        assertFalse(list.contains(5));
        assertTrue(list.contains(11));
    }

    @Test
    public void testRemoveInt() {
        System.out.println("-------------------------------testRemoveInt() - Test-------------------------------");
        CacheList_1x0<Integer> list = CacheList_1x0.of(0.1F, new ArrayList<Integer>());
        for (int i = 0; i < 20; i++) {
            list.add(Random_1x0.randomInt(100));
            if (Random_1x0.randomInt(10) == 3) list.add(null);
        }
        while (0 < list.size()) {
            int i = list.size();
            System.out.print(i + " ");
            list.remove(Random_1x0.randomInt(i));
            assertEquals(i - 1, list.size());
        }
        System.out.println();
    }

    @Test
    public void testSet() {
        System.out.println("-------------------------------testSet() - Test-------------------------------");
        CacheList_1x0<Integer> list = CacheList_1x0.of(0.1F, new ArrayList<Integer>());
        for (int i = 0; i < 20; i++) {
            int p;
            list.add(p = Random_1x0.randomInt(100));
            System.out.print(p + " ");
            if (Random_1x0.randomInt(10) == 3) {
                list.add(null);
                System.out.print("null ");
            }
        }
        System.out.println();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
                list.set(i, new Integer(8888));
                assertFalse(list.get(i).equals(null));
            }
            System.out.print(list.get(i) + " ");
        }
        System.out.println();
    }
}
