package net.sourceforge.annovalidator.validation.exception;

import org.junit.Test;

public class ValidatorMethodExceptionTest {

    @Test
    public void testConstructorForIllegalArgumentException() {
        @SuppressWarnings("unused") ValidatorMethodException e = new ValidatorMethodException("validate", new Class[] { Object.class, String.class }, new IllegalArgumentException());
    }
}
