package com.stateofflow.invariantj.build;

import com.stateofflow.invariantj.InvariantViolationError;
import com.stateofflow.invariantj.NamedThrowableMap;

public class ExceptionsTest extends InvariantJTestCase {

    private interface Command {

        void execute() throws Exception;
    }

    public void testExceptionsThrownInIsInvariantMethodsAreTrapped() {
        assertThrowableThrownInIsInvariantMethodIsTrapped(new Exception());
    }

    public void testInvariantViolationErrorsThrownInIsInvariantMethodsAreTrapped() {
        assertThrowableThrownInIsInvariantMethodIsTrapped(new InvariantViolationError(new String[] {}, new NamedThrowableMap(), null));
    }

    private void assertThrowableThrownInIsInvariantMethodIsTrapped(final Throwable throwable) {
        try {
            new Command() {

                public void execute() {
                }

                public boolean isInvariantFoo() throws Throwable {
                    throw throwable;
                }
            }.execute();
            fail("Expected invariant violation did not occur");
        } catch (InvariantViolationError e) {
            assertTrue("Invariant name in message", e.getMessage().indexOf("Foo") != -1);
            assertSame("Exception enclosed", throwable, e.getTrappedExceptions().get("Foo"));
        }
    }

    public void testViolatedInvariantsAreDetectedWhenMethodsTerminateWithExceptionsAndExceptionsAreTrapped() throws Exception {
        final Exception exception = new Exception();
        try {
            new Command() {

                private boolean foo = true;

                public void execute() throws Exception {
                    foo = false;
                    throw exception;
                }

                public boolean isInvariantFoo() {
                    return foo;
                }
            }.execute();
            fail("Expected invariant violation did not occur");
        } catch (InvariantViolationError e) {
            assertTrue("Invariant name in message", e.getMessage().indexOf("Foo") != -1);
            assertSame("Exception trapped", exception, e.getCause());
        }
    }

    public void testExceptionsThrownFromConstructorsDoNotCauseInvariantViolationErrorsWhenInvariantsAreViolated() {
        class Foo {

            public Foo(Exception ex) throws Exception {
                throw ex;
            }

            public boolean isInvariantFoo() {
                return false;
            }
        }
        Exception thrown = new Exception();
        try {
            new Foo(thrown);
        } catch (Exception caught) {
            assertSame(thrown, caught);
        }
    }
}
