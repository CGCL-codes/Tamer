package org.aiotrade.platform.core.analysis.function;

import org.aiotrade.math.timeseries.computable.Opt;
import org.aiotrade.math.timeseries.DefaultSer.DefaultVar;
import org.aiotrade.math.timeseries.Ser;
import org.aiotrade.math.timeseries.Var;

/**
 *
 * @author Caoyuan Deng
 */
public class ROCFunction extends AbstractFunction {

    Opt period;

    Var<Float> var;

    Var<Float> roc = new DefaultVar();

    public void set(Ser baseSer, Object... args) {
        super.set(baseSer);
        this.var = (Var<Float>) args[0];
        this.period = (Opt) args[1];
    }

    public boolean idEquals(Ser baseSer, Object... args) {
        return this._baseSer == baseSer && this.var == args[0] && this.period == args[1];
    }

    protected void computeSpot(int i) {
        if (i < period.value() - 1) {
            roc.set(i, Float.NaN);
        } else {
            float var_j = var.get(i - (int) period.value());
            float roc_i = (var_j == 0) ? 0 : ((var.get(i) - var_j) / var_j) * 100;
            roc.set(i, roc_i);
        }
    }

    public final float getRoc(long sessionId, int idx) {
        computeTo(sessionId, idx);
        return roc.get(idx);
    }
}
