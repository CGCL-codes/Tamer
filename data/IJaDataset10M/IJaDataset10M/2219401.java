package net.sf.filosof.example.squares;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.sf.filosof.framework.problem.Problem;
import net.sf.filosof.framework.problem.Solution;
import net.sf.filosof.framework.problem.Transformation;
import net.sf.filosof.framework.problem.fitness.Fitness;
import net.sf.filosof.framework.solver.Solver;
import net.sf.filosof.framework.solver.SolverListener;
import net.sf.filosof.library.solver.fork.Fork;
import net.sf.filosof.library.solver.hill_climbing.HillClimbing;
import net.sf.filosof.library.solver.non_local.NonLocalSolver;
import net.sf.filosof.library.solver.random_walk.Randomizable;
import net.sf.filosof.library.solver.sequence.Sequence;
import net.sf.filosof.library.solver.util.ImprovementLogger;

/**
 * Find maximum sum of products of diagonals and anti-diagonals 
 * of a n*n matrix containing the numbers from 1 to n*n.
 * 
 * For details problem description see:
 * http://www.recmath.org/contest/Kurchan/index.php
 * 
 * @author Wisser
 */
public class MaximalPandiagonalMultiplicativeSquaresProblem extends Problem implements Randomizable {

    private static class RandomizeSmallCells extends NonLocalSolver {

        private final int n;

        protected RandomizeSmallCells(String name, int n) {
            super(name);
            this.n = n;
        }

        @Override
        protected String getDescription() {
            return "Randomize-small-cells";
        }

        @Override
        protected Solution optimize(Solution solution) {
            Square square = (Square) solution;
            int x[] = new int[square.n * square.n];
            int y[] = new int[square.n * square.n];
            for (int i = 0; i < square.n; ++i) {
                for (int j = 0; j < square.n; ++j) {
                    x[square.field[i][j].intValue() - 1] = i;
                    y[square.field[i][j].intValue() - 1] = j;
                }
            }
            Random random = new Random();
            for (int i = 0; i < n; ++i) {
                int otherI = random.nextInt(n);
                if (otherI != i) {
                    square.swap(x[i], y[i], x[otherI], y[otherI]);
                }
            }
            return square;
        }
    }

    public static void main(String args[]) {
        int n = 14;
        System.out.println("N=" + n);
        Problem problem = new MaximalPandiagonalMultiplicativeSquaresProblem(n, new Random(789234));
        int r = 1;
        Solver[] solver = new Solver[r];
        for (int i = 0; i < r; ++i) {
            List<Solver> rwHc = new ArrayList<Solver>();
            for (int j = 0; j < 10; ++j) {
                Solver rw = new RandomizeSmallCells("rsm" + j, n * n / 2);
                final Solver hc = new HillClimbing("hc" + j);
                hc.addListener(new SolverListener() {

                    public void onImprovement(long step) {
                    }

                    public void onStep(long step) {
                    }

                    public void onTermination(long step) {
                        System.out.println(hc + " terminated");
                    }
                });
                rwHc.add(rw);
                rwHc.add(hc);
            }
            solver[i] = new Sequence("s" + i, (Solver[]) rwHc.toArray(new Solver[rwHc.size()]));
        }
        Fork fork = new Fork("fork", solver);
        fork.addListener(new ImprovementLogger(fork, 1000));
        System.out.println(fork.prettyPrint());
        Square square = (Square) fork.solve(problem);
        square.checkInvariant();
        System.out.println(square);
    }

    private class Swap implements Transformation {

        public final int x1, y1, x2, y2;

        public Swap(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public void applyTo(Solution solution, Fitness newFitness) {
            Square square = (Square) solution;
            square.swap(x1, y1, x2, y2);
        }

        public Fitness rate(Solution solution) {
            Square square = (Square) solution;
            return square.rateSwap(x1, y1, x2, y2);
        }

        public boolean isApplicable(Solution solution) {
            return true;
        }

        public Collection<Transformation> getReverseTransformations() {
            return Collections.singleton((Transformation) this);
        }
    }

    ;

    private Random random;

    private List<Transformation> transformations = new ArrayList<Transformation>();

    private final int n;

    public MaximalPandiagonalMultiplicativeSquaresProblem(int n, Random random) {
        this.random = random;
        this.n = n;
        for (int x1 = 0; x1 < n; ++x1) {
            for (int y1 = 0; y1 < n; ++y1) {
                for (int x2 = 0; x2 < n; ++x2) {
                    for (int y2 = 0; y2 < n; ++y2) {
                        if (x1 + y1 * n < x2 + y2 * n) {
                            transformations.add(new Swap(x1, y1, x2, y2));
                        }
                    }
                }
            }
        }
    }

    public MaximalPandiagonalMultiplicativeSquaresProblem(int n) {
        this(n, new Random());
    }

    public Square createInitialSolution() {
        return new Square(n, random, this);
    }

    public Square createRandomSolution() {
        return new Square(n, random, this);
    }

    public Iterable<Transformation> getTransformations(Solution solution) {
        return transformations;
    }

    /**
     * Gets the fitness which an optimal solution would have.
     * 
     * A solver stops immediately after finding a solution with the
     * optimal fitness.
     * 
     * @return optimal fitness or <code>null</code> if no such fitness is defined
     */
    public Fitness getOptimalFitness() {
        return null;
    }
}
