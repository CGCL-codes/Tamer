package com.phloc.commons.exceptions;

import org.junit.Test;

/**
 * Test class of class {@link InitializationException}.
 * 
 * @author philip
 */
public final class InitializationExceptionTest {

    @Test
    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = { "RV_EXCEPTION_NOT_THROWN", "RV_RETURN_VALUE_IGNORED" }, justification = "only constructor tests")
    public void testAll() {
        new InitializationException();
        new InitializationException("any text");
        new InitializationException(new Exception());
        new InitializationException("any text", new Exception());
    }
}
