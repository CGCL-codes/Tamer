package com.phloc.commons.exceptions;

import org.junit.Test;

/**
 * Test class of class {@link LoggedRuntimeException}.
 * 
 * @author philip
 */
public final class LoggedRuntimeExceptionTest {

    @Test
    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = { "RV_EXCEPTION_NOT_THROWN", "RV_RETURN_VALUE_IGNORED" }, justification = "only constructor tests")
    public void testAll() {
        new LoggedRuntimeException();
        new LoggedRuntimeException("any text");
        new LoggedRuntimeException(new Exception());
        new LoggedRuntimeException("any text", new Exception());
        new LoggedRuntimeException(false);
        new LoggedRuntimeException(false, "any text");
        new LoggedRuntimeException(false, new Exception());
        new LoggedRuntimeException(false, "any text", new Exception());
        LoggedRuntimeException.newException(new Exception());
        LoggedRuntimeException.newException(new LoggedException());
        LoggedRuntimeException.newException(new LoggedRuntimeException());
        LoggedRuntimeException.newException("any text", new Exception());
        LoggedRuntimeException.newException("any text", new LoggedException());
        LoggedRuntimeException.newException("any text", new LoggedRuntimeException());
    }
}
