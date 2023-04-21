package jopt.js.test.solver;

import jopt.csp.CspSolver;
import jopt.csp.variable.PropagationFailureException;
import jopt.js.spi.domain.resource.UnaryResourceDomain;
import jopt.js.spi.search.actions.GenerateActivityResourceAction;
import jopt.js.spi.search.actions.GenerateActivityStartTimeAction;
import jopt.js.spi.variable.ActivityExpr;
import jopt.js.spi.variable.ResourceExpr;
import junit.framework.TestCase;

/**
 * @author jboerkoel
 */
public class GenerateResourceTest extends TestCase {

    CspSolver solver;

    ActivityExpr[] activities;

    ResourceExpr[] resources;

    public GenerateResourceTest(String testName) {
        super(testName);
    }

    public void setUp() {
        solver = CspSolver.createSolver();
        activities = new ActivityExpr[5];
        resources = new ResourceExpr[20];
        for (int i = 0; i < resources.length; i++) {
            resources[i] = new ResourceExpr("res" + i, new UnaryResourceDomain(0, 100));
        }
        activities[0] = new ActivityExpr("a", 1, 0, 100, 5, 5);
        activities[1] = new ActivityExpr("b", 2, 0, 100, 5, 5);
        activities[2] = new ActivityExpr("c", 3, 0, 100, 5, 5);
        activities[3] = new ActivityExpr("d", 4, 0, 100, 5, 5);
        activities[4] = new ActivityExpr("e", 5, 0, 100, 5, 5);
        try {
            for (int i = 0; i < activities.length - 1; i++) {
                solver.addConstraint(activities[i].endsBeforeStartOf((ActivityExpr) activities[i + 1]));
            }
            for (int i = 0; i < activities.length; i++) {
                solver.addConstraint(activities[i].require(resources, 1));
            }
        } catch (PropagationFailureException e) {
            fail();
        }
    }

    public void setUp2() {
        try {
            for (int i = 0; i < activities.length; i++) {
                solver.addConstraint(activities[i].require(resources, 1));
            }
        } catch (PropagationFailureException e) {
            fail();
        }
    }

    public void testGenerateResource() {
        boolean generatedStartTimes = solver.solve(new GenerateActivityResourceAction(activities), false);
        assertTrue(generatedStartTimes);
        for (int i = 0; i < activities.length; i++) {
            assertTrue(activities[i].operationsAssigned());
        }
    }

    public void testGenerateResourceWithMultOps() {
        setUp2();
        boolean generatedStartTimes = solver.solve(new GenerateActivityResourceAction(activities), false);
        assertTrue(generatedStartTimes);
        for (int i = 0; i < activities.length; i++) {
            assertTrue(activities[i].operationsAssigned());
        }
    }

    public void testGenerateResourceThenStartTimes() {
        boolean generatedResources = solver.solve(new GenerateActivityResourceAction(activities), false);
        assertTrue(generatedResources);
        for (int i = 0; i < activities.length; i++) {
            assertTrue(activities[i].operationsAssigned());
        }
        boolean generatedStartTimes = solver.solve(new GenerateActivityStartTimeAction(activities), false);
        assertTrue(generatedStartTimes);
        for (int i = 0; i < activities.length; i++) {
            assertTrue(activities[i].isBound());
        }
    }

    public void testGenerateResourceThenStartTimesWithMultOps() {
        boolean generatedResources = solver.solve(new GenerateActivityResourceAction(activities), false);
        assertTrue(generatedResources);
        for (int i = 0; i < activities.length; i++) {
            assertTrue(activities[i].operationsAssigned());
        }
        boolean generatedStartTimes = solver.solve(new GenerateActivityStartTimeAction(activities), false);
        assertTrue(generatedStartTimes);
        for (int i = 0; i < activities.length; i++) {
            assertTrue(activities[i].isBound());
        }
    }

    public void testGenerateStartTimesThenResource() {
        boolean generatedStartTimes = solver.solve(new GenerateActivityStartTimeAction(activities), false);
        assertTrue(generatedStartTimes);
        for (int i = 0; i < activities.length; i++) {
            assertTrue(activities[i].getStartTimeExpr().isBound());
        }
        boolean generatedResources = solver.solve(new GenerateActivityResourceAction(activities), false);
        assertTrue(generatedResources);
        for (int i = 0; i < activities.length; i++) {
            assertTrue(activities[i].isBound());
        }
    }

    public void testGenerateStartTimesThenResourceWithMultOps() {
        setUp2();
        boolean generatedStartTimes = solver.solve(new GenerateActivityStartTimeAction(activities), false);
        assertTrue(generatedStartTimes);
        for (int i = 0; i < activities.length; i++) {
            assertTrue(activities[i].getStartTimeExpr().isBound());
        }
        boolean generatedResources = solver.solve(new GenerateActivityResourceAction(activities), false);
        assertTrue(generatedResources);
        for (int i = 0; i < activities.length; i++) {
            assertTrue(activities[i].isBound());
        }
    }
}
