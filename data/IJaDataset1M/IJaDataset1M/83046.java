package fr.inria.papyrus.uml4tst.emftext.alf.tests;

import fr.inria.papyrus.uml4tst.emftext.alf.AlfFactory;
import fr.inria.papyrus.uml4tst.emftext.alf.InitializationExpression;
import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Initialization Expression</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class InitializationExpressionTest extends ExpressionTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(InitializationExpressionTest.class);
    }

    /**
	 * Constructs a new Initialization Expression test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public InitializationExpressionTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Initialization Expression test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected InitializationExpression getFixture() {
        return (InitializationExpression) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(AlfFactory.eINSTANCE.createInitializationExpression());
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
