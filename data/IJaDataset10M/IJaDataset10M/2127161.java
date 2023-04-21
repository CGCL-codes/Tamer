package gnu.testlet.java2.util.Vector;

import java.util.Vector;
import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;

public class removeAll implements Testlet {

    public void test(TestHarness harness) {
        testNull(harness);
    }

    /**
   * Checks if and when null arguments to removeAll() are allowed.
   *
   * @param h the test harness
   */
    private void testNull(TestHarness h) {
        Vector v = new Vector();
        v.add(new Object());
        try {
            v.removeAll(null);
            h.fail("NPE should be thrown");
        } catch (NullPointerException ex) {
            h.check(true);
        }
    }
}
