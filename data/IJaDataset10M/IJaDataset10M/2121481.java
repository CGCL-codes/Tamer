package org.opt4j.benchmark.wfg;

import java.util.ArrayList;
import java.util.List;
import org.opt4j.benchmark.K;
import org.opt4j.benchmark.M;
import com.google.inject.Inject;

/**
 * The {@link WFG4} benchmark function.
 * 
 * @author lukasiewycz
 * 
 */
public class WFG5 extends WFGEvaluator {

    /**
	 * Constructs a {@link WFG4} benchmark function.
	 * 
	 * @param k
	 *            the position parameters
	 * @param M
	 *            the number of objectives
	 */
    @Inject
    public WFG5(@K int k, @M int M) {
        super(k, M);
    }

    protected static List<Double> t1(final List<Double> y) {
        final int n = y.size();
        List<Double> t = new ArrayList<Double>();
        for (int i = 0; i < n; i++) {
            t.add(WFGTransFunctions.s_decept(y.get(i), 0.35, 0.001, 0.05));
        }
        return t;
    }

    @Override
    public List<Double> f(List<Double> y) {
        y = WFG5.t1(y);
        y = WFG2.t3(y, k, M);
        return WFG4.shape(y);
    }
}
