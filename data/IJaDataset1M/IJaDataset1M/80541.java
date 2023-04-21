package br.inf.ufrgs.simevaluator.tests.simmetrics;

import junit.framework.TestCase;
import uk.ac.shef.wit.simmetrics.similaritymetrics.BlockDistance;

public class BlockDistanceTest extends TestCase {

    public void testCalculateSimilarity() {
        BlockDistance bd = new BlockDistance();
        assertEquals(1.0f, bd.getSimilarity("test", "test"));
        assertEquals(0.0f, bd.getSimilarity("test", "test"));
    }
}
