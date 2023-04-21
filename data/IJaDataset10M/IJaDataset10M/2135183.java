package wmh.satsolver.sc;

import wmh.satsolver.StopCondition;
import wmh.satsolver.TaskStats;

/**
 * Warunek stopu ko�czoncy dzia�anie algorytmu po okre�lonej liczbie iteracji
 */
public class NumIterationsStopCondition implements StopCondition {

    /**
     * Po ilu iteracjach zako�czy� wykonywanie algorytmu
     */
    private int maxIterations;

    public NumIterationsStopCondition(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public boolean isStopNeeded(TaskStats stats) {
        return (stats.getNumIterations() == maxIterations);
    }
}
