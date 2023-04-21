package com.thoughtworks.turtlemock.constraint;

import com.thoughtworks.turtlemock.fluapi.Closure;
import junit.framework.TestCase;
import java.io.IOException;

public class ExceptionContraintTest extends TestCase {

    public void testShouldUnMatchIfExceptionNotThrown() throws Exception {
        ExceptionContraint constraint = new ExceptionContraint(Exception.class);
        assertFalse(constraint.check(new Closure() {

            public void execute() {
            }
        }).isFullFilled());
    }

    public void testShouldMatchIfExactExceptionThrown() throws Exception {
        ExceptionContraint constraint = new ExceptionContraint(Exception.class);
        assertTrue(constraint.check(new Closure() {

            public void execute() throws Exception {
                throw new Exception();
            }
        }).isFullFilled());
    }

    public void testShouldMatchIfSubClassExceptionThrown() throws Exception {
        ExceptionContraint constraint = new ExceptionContraint(Exception.class);
        assertTrue(constraint.check(new Closure() {

            public void execute() throws Exception {
                throw new IOException();
            }
        }).isFullFilled());
    }

    public void testShouldThrowOutTheExeptionUnExpected() throws Exception {
        final Exception exceptionWillThrown = new Exception();
        ExceptionContraint constraint = new ExceptionContraint(IOException.class);
        try {
            constraint.check(new Closure() {

                public void execute() throws Exception {
                    throw exceptionWillThrown;
                }
            });
            fail();
        } catch (RuntimeException e) {
            assertEquals(e.getCause(), exceptionWillThrown);
        }
    }
}
