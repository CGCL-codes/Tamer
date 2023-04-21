package net.sourceforge.cruisecontrol.dashboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BuildTestSuite {

    private float duration;

    private String name;

    private List testCases;

    public BuildTestSuite(String name, float duration) {
        this.duration = duration;
        this.name = name;
        this.testCases = null;
    }

    public int getNumberOfErrors() {
        return checkTestCases() ? getErrorTestCases().size() : 0;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfFailures() {
        return checkTestCases() ? getFailingTestCases().size() : 0;
    }

    public int getNumberOfTests() {
        return checkTestCases() ? testCases.size() : 0;
    }

    public float getDurationInSeconds() {
        return duration;
    }

    public List getErrorTestCases() {
        if (checkTestCases()) {
            List errorCases = new ArrayList();
            for (int i = 0; i < testCases.size(); i++) {
                BuildTestCase testCase = (BuildTestCase) testCases.get(i);
                BuildTestCaseResult result = testCase.getResult();
                if (result.equals(BuildTestCaseResult.ERROR)) {
                    errorCases.add(testCase);
                }
            }
            return errorCases;
        }
        return null;
    }

    public List getFailingTestCases() {
        if (checkTestCases()) {
            List failingCases = new ArrayList();
            for (int i = 0; i < testCases.size(); i++) {
                BuildTestCase testCase = (BuildTestCase) testCases.get(i);
                if (testCase.getResult().equals(BuildTestCaseResult.FAILED)) {
                    failingCases.add(testCase);
                }
            }
            return failingCases;
        }
        return null;
    }

    public List getPassedTestCases() {
        if (checkTestCases()) {
            List passedCases = new ArrayList();
            for (int i = 0; i < testCases.size(); i++) {
                BuildTestCase testCase = (BuildTestCase) testCases.get(i);
                BuildTestCaseResult result = testCase.getResult();
                if (result.equals(BuildTestCaseResult.PASSED)) {
                    passedCases.add(testCase);
                }
            }
            return passedCases;
        }
        return null;
    }

    public void appendTestCases(List tests) {
        this.testCases = tests;
    }

    public void addTestCase(BuildTestCase testCase) {
        if (!checkTestCases()) {
            this.testCases = new LinkedList();
        }
        this.testCases.add(testCase);
    }

    private boolean checkTestCases() {
        return this.testCases != null;
    }

    public boolean isFailed() {
        if (checkTestCases()) {
            for (Iterator iterator = testCases.iterator(); iterator.hasNext(); ) {
                BuildTestCase testCase = (BuildTestCase) iterator.next();
                if (testCase.getResult().equals(BuildTestCaseResult.ERROR) || testCase.getResult().equals(BuildTestCaseResult.FAILED)) {
                    return true;
                }
            }
        }
        return false;
    }
}
