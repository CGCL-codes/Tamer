package net.sf.randomjunit;

import java.util.Collections;
import java.util.List;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * This class randomize the execution of tests.
 * <p>
 * Taken from
 * http://stackoverflow.com/questions/1444314/how-can-i-make-my-junit-
 * tests-run-in-random-order
 * <p>
 * <b>Control Version</b>
 * <p>
 * <ul>
 * <li>1.0.0 Class creation.</li>
 * </ul>
 *
 * @author Michael Lloyd Lee (Mlk) - Stackoverflow
 * @version 1.0.0 2011-06-13
 */
public class RandomTestRunner extends BlockJUnit4ClassRunner {

    /**
     * Please see
     * {@link org.junit.runners.BlockJUnit4ClassRunner#BlockJUnit4ClassRunner(Class)}
     * .
     *
     * @param klass
     *            Class to execute.
     * @throws InitializationError
     *             If there is an error.
     */
    public RandomTestRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        final List<FrameworkMethod> methods = super.computeTestMethods();
        Collections.shuffle(methods);
        return methods;
    }
}
