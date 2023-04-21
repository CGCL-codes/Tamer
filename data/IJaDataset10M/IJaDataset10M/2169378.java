package ie.ul.brendancleary.forager.vsm;

import java.util.HashMap;
import java.util.Vector;
import junit.framework.TestCase;

public class VSMToolsTest extends TestCase {

    public void testCalculateTFIDF() {
    }

    public void testComputeQueryVector() {
        HashMap<String, int[]> vocab = new HashMap<String, int[]>();
        vocab.put("a", new int[] { 1, 0 });
        vocab.put("b", new int[] { 2, 0 });
        vocab.put("c", new int[] { 3, 0 });
        vocab.put("d", new int[] { 4, 0 });
        vocab.put("e", new int[] { 5, 0 });
        Vector<String> query = new Vector<String>();
        query.add("a");
        query.add("a");
        query.add("b");
        query.add("2");
        query.add("c");
        query.add("c");
        query.add("e");
        query.add("e");
        query.add("a");
        query.add("w");
        Vector<Double> resultVector = VSMTools.computeQueryVector(vocab, query);
        assertEquals(3.0, resultVector.get(1), 0.0);
        assertEquals(1.0, resultVector.get(2), 0.0);
        assertEquals(2.0, resultVector.get(3), 0.0);
        assertNull(resultVector.get(4));
        assertEquals(2.0, resultVector.get(5), 0.0);
    }
}
