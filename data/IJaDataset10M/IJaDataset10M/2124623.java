package ugliML.inference;

import ugliML.Binding;

/**
 * Interface contract for classes returning point estimates, e.g. after Maximum A Posteriori estimation.
 * @author gbd
 */
public interface PointEstimator extends PosteriorEstimator {

    /**
	 * @return An assignment to all variables in the system representing the point estimate.
	 */
    public Binding pointEstimate();
}
