package org.fh.auge.account.tests;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.fh.auge.account.AccountFactory;
import org.fh.auge.account.Transaction;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Transaction</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class TransactionTest extends TestCase {

    /**
	 * The fixture for this Transaction test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Transaction fixture = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(TransactionTest.class);
    }

    /**
	 * Constructs a new Transaction test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TransactionTest(String name) {
        super(name);
    }

    /**
	 * Sets the fixture for this Transaction test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void setFixture(Transaction fixture) {
        this.fixture = fixture;
    }

    /**
	 * Returns the fixture for this Transaction test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected Transaction getFixture() {
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
        setFixture(AccountFactory.eINSTANCE.createTransaction());
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
