package org.placelab.test;

/**
 * 
 * This is how to do a test of a specific class. This is a
 * simple test to show off how to use the testing infrastructure.
 */
public class ExampleTest implements Testable {

    public String getName() {
        return "ExampleTest";
    }

    public void runTests(TestResult testResult) {
        testFailure(testResult);
        testException(testResult);
        testPass(testResult);
    }

    public void testFailure(TestResult testResult) {
    }

    public void testException(TestResult testResult) {
    }

    public void testPass(TestResult testResult) {
        Example exampleObj = new Example();
        testResult.assertTrue(this, 4, exampleObj.add(2, 2), "you better get this right");
    }
}
