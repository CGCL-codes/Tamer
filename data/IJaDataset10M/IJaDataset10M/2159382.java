package org.eclipse.uml2.uml.tests;

import junit.textui.TestRunner;
import org.eclipse.uml2.uml.DurationConstraint;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Duration Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.DurationConstraint#validateFirstEventMultiplicity(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate First Event Multiplicity</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class DurationConstraintTest extends IntervalConstraintTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(DurationConstraintTest.class);
    }

    /**
	 * Constructs a new Duration Constraint test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DurationConstraintTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Duration Constraint test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected DurationConstraint getFixture() {
        return (DurationConstraint) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(UMLFactory.eINSTANCE.createDurationConstraint());
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
	 * Tests the '{@link org.eclipse.uml2.uml.DurationConstraint#validateFirstEventMultiplicity(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate First Event Multiplicity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.uml2.uml.DurationConstraint#validateFirstEventMultiplicity(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 * @generated
	 */
    public void testValidateFirstEventMultiplicity__DiagnosticChain_Map() {
    }
}
