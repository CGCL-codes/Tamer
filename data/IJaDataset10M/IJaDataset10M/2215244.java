package org.apache.harmony.auth.tests.javax.security.auth.callback.serialization;

import javax.security.auth.callback.LanguageCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.harmony.testframework.serialization.SerializationTest;

/**
 * Serialization test for UnsupportedCallbackException class
 */
public class UnsupportedCallbackExceptionTest extends SerializationTest {

    static LanguageCallback nc = new LanguageCallback();

    @Override
    protected Object[] getData() {
        return new Object[] { new UnsupportedCallbackException(nc) };
    }
}
