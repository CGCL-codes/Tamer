package es.optsicom.lib.tablecreator.statisticcalc;

import es.optsicom.lib.util.BestMode;
import es.optsicom.lib.util.SummarizeMode;

public abstract class RelativizerStatisticCalc extends StatisticCalc {

    protected BestMode bestMode = BestMode.MAX_IS_BEST;

    protected RelativizerStatisticCalc(SummarizeMode summarizeMode, BestMode mode) {
        super(summarizeMode);
        this.bestMode = mode;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public BestMode getBestMode() {
        return bestMode;
    }

    public void setBestMode(BestMode mode) {
        this.bestMode = mode;
    }
}
