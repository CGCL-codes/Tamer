package net.sf.ajanta.test.unit.model;

import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.ajanta.model.LPConstraint;
import net.sf.ajanta.model.LPFunction;
import net.sf.ajanta.model.LPProblem;
import net.sf.ajanta.model.LPSolution;
import net.sf.ajanta.model.LPVariable;
import net.sf.ajanta.solver.InfinitySolutionException;
import net.sf.ajanta.solver.LPSolver;
import net.sf.ajanta.solver.LPSolverFactory;

/**
 * A collection of simple LP problems
 */
public class BasicLPTests {

    /**
	 * <pre>
	 * Maximize
	 *     x + 2y
	 * Subject to:
	 *     x + y >= 6
	 * </pre>
	 */
    @Test(expected = InfinitySolutionException.class)
    public void testInfinityException() throws InfinitySolutionException {
        LPVariable x = new LPVariable("x");
        LPVariable y = new LPVariable("y");
        LPProblem problem = new LPProblem();
        LPFunction of = new LPFunction().plus(1, x).plus(2, y);
        problem.setObjectiveFunction(of);
        problem.addConstraint(new LPConstraint(1, x).plus(1, y).greaterOrEqualTo(6));
        LPSolver solver = LPSolverFactory.newSolver();
        solver.solve(problem);
    }

    /**
	 * <pre>
	 * Maximize
	 *     x + 2y + 3z
	 * Subject to:
	 *     7x + z <= 6
	 *     x + 2y <= 20
	 *     3y + 4z <= 30
	 * </pre>
	 */
    @Test
    public void testProblem1() throws InfinitySolutionException {
        LPVariable x = new LPVariable("x");
        LPVariable y = new LPVariable("y");
        LPVariable z = new LPVariable("z");
        LPProblem problem = new LPProblem();
        LPFunction of = new LPFunction().plus(1, x).plus(2, y).plus(3, z);
        problem.setObjectiveFunction(of);
        problem.addConstraint(new LPConstraint(7, x).plus(1, z).lessOrEqualTo(6));
        problem.addConstraint(new LPConstraint(1, x).plus(2, y).lessOrEqualTo(20));
        problem.addConstraint(new LPConstraint(3, y).plus(4, z).lessOrEqualTo(30));
        LPSolver solver = LPSolverFactory.newSolver();
        LPSolution solution = solver.solve(problem);
        assertEquals(22, solution.getObjectiveFunctionValue(), 0);
        assertEquals(0, solution.getSolution(x), 0);
        assertEquals(2, solution.getSolution(y), 0);
        assertEquals(6, solution.getSolution(z), 0);
    }

    /**
	 * <pre>
	 * Maximize
	 *     3x + 8y
	 * Subject to:
	 *     3x + 5y <= 300
	 *     6x + 2y <= 216
	 * </pre>
	 */
    @Test
    public void testProblem2() throws InfinitySolutionException {
        LPVariable x = new LPVariable("x");
        LPVariable y = new LPVariable("y");
        LPProblem problem = new LPProblem();
        LPFunction of = new LPFunction().plus(3, x).plus(8, y);
        problem.setObjectiveFunction(of);
        problem.addConstraint(new LPConstraint(3, x).plus(5, y).lessOrEqualTo(300));
        problem.addConstraint(new LPConstraint(6, x).plus(2, y).lessOrEqualTo(216));
        LPSolver solver = LPSolverFactory.newSolver();
        LPSolution solution = solver.solve(problem);
        assertEquals(480, solution.getObjectiveFunctionValue(), 0);
        assertEquals(0, solution.getSolution(x), 0);
        assertEquals(60, solution.getSolution(y), 0);
    }

    /**
	 * <pre>
	 * Maximize
	 *     5x + y + 4z
	 * Subject to:
	 *     x + z <= 8
	 *     y + z <= 3
	 *     x + y + z <= 5
	 * </pre>
	 */
    @Test
    public void testProblem3() throws InfinitySolutionException {
        LPVariable x = new LPVariable("x");
        LPVariable y = new LPVariable("y");
        LPVariable z = new LPVariable("z");
        LPProblem problem = new LPProblem();
        LPFunction of = new LPFunction().plus(5, x).plus(1, y).plus(4, z);
        problem.setObjectiveFunction(of);
        problem.addConstraint(new LPConstraint(1, x).plus(1, z).lessOrEqualTo(8));
        problem.addConstraint(new LPConstraint(1, y).plus(1, z).lessOrEqualTo(3));
        problem.addConstraint(new LPConstraint(1, x).plus(1, y).plus(1, z).lessOrEqualTo(5));
        LPSolver solver = LPSolverFactory.newSolver();
        LPSolution solution = solver.solve(problem);
        assertEquals(25, solution.getObjectiveFunctionValue(), 0);
        assertEquals(5, solution.getSolution(x), 0);
        assertEquals(0, solution.getSolution(y), 0);
        assertEquals(0, solution.getSolution(z), 0);
    }

    /**
	 * <pre>
	 * Maximize
	 *     4x + 3y
	 * Subject to:
	 *     7x + y <= 28
	 *     x + y <= 10
	 *     x + 6y <= 15
	 * </pre>
	 */
    @Test
    public void testProblem4() throws InfinitySolutionException {
        LPVariable x = new LPVariable("x");
        LPVariable y = new LPVariable("y");
        LPProblem problem = new LPProblem();
        LPFunction of = new LPFunction().plus(4, x).plus(3, y);
        problem.setObjectiveFunction(of);
        problem.addConstraint(new LPConstraint(7, x).plus(1, y).lessOrEqualTo(28));
        problem.addConstraint(new LPConstraint(1, x).plus(1, y).lessOrEqualTo(10));
        problem.addConstraint(new LPConstraint(1, x).plus(6, y).lessOrEqualTo(15));
        LPSolver solver = LPSolverFactory.newSolver();
        LPSolution solution = solver.solve(problem);
        assertEquals(20.56098, solution.getObjectiveFunctionValue(), 0.00001);
        assertEquals(3.73171, solution.getSolution(x), 0.00001);
        assertEquals(1.87805, solution.getSolution(y), 0.00001);
    }

    /**
	 * <pre>
	 * Maximize
	 *     4x + 3y
	 * Subject to:
	 *     x + y <= 10
	 *     x + y >= 20
	 * </pre>
	 */
    @Test
    public void testProblem5() throws InfinitySolutionException {
        LPVariable x = new LPVariable("x");
        LPVariable y = new LPVariable("y");
        LPProblem problem = new LPProblem();
        LPFunction of = new LPFunction().plus(4, x).plus(3, y);
        problem.setObjectiveFunction(of);
        problem.addConstraint(new LPConstraint(1, x).plus(1, y).lessOrEqualTo(10));
        problem.addConstraint(new LPConstraint(1, x).plus(1, y).greaterOrEqualTo(10));
        LPSolver solver = LPSolverFactory.newSolver();
        LPSolution solution = solver.solve(problem);
        assertEquals(20.56098, solution.getObjectiveFunctionValue(), 0.00001);
        assertEquals(3.73171, solution.getSolution(x), 0.00001);
        assertEquals(1.87805, solution.getSolution(y), 0.00001);
    }
}
