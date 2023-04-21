package joq;

import java.util.NoSuchElementException;
import java.util.Observable;
import org.junit.Test;
import static org.junit.Assert.*;

class Element extends Observable implements Comparable<Element> {

    protected int value;

    Element(int n) {
        value = n;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int compareTo(Element o) {
        return value - o.value;
    }
}

/**
 *
 * @author jeff
 */
public class SkewHeapTest {

    /**
     * Test of size method, of class SkewHeap.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        SkewHeap<Element> instance = new SkewHeap<Element>();
        assertEquals(0, instance.size());
        for (int i = 0; i < 100; ++i) instance.push(new Element(i));
        assertEquals(100, instance.size());
        for (int i = 100; i > 0; --i) instance.pop();
        assertEquals(0, instance.size());
    }

    /**
     * Test of push method, of class SkewHeap.
     */
    @Test
    public void testPush() {
        System.out.println("push");
        SkewHeap<Element> instance = new SkewHeap<Element>();
        Element e = new Element(42);
        instance.push(e);
        assertSame(instance.pop(), e);
    }

    /**
     * Test of poll method, of class SkewHeap.
     */
    @Test
    public void testPoll() {
        System.out.println("poll");
        SkewHeap<Element> instance = new SkewHeap<Element>();
        assertNull(instance.poll());
        Element e = new Element(42);
        instance.push(e);
        assertSame(instance.poll(), e);
    }

    /**
     * Test of pop method, of class SkewHeap.
     */
    @Test
    public void testPop() {
        System.out.println("pop");
        SkewHeap<Element> instance = new SkewHeap<Element>();
        try {
            instance.pop();
            fail("popping empty heap did not throw NoSuchElementException");
        } catch (NoSuchElementException e) {
        } catch (Exception e) {
            fail("popping empty heap threw unexpected exception: " + e);
        }
        Element e = new Element(42);
        instance.push(e);
        assertSame(instance.pop(), e);
    }

    /**
     * Test of isEmpty method, of class SkewHeap.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        SkewHeap<Element> instance = new SkewHeap<Element>();
        assertTrue(instance.isEmpty());
        instance.push(new Element(42));
        assertFalse(instance.isEmpty());
    }

    /**
     * Test of merge method, of class SkewHeap.
     */
    @Test
    public void testMerge() {
        System.out.println("merge");
        SkewHeap<Element> a = new SkewHeap<Element>();
        for (int i = 0; i < 100; ++i) a.push(new Element(i));
        SkewHeap<Element> b = new SkewHeap<Element>();
        for (int i = 100; i < 200; ++i) b.push(new Element(i));
        a.merge(b);
        for (int i = 0; i < 200; ++i) assertEquals(i, a.pop().value);
    }

    /**
     * Test of toString method, of class SkewHeap.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        SkewHeap<Element> instance = new SkewHeap<Element>();
        assertEquals("<SkewHeap (0 items) >", instance.toString());
        instance.push(new Element(42));
        assertEquals("<SkewHeap (1 items) >", instance.toString());
    }

    /**
     * Test of shallowCopy method, of class SkewHeap.
     */
    @Test
    public void testShallowCopy() {
        System.out.println("shallowCopy");
        SkewHeap<Element> a = new SkewHeap<Element>();
        for (int i = 0; i < 100; ++i) a.push(new Element(i));
        SkewHeap<Element> b = a.shallowCopy();
        for (int i = 0; i < 100; ++i) assertEquals(a.pop().value, b.pop().value);
    }

    /**
     * Test of explain method, of class SkewHeap.
     */
    @Test
    public void testExplain() {
        System.out.println("explain: ignored");
    }
}
