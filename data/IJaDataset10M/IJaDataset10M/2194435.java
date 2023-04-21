package siseor.tests;

import junit.textui.TestRunner;
import siseor.DtInteger;
import siseor.SiseorFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Dt Integer</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class DtIntegerTest extends DatatypeTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(DtIntegerTest.class);
    }

    /**
	 * Constructs a new Dt Integer test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DtIntegerTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Dt Integer test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected DtInteger getFixture() {
        return (DtInteger) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(SiseorFactory.eINSTANCE.createDtInteger());
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
