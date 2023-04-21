package org.enml.subjects.tests;

import junit.textui.TestRunner;
import org.enml.subjects.Authority;
import org.enml.subjects.SubjectsFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Authority</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class AuthorityTest extends SubjectTest {

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
        TestRunner.run(AuthorityTest.class);
    }

    /**
	 * Constructs a new Authority test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AuthorityTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Authority test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected Authority getFixture() {
        return (Authority) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(SubjectsFactory.eINSTANCE.createAuthority());
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
