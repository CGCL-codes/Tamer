package org.eclipse.uml2.uml.tests;

import junit.textui.TestRunner;
import org.eclipse.uml2.uml.TemplateParameter;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Template Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following operations are tested:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.TemplateParameter#validateMustBeCompatible(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Must Be Compatible</em>}</li>
 * </ul>
 * </p>
 * @generated
 */
public class TemplateParameterTest extends ElementTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(TemplateParameterTest.class);
    }

    /**
	 * Constructs a new Template Parameter test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TemplateParameterTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Template Parameter test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected TemplateParameter getFixture() {
        return (TemplateParameter) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(UMLFactory.eINSTANCE.createTemplateParameter());
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
	 * Tests the '{@link org.eclipse.uml2.uml.TemplateParameter#validateMustBeCompatible(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate Must Be Compatible</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.uml2.uml.TemplateParameter#validateMustBeCompatible(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 * @generated
	 */
    public void testValidateMustBeCompatible__DiagnosticChain_Map() {
    }
}
