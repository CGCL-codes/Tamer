package org.contract.ist.schema.ist.contract.tests;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.contract.ist.schema.ist.contract.ContractFactory;
import org.contract.ist.schema.ist.contract.ContractRef;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Ref</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class ContractRefTest extends TestCase {

    /**
	 * The fixture for this Ref test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ContractRef fixture = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(ContractRefTest.class);
    }

    /**
	 * Constructs a new Ref test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ContractRefTest(String name) {
        super(name);
    }

    /**
	 * Sets the fixture for this Ref test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void setFixture(ContractRef fixture) {
        this.fixture = fixture;
    }

    /**
	 * Returns the fixture for this Ref test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ContractRef getFixture() {
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
        setFixture(ContractFactory.eINSTANCE.createContractRef());
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
