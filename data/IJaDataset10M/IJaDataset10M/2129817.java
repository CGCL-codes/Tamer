package org.opt4j.benchmark.wfg;

import java.util.List;
import org.opt4j.benchmark.K;
import org.opt4j.benchmark.M;
import com.google.inject.Inject;

/**
 * The {@link WFGI5} benchmark function.
 * 
 * @author lukasiewycz
 * 
 */
public class WFGI5 extends WFGEvaluator {

    /**
	 * Constructs a {@link WFGI5} benchmark function.
	 * 
	 * @param k
	 *            the position parameters
	 * @param M
	 *            the number of objectives
	 */
    @Inject
    public WFGI5(@K int k, @M int M) {
        super(k, M);
    }

    @Override
    public List<Double> f(List<Double> y) {
        y = WFGI3.t1(y);
        y = WFGI1.t2(y, k);
        y = WFGI4.t3(y, k, M);
        return WFGI1.shape(y);
    }
}
