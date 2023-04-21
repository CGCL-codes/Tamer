package fr.inria.papyrus.uml4tst.emftext.ocl.tests;

import fr.inria.papyrus.uml4tst.emftext.ocl.BooleanLiteralExpCS;
import fr.inria.papyrus.uml4tst.emftext.ocl.OclFactory;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Boolean Literal Exp CS</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class BooleanLiteralExpCSTest extends PrimitiveLiteralExpCSTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(BooleanLiteralExpCSTest.class);
    }

    /**
	 * Constructs a new Boolean Literal Exp CS test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BooleanLiteralExpCSTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Boolean Literal Exp CS test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected BooleanLiteralExpCS getFixture() {
        return (BooleanLiteralExpCS) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(OclFactory.eINSTANCE.createBooleanLiteralExpCS());
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
