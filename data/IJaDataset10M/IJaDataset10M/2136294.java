package org.acegisecurity.event.authorization;

import junit.framework.TestCase;
import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.event.authorization.AuthorizationFailureEvent;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.util.SimpleMethodInvocation;

/**
 * Tests {@link AuthorizationFailureEvent}.
 *
 * @author Ben Alex
 * @version $Id: AuthorizationFailureEventTests.java,v 1.3 2005/11/25 04:17:24 benalex Exp $
 */
public class AuthorizationFailureEventTests extends TestCase {

    public AuthorizationFailureEventTests() {
        super();
    }

    public AuthorizationFailureEventTests(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AuthorizationFailureEventTests.class);
    }

    public void testRejectsNulls() {
        try {
            new AuthorizationFailureEvent(null, new ConfigAttributeDefinition(), new UsernamePasswordAuthenticationToken("foo", "bar"), new AccessDeniedException("error"));
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
        try {
            new AuthorizationFailureEvent(new SimpleMethodInvocation(), null, new UsernamePasswordAuthenticationToken("foo", "bar"), new AccessDeniedException("error"));
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
        try {
            new AuthorizationFailureEvent(new SimpleMethodInvocation(), new ConfigAttributeDefinition(), null, new AccessDeniedException("error"));
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
        try {
            new AuthorizationFailureEvent(new SimpleMethodInvocation(), new ConfigAttributeDefinition(), new UsernamePasswordAuthenticationToken("foo", "bar"), null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertTrue(true);
        }
    }
}
