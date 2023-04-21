package pulsarhunter.processes.folding;

import pulsarhunter.FoldingModel;

/**
 *
 * @author mkeith
 */
public class FixedPeriodFoldingModel implements FoldingModel {

    private double period;

    /** Creates a new instance of FixedPeriodFoldingModel */
    public FixedPeriodFoldingModel(double period) {
        this.period = period;
    }

    public double getPeriod(double mjd) {
        return period;
    }

    public double getRefreshTime() {
        return Double.MAX_VALUE;
    }
}
