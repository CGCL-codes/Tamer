package gnu.testlet.java.awt.Rectangle;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Checks that the setLocation() method works correctly.
 */
public class setLocation implements Testlet {

    /**
   * Runs the test using the specified harness.
   * 
   * @param harness  the test harness (<code>null</code> not permitted).
   */
    public void test(TestHarness harness) {
        Rectangle r1 = new Rectangle(1, 2, 3, 4);
        r1.setLocation(5, 6);
        harness.check(r1.x == 5);
        harness.check(r1.y == 6);
        harness.check(r1.width == 3);
        harness.check(r1.height == 4);
        r1 = new Rectangle(1, 2, 3, 4);
        r1.setLocation(new Point(5, 6));
        harness.check(r1.x == 5);
        harness.check(r1.y == 6);
        harness.check(r1.width == 3);
        harness.check(r1.height == 4);
        boolean pass = false;
        try {
            r1.setLocation(null);
        } catch (NullPointerException e) {
            pass = true;
        }
        harness.check(pass);
    }
}
