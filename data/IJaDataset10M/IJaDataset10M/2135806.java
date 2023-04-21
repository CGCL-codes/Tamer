package jopt.csp.example.api;

import jopt.csp.CspSolver;
import jopt.csp.search.SearchAction;
import jopt.csp.search.SearchActions;
import jopt.csp.search.SearchGoal;
import jopt.csp.search.SearchGoals;
import jopt.csp.variable.CspConstraint;
import jopt.csp.variable.CspIntVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;

/**
 * The intention of this class is to demonstrate the simplicity
 * and the power of the jOpt CSP module.  It should give
 * new users a high-level view of creating constraints and
 * combining constraint satisfaction with searching for optimal
 * solutions.  By running this simple example through a
 * debugger, the "curious" user will better understand the
 * "magic" that occurs beneath the surface including (but not
 * limited to) the creation of arcs from constraints, building
 * of a node-arc graph, propagating, and searching.
 * 
 * Any new developer or user of jOpt should start here - hence the
 * not-so-subtle title.
 * 
 * @author Chris Johnson
 */
public class StartHere {

    /**
     * This method will create a simple problem involving 3 variables - x, y, and z.
     * The constraint indicates that x * y <= z, and the intention is to find the
     * largest value of z satisfying the inequality.
     */
    public void run() {
        CspSolver solver = CspSolver.createSolver();
        CspVariableFactory varFactory = solver.getVarFactory();
        CspIntVariable xVar = varFactory.intVar("x", 1, 4);
        CspIntVariable yVar = varFactory.intVar("y", 1, 3);
        CspIntVariable zVar = varFactory.intVar("z", 1, 12);
        CspConstraint constraint = xVar.multiply(yVar).leq(zVar);
        try {
            solver.addConstraint(constraint);
        } catch (PropagationFailureException pfe) {
            System.out.println("Constraint was impossible to satisfy");
        }
        SearchGoals goals = solver.getSearchGoals();
        SearchGoal goal = goals.maximize(zVar);
        SearchActions actions = solver.getSearchActions();
        SearchAction action = actions.generate(new CspIntVariable[] { zVar });
        boolean success = solver.solve(action, goal);
        if (success) {
            System.out.println("We've located an optimal solution:");
            System.out.println(xVar);
            System.out.println(yVar);
            System.out.println(zVar);
        } else {
            System.out.println("We were unable to locate a valid solution");
        }
    }

    /**
     * Simple main method to get the problem runnin'
     * 
     * @param args
     */
    public static void main(String[] args) {
        new StartHere().run();
    }
}
