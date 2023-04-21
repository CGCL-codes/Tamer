package org.eclipse.uml2.uml.tests;

import junit.textui.TestRunner;
import org.eclipse.uml2.uml.BroadcastSignalAction;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Broadcast Signal Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.BroadcastSignalAction#validateNumberAndOrder(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Number And Order</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.BroadcastSignalAction#validateTypeOrderingMultiplicity(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Type Ordering Multiplicity</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class BroadcastSignalActionTest extends InvocationActionTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(BroadcastSignalActionTest.class);
    }

    /**
	 * Constructs a new Broadcast Signal Action test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BroadcastSignalActionTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Broadcast Signal Action test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected BroadcastSignalAction getFixture() {
        return (BroadcastSignalAction) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(UMLFactory.eINSTANCE.createBroadcastSignalAction());
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
	 * Tests the '{@link org.eclipse.uml2.uml.BroadcastSignalAction#validateNumberAndOrder(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Number And Order</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.uml2.uml.BroadcastSignalAction#validateNumberAndOrder(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 * @generated
	 */
    public void testValidateNumberAndOrder__DiagnosticChain_Map() {
    }

    /**
	 * Tests the '{@link org.eclipse.uml2.uml.BroadcastSignalAction#validateTypeOrderingMultiplicity(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Type Ordering Multiplicity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.uml2.uml.BroadcastSignalAction#validateTypeOrderingMultiplicity(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 * @generated
	 */
    public void testValidateTypeOrderingMultiplicity__DiagnosticChain_Map() {
    }
}
