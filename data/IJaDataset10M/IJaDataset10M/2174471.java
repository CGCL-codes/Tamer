package org.apache.harmony.prefs.tests.java.util.prefs;

import java.util.prefs.InvalidPreferencesFormatException;
import junit.framework.TestCase;
import org.apache.harmony.testframework.serialization.SerializationTest;

/**
 * 
 */
public class InvalidPreferencesFormatExceptionTest extends TestCase {

    public void testInvalidPreferencesFormatExceptionString() {
        InvalidPreferencesFormatException e = new InvalidPreferencesFormatException("msg");
        assertNull(e.getCause());
        assertEquals("msg", e.getMessage());
    }

    public void testInvalidPreferencesFormatExceptionStringThrowable() {
        Throwable t = new Throwable("root");
        InvalidPreferencesFormatException e = new InvalidPreferencesFormatException("msg", t);
        assertSame(t, e.getCause());
        assertTrue(e.getMessage().indexOf("root") < 0);
        assertTrue(e.getMessage().indexOf(t.getClass().getName()) < 0);
        assertTrue(e.getMessage().indexOf("msg") >= 0);
    }

    public void testInvalidPreferencesFormatExceptionThrowable() {
        Throwable t = new Throwable("root");
        InvalidPreferencesFormatException e = new InvalidPreferencesFormatException(t);
        assertSame(t, e.getCause());
        assertTrue(e.getMessage().indexOf("root") >= 0);
        assertTrue(e.getMessage().indexOf(t.getClass().getName()) >= 0);
    }

    /**
     * @tests serialization/deserialization.
     */
    public void testSerializationSelf() throws Exception {
        SerializationTest.verifySelf(new InvalidPreferencesFormatException("msg"));
    }

    /**
     * @tests serialization/deserialization compatibility with RI.
     */
    public void testSerializationCompatibility() throws Exception {
        SerializationTest.verifyGolden(this, new InvalidPreferencesFormatException("msg"));
    }
}
