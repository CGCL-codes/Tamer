package org.apache.commons.collections.map;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import junit.framework.Test;
import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.IterableMap;

/**
 * Tests for ReferenceIdentityMap. 
 * 
 * @version $Revision: 646780 $
 *
 * @author Paul Jack
 * @author Stephen Colebourne
 * @author Guilhem Lavaux
 */
public class TestReferenceIdentityMap extends AbstractTestIterableMap {

    private static final Integer I1A = new Integer(1);

    private static final Integer I1B = new Integer(1);

    private static final Integer I2A = new Integer(2);

    private static final Integer I2B = new Integer(2);

    public TestReferenceIdentityMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestReferenceIdentityMap.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestReferenceIdentityMap.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public Map makeEmptyMap() {
        ReferenceIdentityMap map = new ReferenceIdentityMap(ReferenceIdentityMap.WEAK, ReferenceIdentityMap.WEAK);
        return map;
    }

    public Map makeConfirmedMap() {
        return new IdentityMap();
    }

    public boolean isAllowNullKey() {
        return false;
    }

    public boolean isAllowNullValue() {
        return false;
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

    public void testBasics() {
        IterableMap map = new ReferenceIdentityMap(ReferenceIdentityMap.HARD, ReferenceIdentityMap.HARD);
        assertEquals(0, map.size());
        map.put(I1A, I2A);
        assertEquals(1, map.size());
        assertSame(I2A, map.get(I1A));
        assertSame(null, map.get(I1B));
        assertEquals(true, map.containsKey(I1A));
        assertEquals(false, map.containsKey(I1B));
        assertEquals(true, map.containsValue(I2A));
        assertEquals(false, map.containsValue(I2B));
        map.put(I1A, I2B);
        assertEquals(1, map.size());
        assertSame(I2B, map.get(I1A));
        assertSame(null, map.get(I1B));
        assertEquals(true, map.containsKey(I1A));
        assertEquals(false, map.containsKey(I1B));
        assertEquals(false, map.containsValue(I2A));
        assertEquals(true, map.containsValue(I2B));
        map.put(I1B, I2B);
        assertEquals(2, map.size());
        assertSame(I2B, map.get(I1A));
        assertSame(I2B, map.get(I1B));
        assertEquals(true, map.containsKey(I1A));
        assertEquals(true, map.containsKey(I1B));
        assertEquals(false, map.containsValue(I2A));
        assertEquals(true, map.containsValue(I2B));
    }

    public void testHashEntry() {
        IterableMap map = new ReferenceIdentityMap(ReferenceIdentityMap.HARD, ReferenceIdentityMap.HARD);
        map.put(I1A, I2A);
        map.put(I1B, I2A);
        Map.Entry entry1 = (Map.Entry) map.entrySet().iterator().next();
        Iterator it = map.entrySet().iterator();
        Map.Entry entry2 = (Map.Entry) it.next();
        Map.Entry entry3 = (Map.Entry) it.next();
        assertEquals(true, entry1.equals(entry2));
        assertEquals(true, entry2.equals(entry1));
        assertEquals(false, entry1.equals(entry3));
    }

    public void testNullHandling() {
        resetFull();
        assertEquals(null, map.get(null));
        assertEquals(false, map.containsKey(null));
        assertEquals(false, map.containsValue(null));
        assertEquals(null, map.remove(null));
        assertEquals(false, map.entrySet().contains(null));
        assertEquals(false, map.keySet().contains(null));
        assertEquals(false, map.values().contains(null));
        try {
            map.put(null, null);
            fail();
        } catch (NullPointerException ex) {
        }
        try {
            map.put(new Object(), null);
            fail();
        } catch (NullPointerException ex) {
        }
        try {
            map.put(null, new Object());
            fail();
        } catch (NullPointerException ex) {
        }
    }

    WeakReference keyReference;

    WeakReference valueReference;

    public Map buildRefMap() {
        Object key = new Object();
        Object value = new Object();
        keyReference = new WeakReference(key);
        valueReference = new WeakReference(value);
        Map testMap = new ReferenceIdentityMap(ReferenceMap.WEAK, ReferenceMap.HARD, true);
        testMap.put(key, value);
        assertEquals("In map", value, testMap.get(key));
        assertNotNull("Weak reference released early (1)", keyReference.get());
        assertNotNull("Weak reference released early (2)", valueReference.get());
        return testMap;
    }

    /** Tests whether purge values setting works */
    public void testPurgeValues() throws Exception {
        Map testMap = buildRefMap();
        int iterations = 0;
        int bytz = 2;
        while (true) {
            System.gc();
            if (iterations++ > 50) {
                fail("Max iterations reached before resource released.");
            }
            testMap.isEmpty();
            if (keyReference.get() == null && valueReference.get() == null) {
                break;
            } else {
                byte[] b = new byte[bytz];
                bytz = bytz * 2;
            }
        }
    }

    private static void gc() {
        try {
            byte[][] tooLarge = new byte[1000000000][1000000000];
            fail("you have too much RAM");
        } catch (OutOfMemoryError ex) {
            System.gc();
        }
    }
}
