package org.blogtrader.platform.modules.indicator.basic;

import org.blogtrader.platform.core.data.Chart;
import org.blogtrader.platform.core.analysis.indicator.AbstractContIndicator;
import org.blogtrader.platform.core.analysis.indicator.AbstractIndicator.P;
import org.blogtrader.platform.core.analysis.indicator.AbstractIndicator.Var;
import org.blogtrader.platform.core.analysis.indicator.IndicatorName;

/**
 *
 * @author Caoyuan Deng
 */
@IndicatorName("MFI")
public class MFIIndicator extends AbstractContIndicator {

    {
        _sname = "MFI";
        _lname = "Money Flow Index";
        _grids = new Float[] { 30f, 70f };
    }

    P period = new P("Period", 10.0);

    Var<Float> tp = new Var("", Chart.NONE);

    Var<Float> pmf = new Var("", Chart.NONE);

    Var<Float> nmf = new Var("", Chart.NONE);

    Var<Float> mfi = new Var("MFI", Chart.LINE, 0);

    protected void computeCont(int fromIdx) {
        for (int i = fromIdx; i < _dataSize; i++) {
            if (i == 0) {
                tp.set(i, 0f);
                pmf.set(i, 0f);
                nmf.set(i, 0f);
                mfi.set(i, 0f);
            } else {
                tp.set(i, (H.get(i) + C.get(i) + L.get(i)) / 3f);
                if (tp.get(i) > tp.get(i - 1)) {
                    pmf.set(i, tp.get(i) * V.get(i));
                    nmf.set(i, 0f);
                } else if (tp.get(i) < tp.get(i - 1)) {
                    pmf.set(i, 0f);
                    nmf.set(i, tp.get(i) * V.get(i));
                } else {
                    pmf.set(i, 0f);
                    nmf.set(i, 0f);
                }
                float sum_pmf = sum(i, pmf, period.value());
                float sum_nmf = sum(i, nmf, period.value());
                float mr = sum_pmf / sum_nmf;
                mfi.set(i, 100 / (1 + mr));
            }
        }
    }
}
