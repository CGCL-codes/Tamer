package org.apache.harmony.auth.tests.javax.security.auth.callback;

import javax.security.auth.callback.PasswordCallback;
import junit.framework.TestCase;

/**
 * Tests PasswordCallback class
 */
public class PasswordCallbackTest extends TestCase {

    PasswordCallback pc;

    public final void testPasswordCallback() {
        pc = new PasswordCallback("prompt", true);
        assertEquals("prompt", pc.getPrompt());
        assertTrue(pc.isEchoOn());
        pc.setPassword(null);
        pc.clearPassword();
        assertNull(pc.getPassword());
        char[] pwd = { 'a', 'b', 'c' };
        pc.setPassword(pwd);
        assertEquals(new String(pwd), new String(pc.getPassword()));
        pc.clearPassword();
        assertEquals(pwd.length, pc.getPassword().length);
        assertFalse(new String(pwd).equals(pc.getPassword()));
        char[] p = new char[5];
        pc.setPassword(p);
        pc.clearPassword();
        assertEquals(p.length, pc.getPassword().length);
    }

    public final void testInit_01() {
        try {
            pc = new PasswordCallback("", true);
            fail("Prompt and DefaultName should not be empty");
        } catch (IllegalArgumentException e) {
        }
    }

    public final void testInit_02() {
        try {
            pc = new PasswordCallback(null, true);
            fail("Prompt and DefaultName should not null");
        } catch (IllegalArgumentException e) {
        }
    }
}
