package reconcile.data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import junit.framework.TestCase;
import com.google.common.collect.Maps;

@SuppressWarnings("unchecked")
public class AnnotationSetTest extends TestCase {

    public void testAddAnnotation() {
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        Annotation b = new Annotation(1, 5, 25, "typeB", new HashMap());
        Annotation c = new Annotation(2, 15, 35, "typeC", new HashMap());
        AnnotationSet as = new AnnotationSet("set1");
        as.add(a);
        as.add(b);
        as.add(c);
        assertEquals(3, as.size());
    }

    public void testAddSameId() {
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        Annotation b = new Annotation(0, 10, 20, "typeA", new HashMap());
        AnnotationSet as = new AnnotationSet("set1");
        as.add(a);
        as.add(b);
        assertEquals(1, as.size());
        Iterator<Annotation> ai = as.iterator();
        assertTrue(ai.hasNext());
        Annotation a1 = ai.next();
        assertNotNull(a1);
        assertEquals(a, a1);
        assertFalse(ai.hasNext());
    }

    public void testAddSameIdDiffContent() {
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        Annotation b = new Annotation(0, 15, 25, "typeB", new HashMap());
        AnnotationSet as = new AnnotationSet("set1");
        as.add(a);
        as.add(b);
        assertEquals(2, as.size());
        Iterator<Annotation> ai = as.iterator();
        assertTrue(ai.hasNext());
        Annotation a1 = ai.next();
        assertNotNull(a1);
        assertEquals(a, a1);
        assertTrue(ai.hasNext());
    }

    public void testAddDifferentId() {
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        Annotation b = new Annotation(1, 10, 20, "typeA", new HashMap());
        AnnotationSet as = new AnnotationSet("set1");
        as.add(a);
        as.add(b);
        assertEquals(1, as.size());
        Iterator<Annotation> ai = as.iterator();
        assertTrue(ai.hasNext());
        Annotation a1 = ai.next();
        assertNotNull(a1);
        assertEquals(a, a1);
        assertFalse(ai.hasNext());
    }

    public void testAddDifferentFeatures() {
        Map<String, String> featA = Maps.newTreeMap();
        featA.put("a", "a val");
        Map<String, String> featB = Maps.newTreeMap();
        featB.put("b", "b val");
        Annotation a = new Annotation(0, 10, 20, "typeA", featA);
        Annotation b = new Annotation(1, 10, 20, "typeA", featB);
        AnnotationSet as = new AnnotationSet("set1");
        as.add(a);
        as.add(b);
        assertEquals(2, as.size());
        Iterator<Annotation> ai = as.iterator();
        assertTrue(ai.hasNext());
        Annotation a1 = ai.next();
        assertNotNull(a1);
        assertEquals(a, a1);
        assertTrue(ai.hasNext());
        Annotation a2 = ai.next();
        assertNotNull(a2);
        assertEquals(b, a2);
        assertFalse(ai.hasNext());
    }

    public void testConstruct() {
        AnnotationSet as = new AnnotationSet("name");
        assertEquals(0, as.size());
        assertEquals("name", as.getName());
    }

    public void testConstructWithCollection() {
        ArrayList<Annotation> al = new ArrayList<Annotation>();
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        al.add(a);
        Annotation b = new Annotation(1, 15, 20, "typeB", new HashMap());
        al.add(b);
        AnnotationSet as = new AnnotationSet("name", al);
        assertEquals(2, as.size());
        assertEquals("name", as.getName());
    }

    public void testRemove() {
        ArrayList<Annotation> al = new ArrayList<Annotation>();
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        al.add(a);
        Annotation b = new Annotation(1, 15, 20, "typeB", new HashMap());
        al.add(b);
        AnnotationSet as = new AnnotationSet("name", al);
        assertEquals(2, as.size());
        as.remove(a);
        assertEquals(1, as.size());
        as.add(a);
        assertEquals(2, as.size());
        as.remove(a);
        assertEquals(1, as.size());
    }

