package org.delafer.benchmark.interfaces;

/**
 * The Interface IBenchLimited.
 * Benchmark which implements this interface will be
 * executed until the job is finshed.
 * And execution time will be measured.
 */
public interface IBenchContinuous extends IBench {

    /**
	 * Benchmark loops.
	 * values less than one - ignored and at least 1 loop executed
	 * @return loops count
	 */
    public long benchmarkLoops();

    /**
	 * Called before every / each loop.
	 */
    public void beforeLoop();

    /**
	 * Called after every / each loop.
	 */
    public void aufterLoop();

    /**
	 * Coefficient. result = (value / coefficient)
	 * @return the double
	 */
    public double coefficient();

    /**
	 * @return approxRunningTime in seconds
	 */
    public long approxRunningTime();
}
