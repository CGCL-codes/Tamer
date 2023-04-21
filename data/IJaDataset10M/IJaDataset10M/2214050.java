package org.dishevelled.evolve;

import junit.framework.TestCase;

/**
 * Abstract unit test for implementations of Fitness.
 *
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public abstract class AbstractFitnessTest extends TestCase {

    /**
     * Create and return a new instance of Fitness to test.
     *
     * @return a new instance of Fitness to test
     */
    protected abstract <T> Fitness<T> createFitness();

    public void testCreateFitness() {
        Fitness<String> fitness = createFitness();
        assertNotNull(fitness);
    }
}
