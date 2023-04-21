package org.opt4j.benchmark.wfg;

import java.util.ArrayList;
import java.util.List;
import org.opt4j.benchmark.K;
import org.opt4j.benchmark.M;
import com.google.inject.Inject;

/**
 * The {@link WFG8} benchmark function.
 * 
 * @author lukasiewycz
 * 
 */
public class WFG8 extends WFGEvaluator {

    /**
	 * Constructs a {@link WFG8} benchmark function.
	 * 
	 * @param k
	 *            the position parameters
	 * @param M
	 *            the number of objectives
	 */
    @Inject
    public WFG8(@K int k, @M int M) {
        super(k, M);
    }

    protected static List<Double> t1(final List<Double> y, final int k) {
        final int n = y.size();
        assert (k >= 1);
        assert (k < n);
        List<Double> w = new ArrayList<Double>();
        for (int i = 0; i < n; i++) {
            w.add(1.0);
        }
        List<Double> t = new ArrayList<Double>();
        for (int i = 0; i < k; i++) {
            t.add(y.get(i));
        }
        for (int i = k; i < n; i++) {
            final List<Double> y_sub = y.subList(0, i);
            final List<Double> w_sub = w.subList(0, i);
            final double u = WFGTransFunctions.r_sum(y_sub, w_sub);
            t.add(WFGTransFunctions.b_param(y.get(i), u, 0.98 / 49.98, 0.02, 50));
        }
        return t;
    }

    @Override
    public List<Double> f(List<Double> y) {
        y = WFG8.t1(y, k);
        y = WFG1.t1(y, k);
        y = WFG2.t3(y, k, M);
        return WFG4.shape(y);
    }
}
