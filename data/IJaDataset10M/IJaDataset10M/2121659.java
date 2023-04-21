package de.fraunhofer.isst.eastadl.userattributes.tests;

import de.fraunhofer.isst.eastadl.elements.tests.EAPackageableElementTest;
import de.fraunhofer.isst.eastadl.userattributes.UserAttributeElementType;
import de.fraunhofer.isst.eastadl.userattributes.UserattributesFactory;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>User Attribute Element Type</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class UserAttributeElementTypeTest extends EAPackageableElementTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(UserAttributeElementTypeTest.class);
    }

    /**
	 * Constructs a new User Attribute Element Type test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public UserAttributeElementTypeTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this User Attribute Element Type test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected UserAttributeElementType getFixture() {
        return (UserAttributeElementType) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(UserattributesFactory.eINSTANCE.createUserAttributeElementType());
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
