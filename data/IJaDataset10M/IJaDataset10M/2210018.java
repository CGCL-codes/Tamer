package fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.tests;

import fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.Ocl4tstFactory;
import fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.VariableDeclarationWithInit;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Variable Declaration With Init</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class VariableDeclarationWithInitTest extends VariableDeclarationTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(VariableDeclarationWithInitTest.class);
    }

    /**
	 * Constructs a new Variable Declaration With Init test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public VariableDeclarationWithInitTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Variable Declaration With Init test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected VariableDeclarationWithInit getFixture() {
        return (VariableDeclarationWithInit) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(Ocl4tstFactory.eINSTANCE.createVariableDeclarationWithInit());
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
