package fr.inria.papyrus.uml4tst.emftext.alf.tests;

import fr.inria.papyrus.uml4tst.emftext.alf.AlfFactory;
import fr.inria.papyrus.uml4tst.emftext.alf.MultiplicityIndicator;
import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Multiplicity Indicator</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class MultiplicityIndicatorTest extends TestCase {

    /**
	 * The fixture for this Multiplicity Indicator test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected MultiplicityIndicator fixture = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(MultiplicityIndicatorTest.class);
    }

    /**
	 * Constructs a new Multiplicity Indicator test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public MultiplicityIndicatorTest(String name) {
        super(name);
    }

    /**
	 * Sets the fixture for this Multiplicity Indicator test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void setFixture(MultiplicityIndicator fixture) {
        this.fixture = fixture;
    }

    /**
	 * Returns the fixture for this Multiplicity Indicator test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected MultiplicityIndicator getFixture() {
        return fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(AlfFactory.eINSTANCE.createMultiplicityIndicator());
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
