package org.apache.harmony.xnet.tests.javax.net.ssl.serialization;

import javax.net.ssl.SSLKeyException;
import org.apache.harmony.testframework.serialization.SerializationTest;

/**
 * Test for SSLKeyException serialization
 * 
 */
public class SSLKeyExceptionTest extends SerializationTest {

    public static String[] msgs = { "New message", "Long message for Exception. Long message for Exception. Long message for Exception." };

    @Override
    protected Object[] getData() {
        return new Object[] { new SSLKeyException(null), new SSLKeyException(msgs[0]), new SSLKeyException(msgs[1]) };
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SSLKeyExceptionTest.class);
    }
}
