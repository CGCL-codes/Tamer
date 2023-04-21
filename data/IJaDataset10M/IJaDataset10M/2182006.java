package gov.sns.tools.solver;

import java.util.*;

/**
 * SolverStopperFactory is an interface which generates a stopper. Stoppers stop the solver
 * after the specified number of evaluations have been performed.
 *
 * @author   ky6
 * @author t6p
 */
public class SolveStopperFactory {

    /**
	 * Stop the solver immediately.
	 * @return     The stopper implementation.
	 */
    public static Stopper immediateStopper() {
        return new Stopper() {

            public boolean shouldStop(Solver solver) {
                return true;
            }
        };
    }

    /**
	 * Stop the solver after the solver reaches max evaluations.
	 * @param maxEvaluations  The maximum evaluations to run the solver.
	 * @return                The stopper implementation.
	 */
    public static Stopper maxEvaluationsStopper(final int maxEvaluations) {
        return new Stopper() {

            public boolean shouldStop(Solver solver) {
                return solver.getScoreBoard().getEvaluations() >= maxEvaluations;
            }
        };
    }

    /**
	 * Get a stopper after a certain number of seconds.
	 * @param maxSeconds  The maximum number of seconds before getting the stopper.
	 * @return            A stopper.
	 */
    public static Stopper maxElapsedTimeStopper(final double maxSeconds) {
        return new Stopper() {

            public boolean shouldStop(Solver solver) {
                return solver.getScoreBoard().getElapsedTime() >= maxSeconds;
            }
        };
    }

    /**
	 * Get a stopper that runs between a minimum and maximum time and has a minimum satisfaction
	 * that all objectives must reach in order to stop short of the maximum time.
	 * @param minSeconds    The mininum number of seconds before getting stopper.
	 * @param maxSeconds    The maximum number of seconds before getting stopper.
	 * @param satisfaction  The satisfaction that must be reached by all algorithms before getting
	 *      a stopper.
	 * @return              A stopper.
	 */
    public static Stopper minMaxTimeSatisfactionStopper(final double minSeconds, final double maxSeconds, final double satisfactionTarget) {
        return new Stopper() {

            public boolean shouldStop(Solver solver) {
                final double elapsedTime = solver.getScoreBoard().getElapsedTime();
                if (elapsedTime >= maxSeconds) return true;
                if (elapsedTime < minSeconds) return false;
                final Trial bestSolution = solver.getScoreBoard().getBestSolution();
                if (bestSolution == null) return false;
                final Iterator<Objective> objectiveIter = solver.getProblem().getObjectives().iterator();
                while (objectiveIter.hasNext()) {
                    final Objective objective = objectiveIter.next();
                    double satisfaction = bestSolution.getSatisfaction(objective);
                    if (Double.isNaN(satisfaction) || satisfaction < satisfactionTarget) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    /**
	 * Get a stopper after the max number of optimal solutions is reached.
	 * @param minOptimalSolutions  The minimum number of optimal solutions
	 * @return                     The maxOptimalSolutionStopper value
	 */
    public static Stopper maxOptimalSolutionStopper(final int minOptimalSolutions) {
        return new Stopper() {

            public boolean shouldStop(Solver solver) {
                return solver.getScoreBoard().getOptimalSolutionsFound() >= minOptimalSolutions;
            }
        };
    }

    /**
	 * Compound stopper which stops the solver if either stopper1 or stopper2 would stop it.
	 * @param stopper1  The first stopper to check
	 * @param stopper2  The second stopper to check
	 * @return          A compound stopper
	 */
    public static Stopper orStopper(final Stopper stopper1, final Stopper stopper2) {
        return new Stopper() {

            public boolean shouldStop(final Solver solver) {
                return stopper1.shouldStop(solver) || stopper2.shouldStop(solver);
            }
        };
    }

    /**
	 * Compound stopper which stops the solver if both stopper1 and stopper2 would stop it.
	 * @param stopper1  The first stopper to check
	 * @param stopper2  The second stopper to check
	 * @return          A compound stopper
	 */
    public static Stopper andStopper(final Stopper stopper1, final Stopper stopper2) {
        return new Stopper() {

            public boolean shouldStop(final Solver solver) {
                return stopper1.shouldStop(solver) && stopper2.shouldStop(solver);
            }
        };
    }
}
