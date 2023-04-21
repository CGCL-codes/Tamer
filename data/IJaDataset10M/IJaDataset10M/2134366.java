package org.dishevelled.cluster.similarity;

import java.util.Random;
import org.dishevelled.cluster.Similarity;
import org.dishevelled.cluster.AbstractSimilarityTest;

/**
 * Unit test for RandomSimilarity.
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class RandomSimilarityTest extends AbstractSimilarityTest {

    /** {@inheritDoc} */
    protected <T> Similarity<T> createSimilarity() {
        return new RandomSimilarity<T>();
    }

    public void testConstructor() {
        Similarity similarity0 = new RandomSimilarity();
        Similarity similarity1 = new RandomSimilarity(new Random());
        try {
            Similarity similarity = new RandomSimilarity(null);
            fail("ctr(null) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSimilarity() {
        Similarity<String> similarity = new RandomSimilarity<String>();
        for (int i = 0; i < 100; i++) {
            double value = similarity.similarity("foo", "bar");
            assertTrue(value >= 0.0d);
            assertTrue(value < 1.0d);
        }
    }
}
