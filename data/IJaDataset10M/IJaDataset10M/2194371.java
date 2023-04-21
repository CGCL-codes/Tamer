package org.fishwife.jrugged;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.concurrent.Callable;
import org.junit.Test;

public final class TestFailureInterpreter {

    @SuppressWarnings("unchecked")
    @Test
    public void testAcceptableException() throws Exception {
        final RuntimeException theExn = new RuntimeException();
        final Callable<Object> callable = createMock(Callable.class);
        final FailureInterpreter interpreter = createMock(FailureInterpreter.class);
        final CircuitBreaker cb = new CircuitBreaker(interpreter);
        expect(callable.call()).andThrow(theExn);
        expect(interpreter.shouldTrip(theExn)).andReturn(false);
        replay(callable);
        replay(interpreter);
        try {
            cb.invoke(callable);
            fail("exception expected.");
        } catch (Exception e) {
        }
        assertEquals("Status should be UP", Status.UP, cb.getStatus());
        verify(callable);
        verify(interpreter);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testUnacceptableException() throws Exception {
        final RuntimeException theExn = new RuntimeException();
        final Callable<Object> callable = createMock(Callable.class);
        final FailureInterpreter interpreter = createMock(FailureInterpreter.class);
        final CircuitBreaker cb = new CircuitBreaker(interpreter);
        expect(callable.call()).andThrow(theExn);
        expect(interpreter.shouldTrip(theExn)).andReturn(true);
        replay(callable);
        replay(interpreter);
        try {
            cb.invoke(callable);
            fail("exception expected.");
        } catch (Exception e) {
        }
        verify(callable);
        verify(interpreter);
        assertEquals("Status should be DOWN", Status.DOWN, cb.getStatus());
    }
}
