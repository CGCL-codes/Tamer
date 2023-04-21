package org.dynamo.database.reverse.emf.metadata.tests;

import junit.textui.TestRunner;
import org.dynamo.database.reverse.emf.metadata.MetadataFactory;
import org.dynamo.database.reverse.emf.metadata.PrimaryKey;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Primary Key</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class PrimaryKeyTest extends KeyTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(PrimaryKeyTest.class);
    }

    /**
	 * Constructs a new Primary Key test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PrimaryKeyTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Primary Key test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected PrimaryKey getFixture() {
        return (PrimaryKey) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(MetadataFactory.eINSTANCE.createPrimaryKey());
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
