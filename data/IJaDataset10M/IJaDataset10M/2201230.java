package org.xmlfield.validation.handlers;

public class ConstraintViolation<T> {

    String methodName;

    String expected;

    String actual;

    public ConstraintViolation(String methodName, String expected, String actual) {
        this.methodName = methodName;
        this.expected = expected;
        this.actual = actual;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getExpected() {
        return expected;
    }

    public String getActual() {
        return actual;
    }
}
