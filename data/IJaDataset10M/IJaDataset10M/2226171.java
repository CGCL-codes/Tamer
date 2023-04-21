package org.mili.jmibs.api;

import java.util.List;

/**
 * This interface defines a specially benchmark suite that is based on iteration, object loading
 * and interval benchmark. A benchmark suite that benchmarks something 10 or 1000 times in
 * interval 1 to 100 and 1 to 1000 should implementing this interface.
 *
 * @author Michael Lieshoff
 * @version 1.0 04.06.2010
 * @since 1.1
 */
public interface IterationIntervalBenchmarkSuite<I extends Interval<?>> extends BenchmarkSuite {

    /**
     * @return list with iterations.
     */
    List<Integer> getIterationList();

    /**
     * adds an iteration.
     *
     * @param iteration iteration to add.
     */
    void addIteration(int iteration);

    /**
     * @return list with intervals.
     */
    List<I> getIntervalList();

    /**
     * adds an interval.
     *
     * @param interval interval to add.
     */
    void addInterval(I interval);
}
