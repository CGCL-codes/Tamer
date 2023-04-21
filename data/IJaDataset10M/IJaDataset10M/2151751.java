package default_.testpackage.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>Test</b></em>' model.
 * <!-- end-user-doc -->
 * @generated
 */
public class TestAllTests extends TestSuite {

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
        TestSuite suite = new TestAllTests("Test Tests");
        suite.addTest(TestpackageTests.suite());
        return suite;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TestAllTests(String name) {
        super(name);
    }
}
