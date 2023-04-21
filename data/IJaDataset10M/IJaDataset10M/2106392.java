package eu.sarunas.projects.atf.tests;

import java.util.ArrayList;
import java.util.List;

public class TestSuite {

    public void addTestCase(TestCase testCase) {
        this.testCases.add(testCase);
    }

    ;

    public List<TestCase> getTestCases() {
        return testCases;
    }

    ;

    private List<TestCase> testCases = new ArrayList<TestCase>();
}

;