    public void testGetById() {
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        Annotation b = new Annotation(1, 15, 20, "typeB", new HashMap());
        AnnotationSet as = new AnnotationSet("name");
        as.add(a);
        as.add(b);
        assertEquals(2, as.size());
        Annotation a1 = as.get(0);
        assertEquals(a, a1);
        assertFalse(b.equals(a1));
        Annotation b1 = as.get(1);
        assertEquals(b, b1);
        assertFalse(a.equals(b1));
        boolean success = as.remove(a);
        assertTrue(success);
        assertEquals(1, as.size());
        Annotation a2 = as.get(0);
        assertNull(a2);
    }

    public void testGet() {
        ArrayList<Annotation> al = new ArrayList<Annotation>();
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        al.add(a);
        Annotation b = new Annotation(1, 15, 20, "typeB", new HashMap());
        al.add(b);
        AnnotationSet as = new AnnotationSet("name", al);
        assertEquals(2, as.size());
        AnnotationSet as2 = as.get();
        assertEquals(2, as2.size());
        for (Annotation ai : as2) {
            assertTrue(as.contains(ai));
        }
    }

    public void testGetWhenEmpty() {
        AnnotationSet as = new AnnotationSet("name");
        assertEquals(0, as.size());
        AnnotationSet as2 = as.get();
        assertEquals(0, as2.size());
    }

    public void testGetByType() {
        ArrayList<Annotation> al = new ArrayList<Annotation>();
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        al.add(a);
        Annotation b = new Annotation(1, 15, 20, "typeB", new HashMap());
        al.add(b);
        AnnotationSet as = new AnnotationSet("name", al);
        assertEquals(2, as.size());
        AnnotationSet as2 = as.get("typeA");
        assertEquals(1, as2.size());
        for (Annotation ai : as2) {
            assertEquals(a, ai);
        }
        AnnotationSet as3 = as.get("typeB");
        assertEquals(1, as3.size());
        for (Annotation bi : as3) {
            assertEquals(b, bi);
        }
    }

    public void testGetOverlapping() {
        AnnotationSet as = new AnnotationSet("name");
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        as.add(a);
        Annotation b = new Annotation(1, 15, 25, "typeB", new HashMap());
        as.add(b);
        assertEquals(2, as.size());
        AnnotationSet as2 = as.getOverlapping(5, 12);
        assertEquals(1, as2.size());
        for (Annotation ai : as2) {
            assertEquals(a, ai);
        }
        AnnotationSet as3 = as.getOverlapping(17, 22);
        assertEquals(2, as3.size());
        AnnotationSet as4 = as.getOverlapping(22, 27);
        assertEquals(1, as4.size());
        for (Annotation bi : as4) {
            assertEquals(b, bi);
        }
    }

    public void testGetOverlapping2() {
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        Annotation b = new Annotation(1, 30, 40, "typeB", new HashMap());
        Annotation c = new Annotation(2, 50, 60, "typeC", new HashMap());
        AnnotationSet as = new AnnotationSet("set1");
        as.add(a);
        as.add(b);
        as.add(c);
        assertEquals(3, as.size());
        AnnotationSet as2 = as.getOverlapping(5, 12);
        assertEquals(1, as2.size());
        as2 = as.getOverlapping(22, 25);
        assertEquals(0, as2.size());
        as2 = as.getOverlapping(25, 35);
        assertEquals(1, as2.size());
        as2 = as.getOverlapping(55, 65);
        assertEquals(1, as2.size());
        as2 = as.getOverlapping(65, 75);
        assertEquals(0, as2.size());
    }

    public void testGetFirst() {
        ArrayList<Annotation> al = new ArrayList<Annotation>();
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        al.add(a);
        Annotation b = new Annotation(1, 15, 25, "typeB", new HashMap());
        al.add(b);
        AnnotationSet as = new AnnotationSet("name");
        Annotation s = as.getFirst();
        assertNull(s);
        as.addAll(al);
        assertEquals(2, as.size());
        Annotation s2 = as.getFirst();
        assertEquals(a, s2);
    }

    public void testGetLast() {
        ArrayList<Annotation> al = new ArrayList<Annotation>();
        Annotation b = new Annotation(1, 15, 25, "typeB", new HashMap());
        al.add(b);
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        al.add(a);
        AnnotationSet as = new AnnotationSet("name");
        Annotation s = as.getLast();
        assertNull(s);
        as.addAll(al);
        assertEquals(2, as.size());
        Annotation s2 = as.getLast();
        assertEquals(b, s2);
    }

