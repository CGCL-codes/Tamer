package tudresden.ocl20.pivot.essentialocl.expressions.tests;

import junit.textui.TestRunner;
import tudresden.ocl20.pivot.essentialocl.expressions.UnlimitedNaturalExp;
import tudresden.ocl20.pivot.essentialocl.expressions.impl.ExpressionsFactoryImpl;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Unlimited Natural Exp</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class UnlimitedNaturalExpTest extends NumericLiteralExpTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(UnlimitedNaturalExpTest.class);
    }

    /**
	 * Constructs a new Unlimited Natural Exp test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public UnlimitedNaturalExpTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Unlimited Natural Exp test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected UnlimitedNaturalExp getFixture() {
        return (UnlimitedNaturalExp) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(ExpressionsFactoryImpl.eINSTANCE.createUnlimitedNaturalExp());
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
