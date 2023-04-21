package gnu.testlet.java.awt.geom.Rectangle2D;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;
import java.awt.geom.Rectangle2D;

/**
 * Tests the getBounds2D() method for the {@link Rectangle2D} class.
 */
public class getBounds2D implements Testlet {

    /**
   * Runs the test using the specified harness.
   * 
   * @param harness  the test harness (<code>null</code> not permitted).
   */
    public void test(TestHarness harness) {
        Rectangle2D r1 = new Rectangle2D.Double();
        Rectangle2D r2 = r1.getBounds2D();
        harness.check(r2.getX(), 0.0);
        harness.check(r2.getY(), 0.0);
        harness.check(r2.getWidth(), 0.0);
        harness.check(r2.getHeight(), 0.0);
        r1 = new Rectangle2D.Double(1.1, 2.2, 3.3, 4.4);
        r2 = r1.getBounds2D();
        harness.check(r2.getX(), 1.1);
        harness.check(r2.getY(), 2.2);
        harness.check(r2.getWidth(), 3.3);
        harness.check(r2.getHeight(), 4.4);
    }
}