    public void testGetContained() {
        ArrayList<Annotation> al = new ArrayList<Annotation>();
        Annotation b = new Annotation(1, 15, 25, "typeB", new HashMap());
        al.add(b);
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        al.add(a);
        AnnotationSet as = new AnnotationSet("name", al);
        Annotation c1 = new Annotation(2, 9, 21, "myType", new HashMap());
        Annotation c2 = new Annotation(2, 14, 26, "myType", new HashMap());
        Annotation c3 = new Annotation(2, 9, 26, "myType", new HashMap());
        Annotation c4 = new Annotation(2, 9, 10, "myType", new HashMap());
        Annotation c5 = new Annotation(2, 26, 41, "myType", new HashMap());
        AnnotationSet cs1 = as.getContained(c1);
        assertEquals(1, cs1.size());
        AnnotationSet cs2 = as.getContained(c2);
        assertEquals(1, cs2.size());
        AnnotationSet cs3 = as.getContained(c3);
        assertEquals(2, cs3.size());
        AnnotationSet cs4 = as.getContained(c4);
        assertEquals(0, cs4.size());
        AnnotationSet cs5 = as.getContained(c5);
        assertEquals(0, cs5.size());
    }

    public void testGetContainedNeg() {
        ArrayList<Annotation> al = new ArrayList<Annotation>();
        Annotation b = new Annotation(1, 15, 25, "typeB", new HashMap());
        al.add(b);
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        al.add(a);
        AnnotationSet as = new AnnotationSet("name", al);
        AnnotationSet cs1 = as.getContained(9, 21);
        assertEquals(1, cs1.size());
        AnnotationSet cs2 = as.getContained(14, 26);
        assertEquals(1, cs2.size());
        AnnotationSet cs3 = as.getContained(9, 26);
        assertEquals(2, cs3.size());
        AnnotationSet cs4 = as.getContained(9, 10);
        assertEquals(0, cs4.size());
        AnnotationSet cs5 = as.getContained(26, 41);
        assertEquals(0, cs5.size());
        AnnotationSet cs6 = as.getContained(-1, 100);
        assertEquals(2, cs6.size());
    }

    public void testGetOrderedAnnots() {
        ArrayList<Annotation> al = new ArrayList<Annotation>();
        Annotation b = new Annotation(1, 15, 25, "typeB", new HashMap());
        al.add(b);
        Annotation a = new Annotation(0, 10, 20, "typeA", new HashMap());
        al.add(a);
        AnnotationSet as = new AnnotationSet("name", al);
        List<Annotation> oa = as.getOrderedAnnots();
        Annotation a1 = oa.get(0);
        assertEquals(a, a1);
        Annotation a2 = oa.get(1);
        assertEquals(b, a2);
    }

