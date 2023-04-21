package org.aiotrade.platform.modules.indicator.basic;

import org.aiotrade.math.timeseries.plottable.Plot;
import org.aiotrade.platform.core.analysis.indicator.AbstractContIndicator;
import org.aiotrade.platform.core.analysis.indicator.AbstractIndicator.DefaultOpt;
import org.aiotrade.platform.core.analysis.indicator.IndicatorName;
import org.aiotrade.math.timeseries.computable.Opt;
import org.aiotrade.math.timeseries.DefaultSer.DefaultVar;
import org.aiotrade.math.timeseries.Var;

/**
 *
 * @author Caoyuan Deng
 */
@IndicatorName("CCI")
public class CCIIndicator extends AbstractContIndicator {

    Opt alpha = new DefaultOpt("Alpha", 0.015);

    Opt period = new DefaultOpt("Period", 20.0);

    Opt periodMa = new DefaultOpt("Period MA", 3.0);

    Var<Float> cci = new DefaultVar("CCI", Plot.Line);

    Var<Float> cci_ma = new DefaultVar("MACCI", Plot.Line);

    {
        _sname = "CCI";
        _lname = "Commodity Channel Index";
        _grids = new Float[] { 100f, -100f };
    }

    protected void computeCont(int begIdx) {
        for (int i = begIdx; i < _itemSize; i++) {
            cci.set(i, cci(i, period, alpha));
            cci_ma.set(i, ma(i, cci, periodMa));
        }
    }
}
