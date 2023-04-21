package fr.expression4j.test;

import fr.expression4j.test.complex.TestEvaluationWithVariable;
import fr.expression4j.test.complex.TestFunction;
import fr.expression4j.test.complex.TestVariable;
import junit.framework.TestSuite;

/**
 * do simple test like addition.
 * @author SGINER
 *
 */
public class ComplexTest extends TestSuite {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    protected void setUp() {
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(TestVariable.suite());
        suite.addTest(TestEvaluationWithVariable.suite());
        suite.addTest(TestFunction.suite());
        return suite;
    }
}
