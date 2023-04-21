package fr.inria.uml4tst.papyrus.ocl4tst.ocl.tests;

import fr.inria.uml4tst.papyrus.ocl4tst.ocl.OclFactory;
import fr.inria.uml4tst.papyrus.ocl4tst.ocl.OperationCallWithImlicitSourceExpCS;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Operation Call With Imlicit Source Exp CS</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class OperationCallWithImlicitSourceExpCSTest extends OperationCallOnSelfExpCSTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(OperationCallWithImlicitSourceExpCSTest.class);
    }

    /**
	 * Constructs a new Operation Call With Imlicit Source Exp CS test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public OperationCallWithImlicitSourceExpCSTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Operation Call With Imlicit Source Exp CS test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected OperationCallWithImlicitSourceExpCS getFixture() {
        return (OperationCallWithImlicitSourceExpCS) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(OclFactory.eINSTANCE.createOperationCallWithImlicitSourceExpCS());
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
