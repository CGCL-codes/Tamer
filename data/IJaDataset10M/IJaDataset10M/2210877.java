package jopt.csp.test.bool;

import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.bool.BooleanEqThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.BooleanVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Tests the BooleanImpliesConstraint
 * 
 * @author Chris Johnson
 */
public class EqThreeVarConstraintTest extends TestCase {

    private BooleanVariable aVar;

    private BooleanVariable bVar;

    private BooleanVariable zVar;

    private ConstraintStore store;

    private CspConstraint constraint;

    public EqThreeVarConstraintTest(java.lang.String testName) {
        super(testName);
    }

    public void setUp() {
        store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
        store.setAutoPropagate(false);
        aVar = new BooleanVariable("a");
        bVar = new BooleanVariable("b");
        zVar = new BooleanVariable("z");
        constraint = new BooleanEqThreeVarConstraint(aVar, bVar, false, zVar);
    }

    public void tearDown() {
        store = null;
        aVar = null;
        bVar = null;
        zVar = null;
        constraint = null;
    }

    public void testSetAFalse() {
        try {
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            aVar.setFalse();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
            store.getConstraintAlg().propagate();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSetATrue() {
        try {
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            aVar.setTrue();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
            store.getConstraintAlg().propagate();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSetAFalseBFalse() {
        try {
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            aVar.setFalse();
            bVar.setFalse();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("zVar isTrue should be false", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
            store.getConstraintAlg().propagate();
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar isFalse should be false", zVar.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSetZTrue() {
        try {
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            zVar.setTrue();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
            store.getConstraintAlg().propagate();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSetZFalse() {
        try {
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            zVar.setFalse();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
            store.getConstraintAlg().propagate();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("aVar isTrue should be false", aVar.isTrue());
            assertFalse("aVar isFalse should be false", aVar.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSetZFalseATrue() {
        try {
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            zVar.setFalse();
            aVar.setTrue();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
            store.getConstraintAlg().propagate();
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertTrue("bVar isFalse should be true", bVar.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSetAFalseConstZFalse() {
        try {
            constraint = new BooleanEqThreeVarConstraint(aVar, bVar, false, false);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            aVar.setFalse();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            store.propagate();
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertTrue("bVar isTrue should be true", bVar.isTrue());
            assertFalse("bVar isFalse should be false", bVar.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSetAFalseConstZTrue() {
        try {
            constraint = new BooleanEqThreeVarConstraint(aVar, bVar, false, true);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            aVar.setFalse();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            store.propagate();
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertTrue("bVar isFalse should be true", bVar.isFalse());
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSetATrueConstZFalse() {
        try {
            constraint = new BooleanEqThreeVarConstraint(aVar, bVar, false, false);
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            aVar.setTrue();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            store.propagate();
            assertTrue("the constraint is true", constraint.isTrue());
            assertFalse("the constraint is not false", constraint.isFalse());
            assertFalse("bVar isTrue should be false", bVar.isTrue());
            assertTrue("bVar isFalse should be true", bVar.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testConstraintDissatisfaction() {
        try {
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            aVar.setTrue();
            bVar.setFalse();
            zVar.setTrue();
            assertTrue("aVar isTrue should be true", aVar.isTrue());
            assertTrue("bVar isFalse should be true", bVar.isFalse());
            assertTrue("zVar isTrue should be true", zVar.isTrue());
            assertFalse("zVar's constraint should not be true", constraint.isTrue());
            assertTrue("zVar's constraint should not false", constraint.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testPropFail() {
        try {
            store.addConstraint(constraint);
            assertFalse("the unbound constraint is not true", constraint.isTrue());
            assertFalse("the unbound constraint is not false", constraint.isFalse());
            aVar.setTrue();
            bVar.setFalse();
            zVar.setTrue();
            assertFalse("the constraint is not true", constraint.isTrue());
            assertTrue("the constraint is false", constraint.isFalse());
            store.getConstraintAlg().propagate();
            fail();
        } catch (PropagationFailureException pfe) {
        }
    }
}
