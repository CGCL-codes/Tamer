package mipt.math.sys.num.ae;

import mipt.math.array.Vector;
import mipt.math.sys.ae.AEProblemSolver;
import mipt.math.sys.num.Solver;

/**
 * Superinterface for linear and nonlinear numerical AE solvers (they differs only by problem)
 * Here are only options like the initial estimate and the necessity
 *   of preserving problem Numbers (they may be not instantiated as constants)    
 */
public interface AESolver extends AEProblemSolver, Solver {

    /**
	 * If false, the problem can be damaged during calculus (to save time and memory)
	 * By default is true
	 */
    void setPreserveProblem(boolean preserve);

    boolean shouldSetInitialEstimate();

    void setInitialEstimate(Vector x0);
}
