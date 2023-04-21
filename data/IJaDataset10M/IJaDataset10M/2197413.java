package abid.password.types;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import abid.password.PasswordParseException;
import abid.password.Password;
import abid.password.PasswordException;

public class SimplePasswordTest {

    @Test
    public void confirmPasswordShouldValidatePasswordCorrectly() throws PasswordParseException, PasswordException {
        Password password = new SimplePassword("pass1");
        assertTrue(password.confirmPassword("pass1"));
        assertFalse(password.confirmPassword("pass123"));
        password = new SimplePassword("pass[");
        assertTrue(password.confirmPassword("pass["));
        assertFalse(password.confirmPassword("pass"));
        password = new SimplePassword("pass[]");
        assertTrue(password.confirmPassword("pass[]"));
        assertFalse(password.confirmPassword("pass"));
        password = new SimplePassword("pass[{]");
        assertTrue(password.confirmPassword("pass[{]"));
        assertFalse(password.confirmPassword("pass"));
        password = new SimplePassword("ab[id}]");
        assertTrue(password.confirmPassword("ab[id}]"));
        assertFalse(password.confirmPassword("bcje"));
    }
}
