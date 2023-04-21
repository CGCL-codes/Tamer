package jopt.js.test.domain;

import jopt.csp.spi.SolverImpl;
import jopt.csp.variable.CspIntExpr;
import jopt.csp.spi.arcalgorithm.variable.IntVariable;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.constraint.ForwardCheckConstraint;
import jopt.js.spi.domain.resource.GranularResourceDomain;
import jopt.js.spi.domain.resource.DiscreteResourceDomain;
import jopt.js.spi.variable.ActivityExpr;
import jopt.js.spi.variable.ResourceExpr;
import junit.framework.TestCase;

/**
 * @author James Boerkoel
 */
public class ActivityTest extends TestCase {

    ActivityExpr act1;

    ActivityExpr act2;

    ConstraintStore store;

    ForwardCheckConstraint con1;

    ForwardCheckConstraint con2;

    ForwardCheckConstraint con3;

    CspIntExpr startTime1;

    CspIntExpr endTime1;

    CspIntExpr duration1;

    CspIntExpr startTime2;

    CspIntExpr endTime2;

    CspIntExpr duration2;

    public ActivityTest(String testName) {
        super(testName);
    }

    public void tearDown() {
        act1 = null;
        act2 = null;
        store = null;
        con1 = null;
        con2 = null;
        con3 = null;
        startTime1 = null;
        endTime1 = null;
        duration1 = null;
        startTime2 = null;
        endTime2 = null;
        duration2 = null;
    }

