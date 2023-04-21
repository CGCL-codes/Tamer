package org.dishevelled.evolve.fitness;

import java.util.Random;
import junit.framework.TestCase;
import org.dishevelled.evolve.Fitness;

/**
 * Unit test for RandomFitness.
 *
 * @author  Michael Heuer
 * @version $Revision: 225 $ $Date: 2007-01-08 23:25:51 -0600 (Mon, 08 Jan 2007) $
 */
public final class RandomFitnessTest extends TestCase {

    public void testConstructor() {
        Fitness<Integer> fitness0 = new RandomFitness<Integer>();
        Fitness<Integer> fitness1 = new RandomFitness<Integer>(new Random());
        try {
            Fitness<Integer> fitness = new RandomFitness<Integer>(null);
            fail("ctr(null) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testRandomFitness() {
        Fitness<Integer> fitness = new RandomFitness<Integer>();
        double score = fitness.score(Integer.valueOf(1));
        assertTrue(score >= 0.0d);
        assertTrue(score < 1.0d);
    }
}
