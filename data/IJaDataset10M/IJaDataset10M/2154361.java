package jopt.csp.test.constraint;

import jopt.csp.CspSolver;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.arcalgorithm.constraint.num.SumConstraint;
import jopt.csp.spi.arcalgorithm.constraint.num.ThreeVarConstraint;
import jopt.csp.spi.arcalgorithm.variable.DoubleVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Test for SumConstraint violation and propagation
 * 
 * @author jboerkoel
 * @author Chris Johnson
 */
public class DoubleSumConstraintTest extends TestCase {

    DoubleVariable x1;

    DoubleVariable x2;

    DoubleVariable x3;

    DoubleVariable x11;

    DoubleVariable x12;

    DoubleVariable x13;

    DoubleVariable x21;

    DoubleVariable x22;

    DoubleVariable x23;

    DoubleVariable x31;

    DoubleVariable x32;

    DoubleVariable x33;

    DoubleVariable y11;

    DoubleVariable y12;

    DoubleVariable y13;

    DoubleVariable y21;

    DoubleVariable y22;

    DoubleVariable y23;

    DoubleVariable y31;

    DoubleVariable y32;

    DoubleVariable y33;

    DoubleVariable y1;

    DoubleVariable y2;

    DoubleVariable y3;

    DoubleVariable z1;

    DoubleVariable z2;

    DoubleVariable z3;

    ConstraintStore store;

    CspVariableFactory varFactory;

    DoubleVariable y;

    DoubleVariable z;

    public void setUp() {
        store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
        store.setAutoPropagate(false);
        varFactory = store.getConstraintAlg().getVarFactory();
        x1 = new DoubleVariable("x1", 0, 100);
        x2 = new DoubleVariable("x2", 0, 100);
        x3 = new DoubleVariable("x3", 0, 100);
        x11 = new DoubleVariable("x11", 0, 100);
        x12 = new DoubleVariable("x12", 0, 100);
        x13 = new DoubleVariable("x13", 0, 100);
        x21 = new DoubleVariable("x21", 0, 100);
        x22 = new DoubleVariable("x22", 0, 100);
        x23 = new DoubleVariable("x23", 0, 100);
        x31 = new DoubleVariable("x31", 0, 100);
        x32 = new DoubleVariable("x32", 0, 100);
        x33 = new DoubleVariable("x33", 0, 100);
        y11 = new DoubleVariable("y11", 0, 100);
        y12 = new DoubleVariable("y12", 0, 100);
        y13 = new DoubleVariable("y13", 0, 100);
        y21 = new DoubleVariable("y21", 0, 100);
        y22 = new DoubleVariable("y22", 0, 100);
        y23 = new DoubleVariable("y23", 0, 100);
        y31 = new DoubleVariable("y31", 0, 100);
        y32 = new DoubleVariable("y32", 0, 100);
        y33 = new DoubleVariable("y33", 0, 100);
        y1 = new DoubleVariable("y1", 0, 100);
        y2 = new DoubleVariable("y2", 0, 100);
        y3 = new DoubleVariable("y3", 0, 100);
        z1 = new DoubleVariable("z1", 0, 100);
        z2 = new DoubleVariable("z2", 0, 100);
        z3 = new DoubleVariable("z3", 0, 100);
        y = new DoubleVariable("y", 0, 100);
        z = new DoubleVariable("z", 0, 100);
    }

    public void tearDown() {
        x1 = null;
        x2 = null;
        x3 = null;
        x11 = null;
        x12 = null;
        x13 = null;
        x21 = null;
        x22 = null;
        x23 = null;
        x31 = null;
        x32 = null;
        x33 = null;
        y11 = null;
        y12 = null;
        y13 = null;
        y21 = null;
        y22 = null;
        y23 = null;
        y31 = null;
        y32 = null;
        y33 = null;
        y1 = null;
        y2 = null;
        y3 = null;
        z1 = null;
        z2 = null;
        z3 = null;
        store = null;
        varFactory = null;
        y = null;
        z = null;
    }

