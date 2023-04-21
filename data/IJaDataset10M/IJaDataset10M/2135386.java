package fr.inria.uml4tst.papyrus.ocl4tst.ocl.tests;

import fr.inria.uml4tst.papyrus.ocl4tst.ocl.InvariantExpCS;
import fr.inria.uml4tst.papyrus.ocl4tst.ocl.OclFactory;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Invariant Exp CS</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class InvariantExpCSTest extends InvariantOrDefinitionCSTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(InvariantExpCSTest.class);
    }

    /**
	 * Constructs a new Invariant Exp CS test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public InvariantExpCSTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Invariant Exp CS test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected InvariantExpCS getFixture() {
        return (InvariantExpCS) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(OclFactory.eINSTANCE.createInvariantExpCS());
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
