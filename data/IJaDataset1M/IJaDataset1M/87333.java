package org.parallelj.mda.controlflow.model.controlflow.tests;

import org.parallelj.mda.controlflow.model.controlflow.ControlFlowFactory;
import org.parallelj.mda.controlflow.model.controlflow.FinalState;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Final State</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class FinalStateTest extends StateTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(FinalStateTest.class);
    }

    /**
	 * Constructs a new Final State test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FinalStateTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Final State test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected FinalState getFixture() {
        return (FinalState) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(ControlFlowFactory.eINSTANCE.createFinalState());
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
