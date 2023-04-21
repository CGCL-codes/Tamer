package com.google.gwt.inject.client.nonpublic;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Tests for issue #1. We don't actually support injecting non-public types,
 * so this Ginjector creation is expected to fail. This test does illustrate
 * that we give good error messages in this case. (They are not asserted but
 * you can see them in the output.) Unfortunately, this intentional creation of
 * a class with failures in the generator only works in hosted mode, so
 * the actual test contents are commented out. You can uncomment them to see
 * what the error messages are like.
 */
public class NonPublicTest extends GWTTestCase {

    public void testNonPublic() throws Exception {
    }

    public String getModuleName() {
        return "com.google.gwt.inject.InjectTest";
    }
}
