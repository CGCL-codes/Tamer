package org.matsim.stats.algorithms;

/**
 * @author laemmel
 *
 */
public class PlanScoreTrajectory extends AbstractPlanStatsAlgorithm {

    private double[] TRAJECTORY;

    private int minIteration;

    private int iterations;

    public PlanScoreTrajectory(int iters, int minIter) {
        minIteration = minIter;
        iterations = iters;
        init();
    }

    public PlanScoreTrajectory(PlanStatsI nextAlgo, int iters, int minIter) {
        minIteration = minIter;
        iterations = iters;
        this.nextAlgorithm = nextAlgo;
        init();
    }

    private void init() {
        TRAJECTORY = new double[iterations];
        for (int i = 0; i < iterations; i++) TRAJECTORY[i] = 0;
    }

    @Override
    public void printStats() {
        for (int i = 0; i < iterations; i++) System.out.print(" " + TRAJECTORY[i]);
    }

    @Override
    public void update(double score, int iteration) {
        TRAJECTORY[iteration - minIteration] = score;
    }

    @Override
    public String printStrStats() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            str.append(' ');
            str.append(TRAJECTORY[i]);
        }
        return str.toString();
    }
}
