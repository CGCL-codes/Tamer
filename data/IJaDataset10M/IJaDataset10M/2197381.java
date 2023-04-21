package net.sourceforge.processdash.util;

import junit.framework.TestCase;

public class RuntimeUtilsTest extends TestCase {

    public void testAssertMethod() {
        RuntimeUtils.assertMethod(String.class, "concat");
        RuntimeUtils.assertMethod(String.class, "toString");
        RuntimeUtils.assertMethod(String.class, "copyValueOf");
        try {
            RuntimeUtils.assertMethod(String.class, "foobar");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            assertEquals("Class java.lang.String does not support method foobar", uoe.getMessage());
        }
    }
}
