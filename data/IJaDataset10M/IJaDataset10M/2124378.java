package org.enml.work.tests;

import junit.textui.TestRunner;
import org.enml.work.EnmlService;
import org.enml.work.WorkFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Enml Service</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class EnmlServiceTest extends ServiceTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static final String copyright = "enml.org (C) 2007";

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(EnmlServiceTest.class);
    }

    /**
	 * Constructs a new Enml Service test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EnmlServiceTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Enml Service test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EnmlService getFixture() {
        return (EnmlService) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(WorkFactory.eINSTANCE.createEnmlService());
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
