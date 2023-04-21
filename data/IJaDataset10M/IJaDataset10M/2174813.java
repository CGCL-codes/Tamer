package org.apache.harmony.luni.tests.java.lang;

import junit.framework.TestCase;

public class IncompatibleClassChangeErrorTest extends TestCase {

    /**
	 * @tests java.lang.IncompatibleClassChangeError#IncompatibleClassChangeError()
	 */
    public void test_Constructor() {
        IncompatibleClassChangeError e = new IncompatibleClassChangeError();
        assertNull(e.getMessage());
        assertNull(e.getLocalizedMessage());
        assertNull(e.getCause());
    }

    /**
     * @tests java.lang.IncompatibleClassChangeError#IncompatibleClassChangeError(java.lang.String)
     */
    public void test_ConstructorLjava_lang_String() {
        IncompatibleClassChangeError e = new IncompatibleClassChangeError("fixture");
        assertEquals("fixture", e.getMessage());
        assertNull(e.getCause());
    }
}