    public void setUp() {
        try {
            store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
            ResourceExpr res1 = new ResourceExpr("res1", new DiscreteResourceDomain(0, 100, 1));
            ResourceExpr res2 = new ResourceExpr("res2", new DiscreteResourceDomain(0, 100, 1));
            ResourceExpr res3 = new ResourceExpr("res3", new DiscreteResourceDomain(0, 100, 1));
            act1 = new ActivityExpr("act1", 1, 0, 110, 6, 6);
            act2 = new ActivityExpr("act2", 2, 0, 110, 6, 6);
            con1 = (ForwardCheckConstraint) act1.require(new ResourceExpr[] { res1, res2 }, 1);
            con2 = (ForwardCheckConstraint) act1.require(new ResourceExpr[] { res2, res3 }, 1);
            con3 = (ForwardCheckConstraint) act2.require(new ResourceExpr[] { res1, res2, res3 }, 1);
            store.addConstraint(con1);
            store.addConstraint(con2);
            store.addConstraint(con3);
            startTime1 = act1.getStartTimeExpr();
            endTime1 = act1.getEndTimeExpr();
            duration1 = act1.getDurationExpr();
            startTime2 = act2.getStartTimeExpr();
            endTime2 = act2.getEndTimeExpr();
            duration2 = act2.getDurationExpr();
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testSetup() {
        assertEquals(0, act1.getEarliestStartTime());
        assertEquals(5, act1.getEarliestEndTime());
        assertEquals(0, act2.getEarliestStartTime());
        assertEquals(5, act2.getEarliestEndTime());
        assertEquals(95, act1.getLatestStartTime());
        assertEquals(100, act1.getLatestEndTime());
        assertEquals(95, act2.getLatestStartTime());
        assertEquals(100, act2.getLatestEndTime());
        assertEquals(0, startTime1.getMin());
        assertEquals(95, startTime1.getMax());
        assertEquals(5, endTime1.getMin());
        assertEquals(100, endTime1.getMax());
        assertEquals(6, duration1.getMin());
        assertEquals(6, duration1.getMax());
        assertEquals(0, startTime2.getMin());
        assertEquals(95, startTime2.getMax());
        assertEquals(5, endTime2.getMin());
        assertEquals(100, endTime2.getMax());
        assertEquals(6, duration2.getMin());
        assertEquals(6, duration2.getMax());
        assertEquals(2, act1.getAvailResourceCount(con1.getOperationID()));
        assertEquals(2, act1.getAvailResourceCount(con2.getOperationID()));
        assertEquals(0, act1.getAvailResourceCount(con3.getOperationID()));
        assertEquals(0, act2.getAvailResourceCount(con1.getOperationID()));
        assertEquals(0, act2.getAvailResourceCount(con2.getOperationID()));
        assertEquals(3, act2.getAvailResourceCount(con3.getOperationID()));
    }

    public void testExternalStartTimeConstraint() {
        try {
            IntVariable tempVar = new IntVariable("temp", -100, 200);
            IntVariable eqVar = new IntVariable("eqVar", 0, 300);
            CspConstraint constraint = eqVar.eq(startTime1.add(tempVar));
            store.addConstraint(constraint);
            store.propagate();
            assertEquals(0, act1.getEarliestStartTime());
            assertEquals(95, act1.getLatestStartTime());
            assertEquals(5, act1.getEarliestEndTime());
            assertEquals(100, act1.getLatestEndTime());
            assertEquals(0, eqVar.getMin());
            assertEquals(295, eqVar.getMax());
            tempVar.setMin(50);
            assertEquals(0, act1.getEarliestStartTime());
            assertEquals(95, act1.getLatestStartTime());
            assertEquals(5, act1.getEarliestEndTime());
            assertEquals(100, act1.getLatestEndTime());
            assertEquals(50, eqVar.getMin());
            assertEquals(295, eqVar.getMax());
            eqVar.setMax(140);
            assertEquals(0, act1.getEarliestStartTime());
            assertEquals(90, act1.getLatestStartTime());
            assertEquals(5, act1.getEarliestEndTime());
            assertEquals(95, act1.getLatestEndTime());
            assertEquals(50, tempVar.getMin());
            assertEquals(140, tempVar.getMax());
            assertEquals(50, eqVar.getMin());
            assertEquals(140, eqVar.getMax());
            eqVar.setValue(78);
            assertEquals(0, act1.getEarliestStartTime());
            assertEquals(28, act1.getLatestStartTime());
            assertEquals(5, act1.getEarliestEndTime());
            assertEquals(33, act1.getLatestEndTime());
            assertEquals(50, tempVar.getMin());
            assertEquals(78, tempVar.getMax());
            assertEquals(78, eqVar.getMin());
            assertEquals(78, eqVar.getMax());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testBindingIntExprWithActivity() {
        try {
            IntVariable diff = new IntVariable("diff", -200, 200);
            CspConstraint constraint = endTime1.subtract(startTime1).eq(diff);
            store.addConstraint(constraint);
            assertEquals(-90, diff.getMin());
            assertEquals(100, diff.getMax());
            act1.setEarliestStartTime(40);
            act1.setLatestEndTime(100);
            assertEquals(-50, diff.getMin());
            assertEquals(60, diff.getMax());
            act1.setStartTime(43);
            assertEquals(5, diff.getMin());
            assertEquals(5, diff.getMax());
        } catch (PropagationFailureException pfe) {
            fail();
        }
    }

    public void testAll() {
        tearDown();
        store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
        ActivityExpr act1A = new ActivityExpr("act1A", 96, 20, 60, 21, 21);
        ActivityExpr act1B = new ActivityExpr("act1B", 97, 0, 100, 1, 1);
        ActivityExpr act2A = new ActivityExpr("act2A", 98, 25, 65, 31, 31);
        ActivityExpr act2B = new ActivityExpr("act2B", 99, 0, 100, 1, 1);
        ResourceExpr res1 = new ResourceExpr("res1", new DiscreteResourceDomain(0, 200, 1));
        ResourceExpr res2 = new ResourceExpr("res2", new DiscreteResourceDomain(0, 200, 1));
        ResourceExpr res3 = new ResourceExpr("res3", new GranularResourceDomain(new DiscreteResourceDomain(10, 50, 1), 4, 0));
        try {
            store.addConstraint(act1A.startsAtStartOf(act1B));
            store.addConstraint(act2A.startsAtStartOf(act2B));
            ForwardCheckConstraint con1 = (ForwardCheckConstraint) act1A.require(new ResourceExpr[] { res1, res2 }, 1);
            store.addConstraint(con1);
            ForwardCheckConstraint con2 = (ForwardCheckConstraint) act2A.require(new ResourceExpr[] { res1, res2 }, 1);
            store.addConstraint(con2);
            store.addConstraint(act1B.require(new ResourceExpr[] { res3 }, 1));
            store.addConstraint(act2B.require(new ResourceExpr[] { res3 }, 1));
            act1A.setBuilt(true);
            act1B.setBuilt(true);
            act2A.setBuilt(true);
            act2B.setBuilt(true);
            res1.setBuilt(true);
            res2.setBuilt(true);
            res3.setBuilt(true);
            assertEquals(20, act1A.getEarliestStartTime());
            assertEquals(50, act1A.getLatestStartTime());
            assertEquals(20, act1B.getEarliestStartTime());
            assertEquals(50, act1B.getLatestStartTime());
            assertEquals(25, act2A.getEarliestStartTime());
            assertEquals(50, act2A.getLatestStartTime());
            assertEquals(25, act2B.getEarliestStartTime());
            assertEquals(50, act2B.getLatestStartTime());
            act1A.setStartTime(25);
            assertFalse(act1A.isBound());
            assertEquals(25, act1A.getEarliestStartTime());
            assertEquals(25, act1A.getLatestStartTime());
            assertEquals(25, act1B.getEarliestStartTime());
            assertEquals(25, act1B.getLatestStartTime());
            assertEquals(28, act2A.getEarliestStartTime());
            assertEquals(50, act2A.getLatestStartTime());
            assertEquals(28, act2B.getEarliestStartTime());
            assertEquals(50, act2B.getLatestStartTime());
            act1A.setRequiredResource(con1.getOperationID(), res1.getID());
            assertTrue(act1A.isBound());
            assertEquals(25, act1A.getEarliestStartTime());
            assertEquals(25, act1A.getLatestStartTime());
            assertEquals(25, act1B.getEarliestStartTime());
            assertEquals(25, act1B.getLatestStartTime());
            assertEquals(28, act2A.getEarliestStartTime());
            assertEquals(50, act2A.getLatestStartTime());
            assertEquals(28, act2B.getEarliestStartTime());
            assertEquals(50, act2B.getLatestStartTime());
            act2A.setRequiredResource(con2.getOperationID(), res1.getID());
            assertTrue(act1A.isBound());
            assertEquals(25, act1A.getEarliestStartTime());
            assertEquals(25, act1A.getLatestStartTime());
            assertEquals(25, act1B.getEarliestStartTime());
            assertEquals(25, act1B.getLatestStartTime());
            assertEquals(46, act2A.getEarliestStartTime());
            assertEquals(50, act2A.getLatestStartTime());
            assertEquals(46, act2B.getEarliestStartTime());
            assertEquals(50, act2B.getLatestStartTime());
        } catch (PropagationFailureException pfe) {
            pfe.printStackTrace();
            fail(pfe.getLocalizedMessage());
        }
    }
}
