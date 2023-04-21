package org.enerj.apache.commons.collections.buffer;

import java.util.Arrays;
import java.util.Collection;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.enerj.apache.commons.collections.ArrayStack;
import org.enerj.apache.commons.collections.Buffer;
import org.enerj.apache.commons.collections.collection.AbstractTestCollection;

/**
 * Extension of {@link AbstractTestCollection} for exercising the 
 * {@link UnmodifiableBuffer} implementation.
 *
 * @since Commons Collections 3.1
 * @version $Revision: 155406 $ $Date: 2005-02-26 12:55:26 +0000 (Sat, 26 Feb 2005) $
 * 
 * @author Phil Steitz
 * @author Stephen Colebourne
 */
public class TestUnmodifiableBuffer extends AbstractTestCollection {

    public TestUnmodifiableBuffer(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestUnmodifiableBuffer.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestUnmodifiableBuffer.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public Collection makeCollection() {
        return UnmodifiableBuffer.decorate(new UnboundedFifoBuffer());
    }

    public Collection makeFullCollection() {
        Buffer buffer = new UnboundedFifoBuffer();
        buffer.addAll(Arrays.asList(getFullElements()));
        return UnmodifiableBuffer.decorate(buffer);
    }

    public Collection makeConfirmedCollection() {
        ArrayStack list = new ArrayStack();
        return list;
    }

    public Collection makeConfirmedFullCollection() {
        ArrayStack list = new ArrayStack();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }

    public boolean isAddSupported() {
        return false;
    }

    public boolean isRemoveSupported() {
        return false;
    }

    public boolean isNullSupported() {
        return false;
    }

    public void testBufferRemove() {
        resetEmpty();
        Buffer buffer = (Buffer) collection;
        try {
            buffer.remove();
            fail();
        } catch (UnsupportedOperationException ex) {
        }
    }

    public String getCompatibilityVersion() {
        return "3.1";
    }
}
