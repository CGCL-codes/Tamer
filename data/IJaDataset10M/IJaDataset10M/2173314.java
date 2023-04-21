package org.opt4j.benchmark.lotz;

import org.opt4j.config.annotations.Info;
import org.opt4j.core.problem.ProblemModule;
import org.opt4j.start.Constant;

/**
 * <p>
 * The {@link LOTZModule} for the "Leading Ones Trailing Zeros" optimization
 * problem. This is a 2-dimensional optimization problem:
 * </p>
 * 
 * <p>
 * <ol>
 * <li>maximize the consecutive ones from the beginning of a binary string</li>
 * <li>maximize the consecutive ones from the end of a binary string</li>
 * </ol>
 * </p>
 * 
 * @author lukasiewycz
 * 
 */
@Info("The 2-dimensional \"Leading Ones Trailing Zeros\" optimization problem.")
public class LOTZModule extends ProblemModule {

    @Info("The length of the binary string.")
    @Constant(value = "size", namespace = LOTZCreator.class)
    protected int size = 30;

    /**
	 * Returns the size of the binary string.
	 * 
	 * @return the size of the binary string
	 */
    public int getSize() {
        return size;
    }

    /**
	 * Sets the size of the binary string.
	 * 
	 * @param size
	 *            the size of the binary string to set
	 */
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void config() {
        bindProblem(LOTZCreator.class, LOTZDecoder.class, LOTZEvaluator.class);
    }
}
