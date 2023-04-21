package org.eclipse.uml2.uml.tests;

import junit.textui.TestRunner;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Interface</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.Interface#validateVisibility(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Visibility</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.Interface#createOwnedOperation(java.lang.String, org.eclipse.emf.common.util.EList, org.eclipse.emf.common.util.EList, org.eclipse.uml2.uml.Type) <em>Create Owned Operation</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.Interface#createOwnedAttribute(java.lang.String, org.eclipse.uml2.uml.Type, int, int) <em>Create Owned Attribute</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class InterfaceTest extends ClassifierTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(InterfaceTest.class);
    }

    /**
	 * Constructs a new Interface test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public InterfaceTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Interface test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected Interface getFixture() {
        return (Interface) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(UMLFactory.eINSTANCE.createInterface());
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

    /**
	 * Tests the '{@link org.eclipse.uml2.uml.Interface#validateVisibility(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Visibility</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.uml2.uml.Interface#validateVisibility(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 * @generated
	 */
    public void testValidateVisibility__DiagnosticChain_Map() {
    }

    /**
	 * Tests the '{@link org.eclipse.uml2.uml.Interface#createOwnedOperation(java.lang.String, org.eclipse.emf.common.util.EList, org.eclipse.emf.common.util.EList, org.eclipse.uml2.uml.Type) <em>Create Owned Operation</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.uml2.uml.Interface#createOwnedOperation(java.lang.String, org.eclipse.emf.common.util.EList, org.eclipse.emf.common.util.EList, org.eclipse.uml2.uml.Type)
	 * @generated
	 */
    public void testCreateOwnedOperation__String_EList_EList_Type() {
    }

    /**
	 * Tests the '{@link org.eclipse.uml2.uml.Interface#createOwnedAttribute(java.lang.String, org.eclipse.uml2.uml.Type, int, int) <em>Create Owned Attribute</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.uml2.uml.Interface#createOwnedAttribute(java.lang.String, org.eclipse.uml2.uml.Type, int, int)
	 * @generated
	 */
    public void testCreateOwnedAttribute__String_Type_int_int() {
    }
}
