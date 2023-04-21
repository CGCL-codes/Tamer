package edu.yale.csgp.vitapad.tests;

import java.util.Stack;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class JythonTestCaseExtractor extends TestCase {

    protected static Stack __stack = new Stack();

    static class TestDesc {

        private String __dir = null;

        private String __module = null;

        private String __test = null;

        public String getDir() {
            return __dir;
        }

        public String getModule() {
            return __module;
        }

        public String getTest() {
            return __test;
        }

        public TestDesc(String dir, String module, String test) {
            __dir = dir;
            __module = module;
            __test = test;
        }
    }

    public static void addPythonTest(String dir, String module, String test) {
        __stack.push(new TestDesc(dir, module, test));
    }

    public static Test suite() {
        TestDesc desc = null;
        TestSuite suite = new TestSuite();
        while (!__stack.isEmpty()) {
            desc = (TestDesc) (__stack.pop());
            TestSuite localSuite = new JythonTestSuiteExtractor(desc.getDir(), desc.getModule(), desc.getTest());
            suite.addTest(localSuite);
        }
        if (suite.countTestCases() == 0) suite.addTest(new JythonTestCaseExtractor("testPythonIsEmpty"));
        return suite;
    }

    public void testPythonIsEmpty() {
        System.err.println("No test methods were found in class " + getClass());
        fail();
    }

    public JythonTestCaseExtractor(String name) {
        super(name);
    }
}
