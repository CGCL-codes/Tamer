package org.pwsafe.util;

import junit.framework.TestCase;

public class PasswordPolicyTest extends TestCase {

    public void testValueOf() {
        PassphrasePolicy pol = PassphrasePolicy.valueOf("FFFFFFF000FFF000FFF");
        assertTrue(pol.digitChars);
        assertTrue(pol.lowercaseChars);
        assertTrue(pol.uppercaseChars);
        assertTrue(pol.symbolChars);
        assertTrue(pol.easyview);
        assertEquals(Integer.parseInt("FFF", 16), pol.length);
        pol = PassphrasePolicy.valueOf("0000000FFF000FFF000");
        assertFalse(pol.digitChars);
        assertFalse(pol.lowercaseChars);
        assertFalse(pol.uppercaseChars);
        assertFalse(pol.symbolChars);
        assertFalse(pol.easyview);
        assertEquals(0, pol.length);
        pol = PassphrasePolicy.valueOf("0800008FFF000FFF000");
        assertFalse(pol.digitChars);
        assertFalse(pol.lowercaseChars);
        assertFalse(pol.uppercaseChars);
        assertFalse(pol.symbolChars);
        assertFalse(pol.easyview);
        assertEquals(8, pol.length);
    }
}
