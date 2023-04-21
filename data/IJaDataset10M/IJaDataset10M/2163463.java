package org.apache.harmony.beans.tests.java.beans;

import java.beans.IntrospectionException;
import junit.framework.TestCase;
import org.apache.harmony.testframework.serialization.SerializationTest;

/**
 * Unit test of IntrospectionException.
 */
public class IntrospectionExceptionTest extends TestCase {

    public void testConstructor() {
        String message = "IntrospectionExceptionTest";
        IntrospectionException e = new IntrospectionException(message);
        assertSame(message, e.getMessage());
    }

    public void testConstructor_MessageNull() {
        IntrospectionException e = new IntrospectionException(null);
        assertNull(e.getMessage());
    }

    /**
     * @tests serialization/deserialization.
     */
    public void testSerializationSelf() throws Exception {
        SerializationTest.verifySelf(new IntrospectionException("IntrospectionExceptionTest"));
    }

    /**
     * @tests serialization/deserialization compatibility with RI.
     */
    public void testSerializationCompatibility() throws Exception {
        SerializationTest.verifyGolden(this, new IntrospectionException("IntrospectionExceptionTest"));
    }

    public void testIntrospectionExceptionMessage() {
        IntrospectionException e = new IntrospectionException("test message");
        assertEquals("test message", e.getMessage());
    }
}
