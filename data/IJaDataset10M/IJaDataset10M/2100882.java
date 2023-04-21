package picounit.java;

import picounit.java.type.Method;
import junit.framework.Test;
import junit.framework.TestResult;

class PicoUnitTestCase implements Test {

    private final Method testMethod;

    public PicoUnitTestCase(Method testMethod) {
        this.testMethod = testMethod;
    }

    public int countTestCases() {
        return 1;
    }

    public void run(TestResult testResult) {
        testResult.startTest(this);
        testMethod.invoke(testMethod.declaringType().newInstance());
        testResult.endTest(this);
    }
}
