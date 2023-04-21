package com.wideplay.junitobjects.plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.wideplay.junitobjects.pattern.PatternIntegrityException;
import com.wideplay.junitobjects.testing.StateAssertionFailedException;

/**
 * Holder class which is used to communicate results
 * from test cases to an external calling method.
 * 
 * @author chris
 *
 */
public class TestResult {

    public static final String FAIL = "fail";

    public static final String PASS = "pass";

    public static final String ERROR = "error";

    private String testName;

    private Map<String, String> tests;

    /**
	 * Sets the test name during creation of a 
	 * new test result.
	 * 
	 * @param testName
	 */
    public TestResult(String testName) {
        this.testName = testName;
        tests = new HashMap<String, String>();
    }

    /**
	 * Add a result to the test result. Results consist of
	 * a simple description which maps to a result (i. e. pass, fail, error, etc).
	 * 
	 * @param testDescription short description of the test 
	 * @param result a description of what happened in that part of the test (generally pass or fail)
	 */
    public void addResult(String testDescription, String result) {
        tests.put(testDescription, result);
    }

    public void addResult(String description, Throwable e) {
        String resultString = null;
        if (e == null) {
            resultString = TestResult.PASS;
        } else if ((e instanceof StateAssertionFailedException) || (e instanceof PatternIntegrityException)) {
            resultString = TestResult.FAIL;
        } else {
            resultString = TestResult.ERROR;
        }
        addResult(description, resultString);
    }

    public boolean isFailure() {
        return checkForResult(FAIL);
    }

    public boolean isError() {
        return checkForResult(ERROR);
    }

    protected boolean checkForResult(String resultType) {
        for (Iterator<String> iterator = tests.values().iterator(); iterator.hasNext(); ) {
            String type = (String) iterator.next();
            if (type.equals(resultType)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Get the name associated with the test the results are from.
	 * 
	 * @return test name
	 */
    public String getTestName() {
        return testName;
    }

    /**
	 * Updates that test name that is associated with these results.
	 * 
	 * @param testName name of the test which is associated with this result.
	 */
    public void setTestName(String testName) {
        this.testName = testName;
    }

    /**
	 * Get the mapping of result tests to results.
	 * @return results
	 */
    public Map<String, String> getTests() {
        return tests;
    }
}
