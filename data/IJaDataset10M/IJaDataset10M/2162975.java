package com.sf.plctest.testmodel.testpackage.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>testpackage</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class TestpackageTests extends TestSuite {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static Test suite() {
        TestSuite suite = new TestpackageTests("testpackage Tests");
        suite.addTestSuite(TestTest.class);
        suite.addTestSuite(TestParameterTest.class);
        suite.addTestSuite(ParameterValueTest.class);
        suite.addTestSuite(IntValueTest.class);
        suite.addTestSuite(BooleanValueTest.class);
        suite.addTestSuite(RealValueTest.class);
        return suite;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TestpackageTests(String name) {
        super(name);
    }
}
