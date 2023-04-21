package com.stateofflow.invariantj.build;

import com.stateofflow.invariantj.InvariantViolationError;
import junit.framework.TestCase;

public class ConstructorTest extends TestCase {

    public void testCallFromSuperclassToSubclassDuringSuperclassConstructionDoesNotCauseAnErrorWhenSubclassInvariantIsViolated() {
        abstract class Super {

            public Super() {
                foo();
            }

            public abstract void foo();
        }
        class Sub extends Super {

            private boolean bar;

            public Sub() {
                bar = true;
            }

            public void foo() {
            }

            protected boolean isInvariantBar() {
                return bar;
            }
        }
        new Sub();
    }

    public void testViolationAtExitOfConstructorCausesAnError() {
        class Foo {

            public Foo() {
            }

            protected boolean isInvariantBar() {
                return false;
            }
        }
        try {
            new Foo();
            fail("Expected invariant violation did not occur");
        } catch (InvariantViolationError e) {
            assertTrue("Invariant name in message", e.getMessage().indexOf("Bar") != -1);
        }
    }

    public void testCallingPublicMethodsWhenInvariantIsViolatedDoesNotCauseAnError() {
        class Foo {

            private boolean foo;

            public Foo() {
                bar();
                foo = true;
            }

            public void bar() {
            }

            protected boolean isInvariantFoo() {
                return foo;
            }
        }
        new Foo();
    }
}
