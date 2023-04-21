package org.apache.harmony.auth.tests.javax.security.auth.login;

import javax.security.auth.login.AccountExpiredException;
import junit.framework.TestCase;

/**
 * Tests AccountExpiredException class
 */
public class AccountExpiredExceptionTest extends TestCase {

    /**
     * @tests javax.security.auth.login.AccountExpiredException#AccountExpiredException()
     */
    public final void testCtor1() {
        assertNull(new AccountExpiredException().getMessage());
    }

    /**
     * @tests javax.security.auth.login.AccountExpiredException#AccountExpiredException(
     *        java.lang.String)
     */
    public final void testCtor2() {
        assertNull(new AccountExpiredException(null).getMessage());
        String message = "";
        assertSame(message, new AccountExpiredException(message).getMessage());
        message = "message";
        assertSame(message, new AccountExpiredException(message).getMessage());
    }
}
