package gnu.testlet.java2.util.StringTokenizer;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;
import java.util.StringTokenizer;

/**
 * Some checks for the hasMoreTokens() method.
 */
public class hasMoreTokens implements Testlet {

    /**
   * Some checks for the hasMoreTokens() method.  
   * 
   * @param harness  the test harness.
   */
    public void test(TestHarness harness) {
        StringTokenizer t1 = new StringTokenizer("one two");
        harness.check(t1.hasMoreTokens());
        t1.nextToken();
        harness.check(t1.hasMoreTokens());
        t1.nextToken();
        harness.check(!t1.hasMoreTokens());
        StringTokenizer t2 = new StringTokenizer("");
        harness.check(!t2.hasMoreTokens());
    }
}
