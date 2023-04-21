package org.enerj.apache.commons.collections.buffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import junit.framework.Test;
import org.enerj.apache.commons.collections.BufferUnderflowException;
import org.enerj.apache.commons.collections.BulkTest;
import org.enerj.apache.commons.collections.collection.AbstractTestCollection;

/**
 * Test cases for BoundedFifoBuffer.
 * 
 * @version $Revision: 155406 $ $Date: 2005-02-26 12:55:26 +0000 (Sat, 26 Feb 2005) $
 * 
 * @author Paul Jack
 */
public class TestBoundedFifoBuffer extends AbstractTestCollection {

    public TestBoundedFifoBuffer(String n) {
        super(n);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestBoundedFifoBuffer.class);
    }

    /**
     *  Runs through the regular verifications, but also verifies that 
     *  the buffer contains the same elements in the same sequence as the
     *  list.
     */
    public void verify() {
        super.verify();
        Iterator iterator1 = collection.iterator();
        Iterator iterator2 = confirmed.iterator();
        while (iterator2.hasNext()) {
            assertTrue(iterator1.hasNext());
            Object o1 = iterator1.next();
            Object o2 = iterator2.next();
            assertEquals(o1, o2);
        }
    }

    /**
     * Overridden because UnboundedFifoBuffer doesn't allow null elements.
     * @return false
     */
    public boolean isNullSupported() {
        return false;
    }

    /**
     * Overridden because UnboundedFifoBuffer isn't fail fast.
     * @return false
     */
    public boolean isFailFastSupported() {
        return false;
    }

    /**
     *  Returns an empty ArrayList.
     *
     *  @return an empty ArrayList
     */
    public Collection makeConfirmedCollection() {
        return new ArrayList();
    }

    /**
     *  Returns a full ArrayList.
     *
     *  @return a full ArrayList
     */
    public Collection makeConfirmedFullCollection() {
        Collection c = makeConfirmedCollection();
        c.addAll(java.util.Arrays.asList(getFullElements()));
        return c;
    }

    /**
     *  Returns an empty BoundedFifoBuffer that won't overflow.  
     *  
     *  @return an empty BoundedFifoBuffer
     */
    public Collection makeCollection() {
        return new BoundedFifoBuffer(100);
    }

    /**
     * Tests that the removal operation actually removes the first element.
     */
    public void testBoundedFifoBufferRemove() {
        resetFull();
        int size = confirmed.size();
        for (int i = 0; i < size; i++) {
            Object o1 = ((BoundedFifoBuffer) collection).remove();
            Object o2 = ((ArrayList) confirmed).remove(0);
            assertEquals("Removed objects should be equal", o1, o2);
            verify();
        }
        try {
            ((BoundedFifoBuffer) collection).remove();
            fail("Empty buffer should raise Underflow.");
        } catch (BufferUnderflowException e) {
        }
    }

    /**
     * Tests that the constructor correctly throws an exception.
     */
    public void testConstructorException1() {
        try {
            new BoundedFifoBuffer(0);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    /**
     * Tests that the constructor correctly throws an exception.
     */
    public void testConstructorException2() {
        try {
            new BoundedFifoBuffer(-20);
        } catch (IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    /**
     * Tests that the constructor correctly throws an exception.
     */
    public void testConstructorException3() {
        try {
            new BoundedFifoBuffer(null);
        } catch (NullPointerException ex) {
            return;
        }
        fail();
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }

    public void testShift() {
        BoundedFifoBuffer fifo = new BoundedFifoBuffer(3);
        fifo.add("a");
        fifo.add("b");
        fifo.add("c");
        fifo.remove();
        fifo.add("e");
        fifo.remove("c");
    }
}
