package batch.tasks;

import batch.BatchResultEntry;
import ds.ca.evac.EvacuationCellularAutomaton;

/**
 *
 * @author Timon Kelter
 */
public class ComputeAvgStepPerSecondTask implements Runnable {

    /** The batch object which stores the calculated results. */
    BatchResultEntry res;

    public ComputeAvgStepPerSecondTask(BatchResultEntry res) {
        this.res = res;
    }

    public void run() {
        double tmpAverageStepsPerSecond = 0.0;
        double i = 0;
        if (res.getCa() != null) {
            for (EvacuationCellularAutomaton ca : res.getCa()) {
                tmpAverageStepsPerSecond += ca.getStepsPerSecond();
            }
            res.setAverageCAStepsPerSecond(tmpAverageStepsPerSecond / res.getCa().length);
        } else {
            res.setAverageCAStepsPerSecond(0.0);
        }
        res = null;
    }
}
