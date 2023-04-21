package org.apache.harmony.auth.tests.javax.security.auth.login.serialization;

import javax.security.auth.login.FailedLoginException;
import org.apache.harmony.testframework.serialization.SerializationTest;

/**
 * Serialization test for FailedLoginException class
 */
public class FailedLoginExceptionTest extends SerializationTest {

    @Override
    protected Object[] getData() {
        return new Object[] { new FailedLoginException("message") };
    }
}
