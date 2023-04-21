package org.niceassert.example;

import org.hamcrest.BaseMatcher;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Description;
import org.junit.Test;
import static org.niceassert.Expectation.*;
import static org.niceassert.NiceMatchers.ofTypeWithMessage;

public class ExpectationExampleTest {

    private static final String A_RETURNED_VALUE = "The returned value";

    private static final AnException AN_EXCEPTION = new AnException();

    @Test
    public void exactValueIsReturned() {
        expect(new AReturningObject()).to(returnValue(A_RETURNED_VALUE)).whenCalling().aMethod();
    }

    @Test
    public void matchedValueIsReturned() {
        expect(new AReturningObject()).to(returnValueThat(is(any(String.class)))).whenCalling().aMethod();
    }

    @Test
    public void exactExceptionIsThrown() throws AnException {
        expect(new AnExceptionThrowingObject()).to(throwException(AN_EXCEPTION)).whenCalling().aMethod();
    }

    @Test
    public void exceptionTypeAndMessageIsThrown() throws AnException {
        expect(new AnExceptionThrowingObject()).to(throwExceptionThat(is(ofTypeWithMessage(AnException.class, AnException.MESSAGE)))).whenCalling().aMethod();
    }

    @Test
    public void matchedExceptionIsThrown() throws AnException {
        expect(new AnExceptionThrowingObject()).to(throwExceptionThat(is(any(Throwable.class)))).whenCalling().aMethod();
    }

    @Test
    public void stateOfTheObjectUpdated() throws AnException {
        final AStateUpdatingObject object = new AStateUpdatingObject();
        expect(object).to(resultIn(new UpdatedTheStateOf(object))).whenCalling().aMethod();
    }

    public static class AnExceptionThrowingObject {

        public void aMethod() throws AnException {
            throw AN_EXCEPTION;
        }
    }

    public static class AReturningObject {

        public String aMethod() {
            return A_RETURNED_VALUE;
        }
    }

    public static class AStateUpdatingObject {

        private boolean called;

        public void aMethod() {
            called = true;
        }

        public boolean isCalled() {
            return called;
        }
    }

    public static class AnException extends Throwable {

        private static final String MESSAGE = "MESSAGE";

        public AnException() {
            super(MESSAGE);
        }

        public boolean equals(Object obj) {
            return obj.getClass() == this.getClass();
        }
    }

    private static class UpdatedTheStateOf extends BaseMatcher {

        private final AStateUpdatingObject object;

        public UpdatedTheStateOf(AStateUpdatingObject object) {
            this.object = object;
        }

        public boolean matches(Object o) {
            return object.isCalled();
        }

        public void describeTo(Description description) {
        }
    }
}
