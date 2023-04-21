package mipt.math.sys.num.ae.iter;

import mipt.math.array.Vector;
import mipt.math.sys.ae.AEProblem;
import mipt.math.sys.num.Algorithm;

/**
 * Algorithm for IterativeAESolver: 1) do 1 iteration 2) return current deviation (Xnext-X)
 *  to help some ConvergenceAlgorithms compare error it with tolerance without cloning vectors
 * @author Evdokimov
 */
public interface IterativeAEAlgorithm extends Algorithm {

    /**
	 * x (previous solution estimate) must not be changed here
	 * If you use derivative here, it must be calculated only after function itself   
	 */
    public abstract Vector calcStep(AEProblem problem, Vector x, boolean willCalcDeviation);

    /**
	 * Will be called only if calcStep(x, true) is called before
	 * Return deviation vector xNext-x (if possible, reuse one calculated in calcStep())
	 */
    public abstract Vector getDeviationVector(Vector xNext, Vector x);
}
