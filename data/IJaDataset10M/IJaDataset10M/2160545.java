package org.josef.science.math;

import static org.josef.annotations.Status.Stage.DEVELOPMENT;
import static org.josef.annotations.Status.UnitTests.COMPLETE;
import static org.josef.annotations.ThreadSafety.ThreadSafetyLevel.NOT_THREAD_SAFE;
import org.josef.annotations.Review;
import org.josef.annotations.Status;
import org.josef.annotations.ThreadSafety;
import org.josef.util.CDebug;

/**
 * Context to find a root of a function using different strategies.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 2840 $
 */
@Status(stage = DEVELOPMENT, unitTests = COMPLETE)
@Review(by = "Kees Schotanus", at = "2009-09-28")
@ThreadSafety(level = NOT_THREAD_SAFE)
public class RootFinderContext {

    /**
     * The strategy to find a root of a function.
     */
    private RootFinderStrategy strategy;

    /**
     * Constructs this RootFinder from the supplied strategy.
     * @param strategy The strategy to find a root of a function.
     * @throws NullPointerException When the supplied strategy is null.
     */
    public RootFinderContext(final RootFinderStrategy strategy) {
        CDebug.checkParameterNotNull(strategy, "strategy");
        this.strategy = strategy;
    }

    /**
     * Sets the strategy to find a root of a function.
     * @param strategy The strategy to find a root of a function.
     * @throws NullPointerException When the supplied strategy is null.
     */
    public void setRootFinderStrategy(final RootFinderStrategy strategy) {
        CDebug.checkParameterNotNull(strategy, "strategy");
        this.strategy = strategy;
    }

    /**
     * Finds a root of the supplied function, within the supplied interval
     * [left, right], using a {@link RootFinderStrategy}.
     * @param function The function for which a root should be found.
     * @param left x value of the left side of the interval.
     *  <br>The y value, that is the value of function(left) should have a sign
     *  that is opposite to that of function(right).
     * @param right x value of the right side of the interval.
     *  <br>The y value, that is the value of function(right) should have a sign
     *  that is opposite to that of function(left).
     * @return A root of the supplied function within the supplied interval[
     *  left, right].
     * @throws IllegalArgumentException When left equals right or when both
     *  function(left) and function(right) are of equal sign or when either
     *  function(left) or function(right) is "Not a Number".
     * @throws NullPointerException when the supplied function is null.
     */
    public double findRoot(final SingleParameterFunction function, final double left, final double right) {
        return strategy.findRoot(function, left, right);
    }
}
