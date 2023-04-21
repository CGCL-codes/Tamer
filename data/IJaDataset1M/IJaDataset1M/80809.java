package gnu.testlet.java.awt.geom.Rectangle2D.Double;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;
import java.awt.geom.Rectangle2D;

/**
 * Some checks for the isEmpty() method in the {@link Rectangle2D.Double}
 * class.
 */
public class isEmpty implements Testlet {

    /**
   * Runs the test using the specified harness.
   * 
   * @param harness  the test harness (<code>null</code> not permitted).
   */
    public void test(TestHarness harness) {
        Rectangle2D r = new Rectangle2D.Double();
        harness.check(r.isEmpty());
        r = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        harness.check(!r.isEmpty());
        r = new Rectangle2D.Double(1.0, 2.0, -3.0, 4.0);
        harness.check(r.isEmpty());
        r = new Rectangle2D.Double(1.0, 2.0, 3.0, -4.0);
        harness.check(r.isEmpty());
    }
}
