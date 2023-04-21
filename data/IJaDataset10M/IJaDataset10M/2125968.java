package fr.inria.uml4tst.papyrus.ocl4tst.ocl.tests;

import fr.inria.uml4tst.papyrus.ocl4tst.ocl.MultOperationCallExpCS;
import fr.inria.uml4tst.papyrus.ocl4tst.ocl.OclFactory;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Mult Operation Call Exp CS</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class MultOperationCallExpCSTest extends OperationCallBinaryExpCSTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(MultOperationCallExpCSTest.class);
    }

    /**
	 * Constructs a new Mult Operation Call Exp CS test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MultOperationCallExpCSTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Mult Operation Call Exp CS test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected MultOperationCallExpCS getFixture() {
        return (MultOperationCallExpCS) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(OclFactory.eINSTANCE.createMultOperationCallExpCS());
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
