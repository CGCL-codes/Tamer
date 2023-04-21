package gnu.testlet.java2.util.SimpleTimeZone;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;
import java.util.SimpleTimeZone;

/**
 * Some checks for the constants in the SimpleTimeZone class.
 */
public class constants implements Testlet {

    /**
   * Runs the test using the specified harness.
   * 
   * @param harness  the test harness (<code>null</code> not permitted).
   */
    public void test(TestHarness harness) {
        harness.check(SimpleTimeZone.STANDARD_TIME, 1);
        harness.check(SimpleTimeZone.UTC_TIME, 2);
        harness.check(SimpleTimeZone.WALL_TIME, 0);
    }
}
