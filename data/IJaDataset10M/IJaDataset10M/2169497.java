package itjava.tests;

import static org.junit.Assert.assertEquals;
import itjava.util.Concordance;
import org.junit.Test;

public class ConcordanceTest {

    @Test
    public void TestConcordance() {
        Concordance<String> concordance = new Concordance<String>();
        concordance.put("concordance", 1);
        concordance.put("concordance");
        assertEquals(new Integer(2), concordance.get("concordance"));
    }
}
