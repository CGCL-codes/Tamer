package tei.cr.merger.aligner;

import java.util.Arrays;
import junit.framework.TestCase;

public class TestTokenAligner extends TestCase {

    ObjectDiff d;

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
        d = null;
    }

    public void testDiffIdentical() {
        String hyp[] = new String[] { "Hypoth�se", "et", "r�f�rence", "sont", "pareils" };
        String ref[] = hyp;
        TokenAligner t = new TokenAligner();
        int[] found = t.align(hyp, ref);
        int[] expected = new int[] { 0, 1, 2, 3, 4 };
        assertTrue(Arrays.equals(expected, found));
    }

    public void testDiffRemoveInHyp() {
        String hyp[] = new String[] { "Hypoth�se", "et", "r�f�rence", "sont", "pareils" };
        String ref[] = new String[] { "Hypoth�se", "et", "r�f�rence", "sont", "pas", "pareils" };
        TokenAligner t = new TokenAligner();
        int[] found = t.align(hyp, ref);
        int[] expected = new int[] { 0, 1, 2, 3, 5 };
        assertTrue(Arrays.equals(expected, found));
    }

    public void testDiffAddedInHyp() {
        String hyp[] = new String[] { "Hypoth�se", "et", "r�f�rence", "sont", "pas", "toutafait", "pareils" };
        String ref[] = new String[] { "Hypoth�se", "et", "r�f�rence", "sont", "pas", "pareils" };
        TokenAligner t = new TokenAligner();
        int[] found = t.align(hyp, ref);
        int[] expected = new int[] { 0, 1, 2, 3, 4, -1, 5 };
        assertTrue(Arrays.equals(expected, found));
    }

    public void testDiffComplex1() {
        String hyp[] = new String[] { "Hypoth�se", "et", "r�f�rence", "ne", "sont", "pas", "toutafait", "pareils", "presque" };
        String ref[] = new String[] { "Hypoth�se", "et", "r�f�rence", "sont", "pas", "pareils", "mais", "presque" };
        TokenAligner t = new TokenAligner();
        int[] found = t.align(hyp, ref);
        int[] expected = new int[] { 0, 1, 2, -1, 3, 4, -1, 5, 7 };
        assertTrue("Expected: " + Arrays.toString(expected) + "; found: " + Arrays.toString(found) + ".", Arrays.equals(expected, found));
    }
}