    public void testSumConstraintViolationLTVarVarVarViolate() {
        SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.LT);
        assertFalse("constraint is not violated still", constraint.isViolated(false));
        try {
            x1.setDomainMin(new Double(99));
            x2.setDomainMin(new Double(98));
            x3.setDomainMax(new Double(3));
        } catch (PropagationFailureException pfe) {
            fail();
        }
        assertTrue("constraint is now violated", constraint.isViolated(false));
    }

    public void testSumConstraintViolationGEQVarVarVarNoViolate() {
        SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.GEQ);
        assertFalse("constraint is not violated still", constraint.isViolated(false));
        try {
            x1.setDomainMax(new Double(3));
            x2.setDomainMax(new Double(3));
            x3.setDomainMin(new Double(6));
        } catch (PropagationFailureException pfe) {
            fail();
        }
        assertFalse("constraint is not violated", constraint.isViolated(false));
    }

    public void testSumConstraintViolationGEQVarVarVarViolate() {
        SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.GEQ);
        assertFalse("constraint is not violated still", constraint.isViolated(false));
        try {
            x1.setDomainMax(new Double(3));
            x2.setDomainMax(new Double(3));
            x3.setDomainMin(new Double(7));
        } catch (PropagationFailureException pfe) {
            fail();
        }
        assertTrue("constraint is now violated", constraint.isViolated(false));
    }

    public void testSumConstraintViolationEQVarVarVarViolate() {
        SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.EQ);
        assertFalse("constraint is not violated still", constraint.isViolated(false));
        try {
            x1.setDomainMin(new Double(7));
            x2.setDomainMin(new Double(3));
            x3.setDomainMax(new Double(9));
        } catch (PropagationFailureException pfe) {
            fail();
        }
        assertTrue("constraint is now violated", constraint.isViolated(false));
    }

    public void testSumConstraintViolationEQVarVarVarNoViolate() {
        SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.EQ);
        assertFalse("constraint is not violated still", constraint.isViolated(false));
        try {
            x1.setDomainMin(new Double(7));
            x2.setDomainMin(new Double(3));
            x3.setDomainMax(new Double(12));
        } catch (PropagationFailureException pfe) {
            fail();
        }
        assertFalse("constraint is not violated", constraint.isViolated(false));
    }

    public void testSumConstraintViolationNEQVarVarVarViolate() {
        SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
        assertFalse("constraint is not violated still", constraint.isViolated(false));
        try {
            x1.setDomainValue(new Double(7));
            x2.setDomainValue(new Double(3));
            x3.setDomainValue(new Double(10));
        } catch (PropagationFailureException pfe) {
            fail();
        }
        assertTrue("constraint is now violated", constraint.isViolated(false));
    }

    public void testSumConstraintViolationNEQUnboundVarVarVarNoViolate() {
        SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
        assertFalse("constraint is not violated still", constraint.isViolated(false));
        try {
            x1.setDomainMin(new Double(7));
            x2.setDomainMin(new Double(3));
            x3.setDomainMax(new Double(12));
        } catch (PropagationFailureException pfe) {
            fail();
        }
        assertFalse("constraint is not violated", constraint.isViolated(false));
    }

    public void testSumConstraintViolationNEQBoundVarVarVarNoViolate() {
        SumConstraint constraint = new SumConstraint(x1, x2, x3, ThreeVarConstraint.NEQ);
        assertFalse("constraint is not violated still", constraint.isViolated(false));
        try {
            x1.setDomainValue(new Double(7));
            x2.setDomainValue(new Double(3));
            x3.setDomainValue(new Double(12));
        } catch (PropagationFailureException pfe) {
            fail();
        }
        assertFalse("constraint is not violated", constraint.isViolated(false));
    }

    public void testNormalConstNormalConstAPISum() {
        CspConstraint constraint = x1.add(x2).add(x3).add(7).eq(50);
        try {
            assertFalse("constraint is not true still", constraint.isTrue());
            assertFalse("constraint is not false still", constraint.isFalse());
            store.addConstraint(constraint);
            assertEquals("x1Max should be 100 still", 100, x1.getMax(), .00001);
            assertEquals("x2Max should be 100 still", 100, x2.getMax(), .00001);
            assertEquals("x3Max should be 100 still", 100, x3.getMax(), .00001);
            assertEquals("x1Min should be 0 still", 0, x1.getMin(), .00001);
            assertEquals("x2Min should be 0 still", 0, x2.getMin(), .00001);
            assertEquals("x3Min should be 0 still", 0, x3.getMin(), .00001);
            store.propagate();
            assertEquals("x1Min should be 0 still", 0, x1.getMin(), .00001);
            assertEquals("x2Min should be 0 still", 0, x2.getMin(), .00001);
            assertEquals("x3Min should be 0 still", 0, x3.getMin(), .00001);
            assertEquals("x1Max should be 43 now", 43, x1.getMax(), .00001);
            assertEquals("x2Max should be 43 now", 43, x2.getMax(), .00001);
            assertEquals("x3Max should be 43 now", 43, x3.getMax(), .00001);
            x1.setDomainMin(new Double(20));
            assertEquals("x1Min should be 20 still", 20, x1.getMin(), .00001);
            assertEquals("x2Min should be 0 still", 0, x2.getMin(), .00001);
            assertEquals("x3Min should be 0 still", 0, x3.getMin(), .00001);
            assertEquals("x1Max should be 43 now", 43, x1.getMax(), .00001);
            assertEquals("x2Max should be 43 now", 43, x2.getMax(), .00001);
            assertEquals("x3Max should be 43 now", 43, x3.getMax(), .00001);
            store.propagate();
            assertEquals("x1Min should be 20 still", 20, x1.getMin(), .00001);
            assertEquals("x2Min should be 0 still", 0, x2.getMin(), .00001);
            assertEquals("x3Min should be 0 still", 0, x3.getMin(), .00001);
            assertEquals("x1Max should be 43 now", 43, x1.getMax(), .00001);
            assertEquals("x2Max should be 23 now", 23, x2.getMax(), .00001);
            assertEquals("x3Max should be 23 now", 23, x3.getMax(), .00001);
            x2.setValue(5);
            x1.setValue(23);
            store.propagate();
            assertEquals("x1Min should be 23 still", 23, x1.getMin(), .00001);
            assertEquals("x2Min should be 5 still", 5, x2.getMin(), .00001);
            assertEquals("x3Min should be 15 still", 15, x3.getMin(), .00001);
            assertEquals("x1Max should be 23 now", 23, x1.getMax(), .00001);
            assertEquals("x2Max should be 5 now", 5, x2.getMax(), .00001);
            assertEquals("x3Max should be 15 now", 15, x3.getMax(), .00001);
            assertTrue("constraint should now be true", constraint.isTrue());
            assertFalse("constraint should not be false", constraint.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testNormalConstNormalConstInMiddleAPISum() {
        CspConstraint constraint = x1.add(x2).add(7).add(x3).eq(50);
        try {
            assertFalse("constraint is not true still", constraint.isTrue());
            assertFalse("constraint is not false still", constraint.isFalse());
            store.addConstraint(constraint);
            assertEquals("x1Max should be 100 still", 100, x1.getMax(), .00001);
            assertEquals("x2Max should be 100 still", 100, x2.getMax(), .00001);
            assertEquals("x3Max should be 100 still", 100, x3.getMax(), .00001);
            assertEquals("x1Min should be 0 still", 0, x1.getMin(), .00001);
            assertEquals("x2Min should be 0 still", 0, x2.getMin(), .00001);
            assertEquals("x3Min should be 0 still", 0, x3.getMin(), .00001);
            store.propagate();
            assertEquals("x1Min should be 0 still", 0, x1.getMin(), .00001);
            assertEquals("x2Min should be 0 still", 0, x2.getMin(), .00001);
            assertEquals("x3Min should be 0 still", 0, x3.getMin(), .00001);
            assertEquals("x1Max should be 43 now", 43, x1.getMax(), .00001);
            assertEquals("x2Max should be 43 now", 43, x2.getMax(), .00001);
            assertEquals("x3Max should be 43 now", 43, x3.getMax(), .00001);
            x1.setDomainMin(new Double(20));
            assertEquals("x1Min should be 20 still", 20, x1.getMin(), .00001);
            assertEquals("x2Min should be 0 still", 0, x2.getMin(), .00001);
            assertEquals("x3Min should be 0 still", 0, x3.getMin(), .00001);
            assertEquals("x1Max should be 43 now", 43, x1.getMax(), .00001);
            assertEquals("x2Max should be 43 now", 43, x2.getMax(), .00001);
            assertEquals("x3Max should be 43 now", 43, x3.getMax(), .00001);
            store.propagate();
            assertEquals("x1Min should be 20 still", 20, x1.getMin(), .00001);
            assertEquals("x2Min should be 0 still", 0, x2.getMin(), .00001);
            assertEquals("x3Min should be 0 still", 0, x3.getMin(), .00001);
            assertEquals("x1Max should be 43 now", 43, x1.getMax(), .00001);
            assertEquals("x2Max should be 23 now", 23, x2.getMax(), .00001);
            assertEquals("x3Max should be 23 now", 23, x3.getMax(), .00001);
            x2.setValue(5);
            x1.setValue(23);
            store.propagate();
            assertEquals("x1Min should be 23 still", 23, x1.getMin(), .00001);
            assertEquals("x2Min should be 5 still", 5, x2.getMin(), .00001);
            assertEquals("x3Min should be 15 still", 15, x3.getMin(), .00001);
            assertEquals("x1Max should be 23 now", 23, x1.getMax(), .00001);
            assertEquals("x2Max should be 5 now", 5, x2.getMax(), .00001);
            assertEquals("x3Max should be 15 now", 15, x3.getMax(), .00001);
            assertTrue("x1Max should be bound", x1.isBound());
            assertTrue("x2Max should be bound", x2.isBound());
            assertTrue("x3Max should be bound", x3.isBound());
            assertTrue("constraint should now be true", constraint.isTrue());
            assertFalse("constraint should not be false", constraint.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testNormalConstNormalConstInBeginAPISum() {
        CspConstraint constraint = x1.add(7).add(x2).add(x3).eq(50);
        try {
            assertFalse("constraint is not true still", constraint.isTrue());
            assertFalse("constraint is not false still", constraint.isFalse());
            store.addConstraint(constraint);
            assertEquals("x1Max should be 100 still", 100, x1.getMax(), .00001);
            assertEquals("x2Max should be 100 still", 100, x2.getMax(), .00001);
            assertEquals("x3Max should be 100 still", 100, x3.getMax(), .00001);
            assertEquals("x1Min should be 0 still", 0, x1.getMin(), .00001);
            assertEquals("x2Min should be 0 still", 0, x2.getMin(), .00001);
            assertEquals("x3Min should be 0 still", 0, x3.getMin(), .00001);
            store.propagate();
            assertEquals("x1Min should be 0 still", 0, x1.getMin(), .00001);
            assertEquals("x2Min should be 0 still", 0, x2.getMin(), .00001);
            assertEquals("x3Min should be 0 still", 0, x3.getMin(), .00001);
            assertEquals("x1Max should be 43 now", 43, x1.getMax(), .00001);
            assertEquals("x2Max should be 43 now", 43, x2.getMax(), .00001);
            assertEquals("x3Max should be 43 now", 43, x3.getMax(), .00001);
            x1.setDomainMin(new Double(20));
            assertEquals("x1Min should be 20 still", 20, x1.getMin(), .00001);
            assertEquals("x2Min should be 0 still", 0, x2.getMin(), .00001);
            assertEquals("x3Min should be 0 still", 0, x3.getMin(), .00001);
            assertEquals("x1Max should be 43 now", 43, x1.getMax(), .00001);
            assertEquals("x2Max should be 43 now", 43, x2.getMax(), .00001);
            assertEquals("x3Max should be 43 now", 43, x3.getMax(), .00001);
            store.propagate();
            assertEquals("x1Min should be 20 still", 20, x1.getMin(), .00001);
            assertEquals("x2Min should be 0 still", 0, x2.getMin(), .00001);
            assertEquals("x3Min should be 0 still", 0, x3.getMin(), .00001);
            assertEquals("x1Max should be 43 now", 43, x1.getMax(), .00001);
            assertEquals("x2Max should be 23 now", 23, x2.getMax(), .00001);
            assertEquals("x3Max should be 23 now", 23, x3.getMax(), .00001);
            x2.setValue(5);
            x1.setValue(23);
            store.propagate();
            assertEquals("x1Min should be 23 still", 23, x1.getMin(), .00001);
            assertEquals("x2Min should be 5 still", 5, x2.getMin(), .00001);
            assertEquals("x3Min should be 15 still", 15, x3.getMin(), .00001);
            assertEquals("x1Max should be 23 now", 23, x1.getMax(), .00001);
            assertEquals("x2Max should be 5 now", 5, x2.getMax(), .00001);
            assertEquals("x3Max should be 15 now", 15, x3.getMax(), .00001);
            assertTrue("x1Max should be bound", x1.isBound());
            assertTrue("x2Max should be bound", x2.isBound());
            assertTrue("x3Max should be bound", x3.isBound());
            assertTrue("constraint should now be true", constraint.isTrue());
            assertFalse("constraint should not be false", constraint.isFalse());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSumConstraintPropagationGTWithAllNegativesConst() {
        try {
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable c = new DoubleVariable("c", -100, 100);
            solver.addConstraint(a.add(-10).gt(c));
            solver.propagate();
            assertEquals("min of a is -6", -6, a.getMin(), .00001);
            assertEquals("max of a is -2", -2, a.getMax(), .00001);
            assertEquals("min of c is -100", -100, c.getMin(), .00001);
            assertEquals("max of c is -12", -12, c.getMax(), .00001);
            c.setMin(-15);
            solver.propagate();
            assertEquals("min of a is -5", -5, a.getMin(), .00001);
            assertEquals("max of a is -2", -2, a.getMax(), .00001);
            assertEquals("min of c is -15", -15, c.getMin(), .00001);
            assertEquals("max of c is -12", -12, c.getMax(), .00001);
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSumConstraintPropagationGTWithSomeNegativesConst() {
        try {
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable c = new DoubleVariable("c", -100, 100);
            solver.addConstraint(a.add(10).gt(c));
            solver.propagate();
            assertEquals("min of a is -6", -6, a.getMin(), .00001);
            assertEquals("max of a is -2", -2, a.getMax(), .00001);
            assertEquals("min of c is -100", -100, c.getMin(), .00001);
            assertEquals("max of c is 8", 8, c.getMax(), .00001);
            c.setMin(5);
            solver.propagate();
            assertEquals("min of a is -5", -5, a.getMin(), .00001);
            assertEquals("max of a is -2", -2, a.getMax(), .00001);
            assertEquals("min of c is 5", 5, c.getMin(), .00001);
            assertEquals("max of c is 8", 8, c.getMax(), .00001);
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSumConstraintPropagationGTWithNegativesAndPositivesConst() {
        try {
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            DoubleVariable a = new DoubleVariable("a", -6, 2);
            DoubleVariable c = new DoubleVariable("c", -100, 100);
            solver.addConstraint(a.add(10).gt(c));
            solver.propagate();
            assertEquals("min of a is -6", -6, a.getMin(), .00001);
            assertEquals("max of a is 2", 2, a.getMax(), .00001);
            assertEquals("min of c is -100", -100, c.getMin(), .00001);
            assertEquals("max of c is 12", 12, c.getMax(), .00001);
            c.setMin(5);
            solver.propagate();
            assertEquals("min of a is -5", -5, a.getMin(), .00001);
            assertEquals("max of a is 2", 2, a.getMax(), .00001);
            assertEquals("min of c is 5", 5, c.getMin(), .00001);
            assertEquals("max of c is 12", 12, c.getMax(), .00001);
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSumConstraintPropagationGTWithAllNegatives() {
        try {
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -4, -2);
            DoubleVariable c = new DoubleVariable("c", -100, 100);
            solver.addConstraint(a.add(b).gt(c));
            solver.propagate();
            assertEquals("min of a is -6", -6, a.getMin(), .00001);
            assertEquals("max of a is -2", -2, a.getMax(), .00001);
            assertEquals("min of b is -4", -4, b.getMin(), .00001);
            assertEquals("max of b is -2", -2, b.getMax(), .00001);
            assertEquals("min of c is -100", -100, c.getMin(), .00001);
            assertEquals("max of c is -4", -4, c.getMax(), .00001);
            c.setMin(-8);
            solver.propagate();
            assertEquals("min of a is -6", -6, a.getMin(), .00001);
            assertEquals("max of a is -2", -2, a.getMax(), .00001);
            assertEquals("min of b is -4", -4, b.getMin(), .00001);
            assertEquals("max of b is -2", -2, b.getMax(), .00001);
            assertEquals("min of c is -8", -8, c.getMin(), .00001);
            assertEquals("max of c is -4", -4, c.getMax(), .00001);
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSumConstraintPropagationGTWithSomeNegatives() {
        try {
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -2, 4);
            DoubleVariable c = new DoubleVariable("c", -100, 100);
            solver.addConstraint(a.add(b).gt(c));
            solver.propagate();
            assertEquals("min of a is -6", -6, a.getMin(), .00001);
            assertEquals("max of a is -2", -2, a.getMax(), .00001);
            assertEquals("min of b is -2", -2, b.getMin(), .00001);
            assertEquals("max of b is 4", 4, b.getMax(), .00001);
            assertEquals("min of c is -100", -100, c.getMin(), .00001);
            assertEquals("max of c is 2", 2, c.getMax(), .00001);
            c.setMin(0);
            solver.propagate();
            assertEquals("min of a is -4", -4, a.getMin(), .00001);
            assertEquals("max of a is -2", -2, a.getMax(), .00001);
            assertEquals("min of b is 2", 2, b.getMin(), .00001);
            assertEquals("max of b is 4", 4, b.getMax(), .00001);
            assertEquals("min of c is 0", 0, c.getMin(), .00001);
            assertEquals("max of c is 2", 2, c.getMax(), .00001);
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSumConstraintPropagationGTWithNegativesAndPositives() {
        try {
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            DoubleVariable a = new DoubleVariable("a", -6, 2);
            DoubleVariable b = new DoubleVariable("b", -2, 4);
            DoubleVariable c = new DoubleVariable("c", -100, 100);
            solver.addConstraint(a.add(b).gt(c));
            solver.propagate();
            assertEquals("min of a is -6", -6, a.getMin(), .00001);
            assertEquals("max of a is 2", 2, a.getMax(), .00001);
            assertEquals("min of b is -2", -2, b.getMin(), .00001);
            assertEquals("max of b is 4", 4, b.getMax(), .00001);
            assertEquals("min of c is -100", -100, c.getMin(), .00001);
            assertEquals("max of c is 6", 6, c.getMax(), .00001);
            c.setMin(0);
            solver.propagate();
            assertEquals("min of a is -4", -4, a.getMin(), .00001);
            assertEquals("max of a is 2", 2, a.getMax(), .00001);
            assertEquals("min of b is -2", -2, b.getMin(), .00001);
            assertEquals("max of b is 4", 4, b.getMax(), .00001);
            assertEquals("min of c is 0", 0, c.getMin(), .00001);
            assertEquals("max of c is 6", 6, c.getMax(), .00001);
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSumConstraintPropagationLEQWithAllNegatives() {
        try {
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", -4, -2);
            DoubleVariable c = new DoubleVariable("c", -100, 100);
            solver.addConstraint(a.add(b).leq(c));
            solver.propagate();
            assertEquals("min of a is -6", -6, a.getMin(), .00001);
            assertEquals("max of a is -2", -2, a.getMax(), .00001);
            assertEquals("min of b is -4", -4, b.getMin(), .00001);
            assertEquals("max of b is -2", -2, b.getMax(), .00001);
            assertEquals("min of c is -10", -10, c.getMin(), .00001);
            assertEquals("max of c is 100", 100, c.getMax(), .00001);
            c.setMax(-9);
            solver.propagate();
            assertEquals("min of a is -6", -6, a.getMin(), .00001);
            assertEquals("max of a is -5", -5, a.getMax(), .00001);
            assertEquals("min of b is -4", -4, b.getMin(), .00001);
            assertEquals("max of b is -3", -3, b.getMax(), .00001);
            assertEquals("min of c is -10", -10, c.getMin(), .00001);
            assertEquals("max of c is -9", -9, c.getMax(), .00001);
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSumConstraintPropagationEQWithSomeNegatives() {
        try {
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            DoubleVariable a = new DoubleVariable("a", -6, -2);
            DoubleVariable b = new DoubleVariable("b", 2, 4);
            DoubleVariable c = new DoubleVariable("c", -100, 100);
            solver.addConstraint(a.add(b).eq(c));
            solver.propagate();
            assertEquals("min of a is -6", -6, a.getMin(), .00001);
            assertEquals("max of a is -2", -2, a.getMax(), .00001);
            assertEquals("min of b is 2", 2, b.getMin(), .00001);
            assertEquals("max of b is 4", 4, b.getMax(), .00001);
            assertEquals("min of c is -4", -4, c.getMin(), .00001);
            assertEquals("max of c is 2", 2, c.getMax(), .00001);
            c.setMin(0);
            solver.propagate();
            assertEquals("min of a is -4", -4, a.getMin(), .00001);
            assertEquals("max of a is -2", -2, a.getMax(), .00001);
            assertEquals("min of b is 2", 2, b.getMin(), .00001);
            assertEquals("max of b is 4", 4, b.getMax(), .00001);
            assertEquals("min of c is 0", 0, c.getMin(), .00001);
            assertEquals("max of c is 2", 2, c.getMax(), .00001);
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSumConstraintPropagationNEQWithNegativesAndPositives() {
        try {
            CspSolver solver = CspSolver.createSolver();
            solver.setAutoPropagate(false);
            DoubleVariable a = new DoubleVariable("a", -6, 2);
            DoubleVariable b = new DoubleVariable("b", -2, 4);
            DoubleVariable c = new DoubleVariable("c", -100, 100);
            solver.addConstraint(a.add(b).neq(c));
            solver.propagate();
            assertEquals("min of a is -6", -6, a.getMin(), .00001);
            assertEquals("max of a is -2", 2, a.getMax(), .00001);
            assertEquals("min of b is -2", -2, b.getMin(), .00001);
            assertEquals("max of b is 4", 4, b.getMax(), .00001);
            assertEquals("min of c is -100", -100, c.getMin(), .00001);
            assertEquals("max of c is 100", 100, c.getMax(), .00001);
            a.setValue(-5);
            b.setValue(-1);
            solver.propagate();
            assertEquals("min of c is -100", -100, c.getMin(), .00001);
            assertEquals("max of c is 100", 100, c.getMax(), .00001);
            assertFalse("c does not contain -6", c.isInDomain(-6));
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }
}
