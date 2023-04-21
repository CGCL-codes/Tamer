package org.apache.harmony.luni.tests.java.lang;

import junit.framework.TestCase;

public class IllegalAccessErrorTest extends TestCase {

    /**
	 * @tests java.lang.IllegalAccessError#IllegalAccessError()
	 */
    public void test_Constructor() {
        IllegalAccessError e = new IllegalAccessError();
        assertNull(e.getMessage());
        assertNull(e.getLocalizedMessage());
        assertNull(e.getCause());
    }

    /**
     * @tests java.lang.IllegalAccessError#IllegalAccessError(java.lang.String)
     */
    public void test_ConstructorLjava_lang_String() {
        IllegalAccessError e = new IllegalAccessError("fixture");
        assertEquals("fixture", e.getMessage());
        assertNull(e.getCause());
    }
}
