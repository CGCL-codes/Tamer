package org.nakedobjects.applib.value;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PasswordTest {

    @Test
    public void testCheckPassword() {
        final Password password = new Password("secret");
        assertFalse(password.checkPassword(""));
        assertFalse(password.checkPassword("SECRET"));
        assertTrue(password.checkPassword("secret"));
    }

    @Test
    public void testTitleObscuresPassword() {
        Password password = new Password("secret");
        assertEquals("******", password.toString());
        password = new Password("a very very very long password");
        assertEquals("********************", password.toString());
    }
}
