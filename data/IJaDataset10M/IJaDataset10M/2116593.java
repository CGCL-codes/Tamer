package framework.unitTest.profiles;

import framework.unitTest.base.TestProfile;
import framework.unitTest.base.TestResult;
import framework.unitTest.base.UnitTestException;
import framework.util.Utility;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class A1TestProfile implements TestProfile {

    private TestResult testMajority(Object testee, int[] list, Integer expected) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassCastException {
        Method testMajority;
        testMajority = testee.getClass().getMethod("findMajority", int[].class);
        Integer result = (Integer) testMajority.invoke(testee, list);
        String input = "findMajority(" + Utility.intArrayToString(list) + ")";
        String expectedResult = expected.toString();
        String actualResult = result.toString();
        boolean passed = expected.equals(result);
        return new TestResult(input, expectedResult, actualResult, passed);
    }

    private TestResult testMatches(Object testee, String source, String target, ArrayList expected) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassCastException {
        Method matches;
        matches = testee.getClass().getMethod("matches", String.class, String.class);
        ArrayList result = ((ArrayList) matches.invoke(testee, source, target));
        String input = "matches(\"" + source + "\", \"" + target + "\")";
        String expectedResult = expected.toString();
        String actualResult = result.toString();
        boolean passed = result.equals(expected);
        return new TestResult(input, expectedResult, actualResult, passed);
    }

    public TestResult testMethod(Object target, String methodName, Object... args) throws UnitTestException, InvocationTargetException, ClassCastException, NoSuchMethodException, IllegalAccessException {
        if (target == null) throw new UnitTestException("Null target");
        if (methodName == null) throw new UnitTestException("Null methodName");
        if (methodName.equals("matches")) return testMatches(target, ((String) args[0]), ((String) args[1]), ((ArrayList) args[2]));
        if (methodName.equals("findMajority")) return testMajority(target, ((int[]) args[0]), ((Integer) args[1]));
        throw new UnitTestException("Test profile does not know method: " + methodName);
    }
}
