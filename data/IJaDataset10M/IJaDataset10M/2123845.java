package org.eclipse.uml2.uml.tests;

import junit.textui.TestRunner;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Connector End</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are tested:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.ConnectorEnd#getDefiningEnd() <em>Defining End</em>}</li>
 * </ul>
 * </p>
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.ConnectorEnd#validateMultiplicity(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Multiplicity</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.ConnectorEnd#validatePartWithPortEmpty(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Part With Port Empty</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.ConnectorEnd#validateRoleAndPartWithPort(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Role And Part With Port</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.ConnectorEnd#validateSelfPartWithPort(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Self Part With Port</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class ConnectorEndTest extends MultiplicityElementTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(ConnectorEndTest.class);
    }

    /**
	 * Constructs a new Connector End test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ConnectorEndTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Connector End test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected ConnectorEnd getFixture() {
        return (ConnectorEnd) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(UMLFactory.eINSTANCE.createConnectorEnd());
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
	 * Tests the '{@link org.eclipse.uml2.uml.ConnectorEnd#getDefiningEnd() <em>Defining End</em>}' feature getter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.uml2.uml.ConnectorEnd#getDefiningEnd()
	 * @generated
	 */
    public void testGetDefiningEnd() {
    }

    /**
	 * Tests the '{@link org.eclipse.uml2.uml.ConnectorEnd#validateMultiplicity(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Multiplicity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.uml2.uml.ConnectorEnd#validateMultiplicity(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 * @generated
	 */
    public void testValidateMultiplicity__DiagnosticChain_Map() {
    }

    /**
	 * Tests the '{@link org.eclipse.uml2.uml.ConnectorEnd#validatePartWithPortEmpty(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Part With Port Empty</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.uml2.uml.ConnectorEnd#validatePartWithPortEmpty(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 * @generated
	 */
    public void testValidatePartWithPortEmpty__DiagnosticChain_Map() {
    }

    /**
	 * Tests the '{@link org.eclipse.uml2.uml.ConnectorEnd#validateRoleAndPartWithPort(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Role And Part With Port</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.uml2.uml.ConnectorEnd#validateRoleAndPartWithPort(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 * @generated
	 */
    public void testValidateRoleAndPartWithPort__DiagnosticChain_Map() {
    }

    /**
	 * Tests the '{@link org.eclipse.uml2.uml.ConnectorEnd#validateSelfPartWithPort(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Self Part With Port</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.uml2.uml.ConnectorEnd#validateSelfPartWithPort(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 * @generated
	 */
    public void testValidateSelfPartWithPort__DiagnosticChain_Map() {
    }
}