    public void testAddByteOffsets() {
        AnnotationSet as = new AnnotationSet("name");
        as.add(0, 10, "typeA");
        assertEquals(1, as.size());
        as.add(0, 10, "typeA");
        assertEquals(1, as.size());
        as.add(2, 20, "typeA");
        assertEquals(2, as.size());
        List<Annotation> oa = as.getOrderedAnnots();
        assertEquals(2, oa.size());
        Iterator iterator = as.iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    public void testAddByteOffsetsWithId() {
        AnnotationSet as = new AnnotationSet("name");
        as.add(0, 0, 10, "typeA");
        assertEquals(1, as.size());
        as.add(1, 0, 10, "typeA");
        assertEquals(1, as.size());
        as.add(2, 2, 20, "typeA");
        assertEquals(2, as.size());
        List<Annotation> oa = as.getOrderedAnnots();
        assertEquals(2, oa.size());
        Iterator iterator = as.iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    public void testAddMethodCount() {
        Method[] m = AnnotationSet.class.getMethods();
        int addCount = 0;
        int fivePCount = 0;
        for (Method element : m) {
            if (element.getName().equals("add")) {
                addCount++;
                if (element.getGenericParameterTypes().length == 5) {
                    fivePCount++;
                }
            }
        }
        assertEquals(1, fivePCount);
    }

    public void testGetTypes() {
        AnnotationSet as = new AnnotationSet("name");
        as.add(0, 0, 10, "typeA");
        assertEquals(1, as.size());
        as.add(1, 0, 10, "typeA");
        assertEquals(1, as.size());
        as.add(2, 2, 20, "typeA");
        assertEquals(2, as.size());
        Set<String> types = as.getAllTypes();
        assertEquals(1, types.size());
        String t = types.iterator().next();
        assertEquals("typeA", t);
        as.add(3, 10, 20, "typeB");
        types = as.getAllTypes();
        assertEquals(2, types.size());
        for (Object element : types) {
            String name = (String) element;
            assertTrue(name.equals("typeA") || name.equals("typeB"));
        }
    }

    public void testClear() {
        AnnotationSet as = new AnnotationSet("name");
        as.add(0, 0, 10, "typeA");
        assertEquals(1, as.size());
        as.add(1, 0, 10, "typeA");
        assertEquals(1, as.size());
        as.add(2, 2, 20, "typeA");
        assertEquals(2, as.size());
        Set<String> types = as.getAllTypes();
        assertEquals(1, types.size());
        String t = types.iterator().next();
        assertEquals("typeA", t);
        as.add(3, 10, 20, "typeB");
        types = as.getAllTypes();
        assertEquals(2, types.size());
        for (Object element : types) {
            String name = (String) element;
            assertTrue(name.equals("typeA") || name.equals("typeB"));
        }
        as.clear();
        assertEquals(0, as.size());
        types = as.getAllTypes();
        assertEquals(0, types.size());
    }

    public void testVector() {
        AnnotationSet as = new AnnotationSet("name");
        as.add(0, 10, 20, "typeA");
        assertEquals(1, as.size());
        as.add(1, 15, 25, "typeA");
        assertEquals(2, as.size());
        as.add(2, 20, 30, "typeA");
        assertEquals(3, as.size());
        Vector<Annotation> ans = new Vector<Annotation>(as.getOverlapping(5, 12));
        assertEquals(1, ans.size());
        Iterator<Annotation> i = ans.iterator();
        assertTrue(i.hasNext());
        Annotation ai = i.next();
        assertNotNull(ai);
        assertFalse(i.hasNext());
        ans = new Vector<Annotation>(as.getOverlapping(5, 19));
        assertEquals(2, ans.size());
        i = ans.iterator();
        assertTrue(i.hasNext());
        ai = i.next();
        assertNotNull(ai);
        assertTrue(i.hasNext());
        ai = i.next();
        assertNotNull(ai);
        assertFalse(i.hasNext());
        ans = new Vector<Annotation>(as.getOverlapping(5, 40));
        assertEquals(3, ans.size());
        i = ans.iterator();
        assertTrue(i.hasNext());
        ai = i.next();
        assertNotNull(ai);
        assertTrue(i.hasNext());
        ai = i.next();
        assertNotNull(ai);
        assertTrue(i.hasNext());
        ai = i.next();
        assertNotNull(ai);
        assertFalse(i.hasNext());
    }

    public void testAnnotationIdConservation() {
        AnnotationSet as = new AnnotationSet("name");
        Annotation a = new Annotation(0, 10, 20, "typeA");
        as.add(a);
        assertEquals(1, as.size());
        Annotation b = new Annotation(0, 15, 25, "typeA");
        as.add(b);
        assertEquals(2, as.size());
        Annotation c = new Annotation(0, 20, 30, "typeA");
        as.add(c);
        assertEquals(3, as.size());
        Annotation a1 = as.get(0);
        assertEquals(a, a1);
        Annotation b1 = as.get(1);
        assertEquals(b, b1);
        Annotation c1 = as.get(2);
        assertEquals(c, c1);
        Annotation d = new Annotation(3, 30, 40, "typeA");
        List<Annotation> dList = new ArrayList<Annotation>();
        dList.add(d);
        as.addAll(dList);
        Annotation d1 = as.get(3);
        assertEquals(d, d1);
    }
}
