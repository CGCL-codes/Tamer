package com.seitenbau.junit.model;

/**
 * An Expected result of a Test
 */
public class DDTModelTestExpectedResult {

    private Object expectedResult;

    private Object expectedException;

    private boolean isException;

    public boolean isException() {
        return isException;
    }

    public void setException(boolean isException) {
        this.isException = isException;
    }

    public Object getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(Object expectedResult) {
        this.expectedResult = expectedResult;
    }

    public Object getExpectedException() {
        return expectedException;
    }

    public void setExpectedException(Object expectedException) {
        this.expectedException = expectedException;
    }
}
