package de.fraunhofer.isst.eastadl.datatypes.tests;

import de.fraunhofer.isst.eastadl.datatypes.DatatypesFactory;
import de.fraunhofer.isst.eastadl.datatypes.Enumeration;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Enumeration</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class EnumerationTest extends EADatatypeTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(EnumerationTest.class);
    }

    /**
	 * Constructs a new Enumeration test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EnumerationTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Enumeration test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected Enumeration getFixture() {
        return (Enumeration) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(DatatypesFactory.eINSTANCE.createEnumeration());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
    @Override
    protected void tearDown() throws Exception {
        setFixture(null);
    }
}
