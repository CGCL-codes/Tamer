package vehikel.schema.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>VehikelConfigurationSchema</b></em>' model.
 * <!-- end-user-doc -->
 * @generated
 */
public class VehikelConfigurationSchemaAllTests extends TestSuite {

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
        TestSuite suite = new VehikelConfigurationSchemaAllTests("VehikelConfigurationSchema Tests");
        suite.addTest(SchemaTests.suite());
        return suite;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public VehikelConfigurationSchemaAllTests(String name) {
        super(name);
    }
}
