package com.google.web.bindery.event.shared;

import junit.framework.TestCase;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Unit test for {@link #UmbrellaException}.
 */
public class UmbrellaExceptionTest extends TestCase {

    public void testNone() {
        try {
            throw new UmbrellaException(Collections.<Throwable>emptySet());
        } catch (UmbrellaException e) {
            assertNull(e.getCause());
            assertNull(e.getMessage());
        }
    }

    public void testOne() {
        Set<Throwable> causes = new HashSet<Throwable>();
        String message = "Just me";
        RuntimeException theOne = new RuntimeException(message);
        causes.add(theOne);
        try {
            throw new UmbrellaException(causes);
        } catch (UmbrellaException e) {
            assertSame(theOne, e.getCause());
            assertEquals(UmbrellaException.ONE + message, e.getMessage());
        }
    }

    public void testSome() {
        Set<Throwable> causes = new HashSet<Throwable>();
        String oneMessage = "one";
        RuntimeException oneException = new RuntimeException(oneMessage);
        causes.add(oneException);
        String twoMessage = "two";
        RuntimeException twoException = new RuntimeException(twoMessage);
        causes.add(twoException);
        try {
            throw new UmbrellaException(causes);
        } catch (UmbrellaException e) {
            if (e.getCause() == oneException) {
                assertCauseMatchesFirstMessage(e, oneMessage, twoMessage);
            } else if (e.getCause() == twoException) {
                assertCauseMatchesFirstMessage(e, twoMessage, oneMessage);
            } else {
                fail("Expected one of the causes and its message");
            }
        }
    }

    private void assertCauseMatchesFirstMessage(UmbrellaException e, String firstMessage, String otherMessage) {
        assertTrue("Cause should be first message", e.getMessage().startsWith(2 + UmbrellaException.MULTIPLE + firstMessage));
        assertTrue("Should also see the other message", e.getMessage().contains(otherMessage));
    }
}
