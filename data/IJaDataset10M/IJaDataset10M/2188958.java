package org.apache.harmony.crypto.tests.javax.crypto.serialization;

import javax.crypto.NoSuchPaddingException;
import org.apache.harmony.testframework.serialization.SerializationTest;

/**
 * Test for NuSuchPaddingException serialization
 * 
 */
public class NoSuchPaddingExceptionTest extends SerializationTest {

    public static String[] msgs = { "New message", "Long message for Exception. Long message for Exception. Long message for Exception." };

    protected Object[] getData() {
        return new Object[] { new NoSuchPaddingException(), new NoSuchPaddingException(null), new NoSuchPaddingException(msgs[1]) };
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(NoSuchPaddingExceptionTest.class);
    }
}
