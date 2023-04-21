package org.statefive.feedstate.feed;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FeedExceptionTest {

    private static final String message = "Basic error message.";

    private static final NullPointerException npex = new NullPointerException("");

    @Test
    public void testFeedExceptionString() {
        try {
            throw new FeedException(message);
        } catch (FeedException throwEx) {
            assertEquals(throwEx.getMessage(), message);
        }
    }

    @Test
    public void testFeedExceptionStringThrowable() {
        try {
            throw new FeedException(message, npex);
        } catch (FeedException throwEx) {
            assertEquals(throwEx.getMessage(), message);
            assertEquals(throwEx.getCause().getClass(), NullPointerException.class);
            assertEquals(throwEx.getCause().getMessage(), npex.getMessage());
        }
    }

    @Test
    public void testFeedExceptionThrowable() {
        try {
            throw new FeedException(npex);
        } catch (FeedException throwEx) {
            assertEquals(throwEx.getCause().getClass(), NullPointerException.class);
            assertEquals(throwEx.getCause().getMessage(), npex.getMessage());
        }
    }
}
