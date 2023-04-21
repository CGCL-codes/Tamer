package org.baselinetest;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestSuiteResults {

    private static final String DEFAULT_ERROR_MESSAGE = "Test Error provided no message";

    private static final String DEFAULT_ERROR_BODY = "Test Error provided no body";

    private static final String DEFAULT_ERROR_TYPE = "TestError";

    private static final String DEFAULT_FAILURE_MESSAGE = "Test Failure provided no message";

    private static final String DEFAULT_FAILURE_BODY = "Test Failure provided no body";

    private static final String DEFAULT_FAILURE_TYPE = "TestFailure";

    private String suiteName;

    private String contextName;

    private boolean suiteSkipped;

    private Double timeInSeconds = new Double(0.0);

    private List<String> testCases = new ArrayList<String>();

    private HashMap<String, Double> testCaseTimes = new HashMap<String, Double>();

    private HashMap<String, TestCaseStepFailure> testCaseErrors = new HashMap<String, TestCaseStepFailure>();

    private HashMap<String, TestCaseStepFailure> testCaseFailures = new HashMap<String, TestCaseStepFailure>();

    public TestSuiteResults(String suiteRelativePath, String contextName) {
        this.suiteName = getSuiteNameFromPath(suiteRelativePath);
        this.contextName = contextName;
        this.suiteSkipped = false;
    }

    private String getSuiteNameFromPath(String suiteRelativePath) {
        if (suiteRelativePath == null) {
            throw new IllegalArgumentException("Suite Path cannot be null");
        }
        String name;
        String separator = File.separator;
        if (suiteRelativePath.startsWith(separator)) {
            if (suiteRelativePath.length() == 1) {
                throw new IllegalArgumentException("Suite Path cannot be " + separator);
            }
            name = suiteRelativePath.substring(1);
        } else {
            name = suiteRelativePath;
        }
        name = name.replace(separator, "_");
        return name;
    }

    public void addTestCaseStep(String testCaseStepName) {
        testCases.add(testCaseStepName);
    }

    public void setTimeForTestCaseStep(String testCaseStepName, Double timeInSeconds) {
        testCaseTimes.put(testCaseStepName, timeInSeconds);
    }

    public void setTestCaseStepFailure(String testCaseStepName, String type, String message, String body) {
        if (null == type || "".equals(type)) {
            type = DEFAULT_FAILURE_TYPE;
        }
        if (null == message || "".equals(message)) {
            message = DEFAULT_FAILURE_MESSAGE;
        }
        if (null == body || "".equals(body)) {
            body = DEFAULT_FAILURE_BODY;
        }
        TestCaseStepFailure failure = new TestCaseStepFailure(type, message, body);
        testCaseFailures.put(testCaseStepName, failure);
    }

    public void setTestCaseStepFailure(String testCaseStepName, AssertionFailedError failure) {
        String type = failure.getClass().getName();
        String message = failure.getMessage();
        String body = getTrace(failure);
        setTestCaseStepFailure(testCaseStepName, type, message, body);
    }

    public void setTestCaseStepError(String testCaseStepName, String type, String message, String body) {
        if (null == type || "".equals(type)) {
            type = DEFAULT_ERROR_TYPE;
        }
        if (null == message || "".equals(message)) {
            message = DEFAULT_ERROR_MESSAGE;
        }
        if (null == body || "".equals(body)) {
            body = DEFAULT_ERROR_BODY;
        }
        TestCaseStepFailure error = new TestCaseStepFailure(type, message, body);
        testCaseErrors.put(testCaseStepName, error);
    }

    public void setTestCaseStepError(String testCaseStepName, Throwable error) {
        String type = error.getClass().getName();
        String message = error.getMessage();
        String body = getTrace(error);
        setTestCaseStepError(testCaseStepName, type, message, body);
    }

    public String getSuiteName() {
        return suiteName;
    }

    public String getContextName() {
        return contextName;
    }

    public boolean isSuiteSkipped() {
        return suiteSkipped;
    }

    public void setSuiteSkipped(boolean suiteSkipped) {
        this.suiteSkipped = suiteSkipped;
    }

    public Double getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setSuiteTime(Double timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    private String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    public List<String> getTestCases() {
        return testCases;
    }

    public HashMap<String, Double> getTestCaseTimes() {
        return testCaseTimes;
    }

    public HashMap<String, TestCaseStepFailure> getTestCaseErrors() {
        return testCaseErrors;
    }

    public HashMap<String, TestCaseStepFailure> getTestCaseFailures() {
        return testCaseFailures;
    }
}
