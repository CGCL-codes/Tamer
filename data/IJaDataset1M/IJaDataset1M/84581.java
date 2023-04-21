package org.parallelj.mda.controlflow.model.controlflow.tests;

import org.parallelj.mda.controlflow.model.controlflow.AtomicTask;
import org.parallelj.mda.controlflow.model.controlflow.ControlFlowFactory;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Atomic Task</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class AtomicTaskTest extends TaskTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(AtomicTaskTest.class);
    }

    /**
	 * Constructs a new Atomic Task test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AtomicTaskTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Atomic Task test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected AtomicTask getFixture() {
        return (AtomicTask) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(ControlFlowFactory.eINSTANCE.createAtomicTask());
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
