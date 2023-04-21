package com.paullindorff.gwt.jaxrs.client.test.ui;

public class TestResult {

    private boolean passed;

    private String message = "";

    public TestResult() {
    }

    public TestResult(boolean passed) {
        this.passed = passed;
    }

    public TestResult(boolean passed, String message) {
        this.passed = passed;
        this.message = message;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
