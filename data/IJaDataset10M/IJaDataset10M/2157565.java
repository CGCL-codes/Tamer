package cc.mallet.optimize;

/** Optimize, constrained to move parameters along the direction of a specified line.
 * The Optimizable object would be either Optimizable.ByValue or Optimizable.ByGradient. */
public interface LineOptimizer {

    /** Returns the last step size used. */
    public double optimize(double[] line, double initialStep);

    public interface ByGradient {

        /** Returns the last step size used. */
        public double optimize(double[] line, double initialStep);
    }
}
