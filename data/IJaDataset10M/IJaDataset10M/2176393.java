package gnu.testlet.java2.text.DecimalFormat;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;
import java.text.DecimalFormat;

/**
 * Some checks for the getPositiveSuffix() method in the {@link DecimalFormat} 
 * class.
 */
public class getPositiveSuffix implements Testlet {

    /**
   * Runs the test using the specified harness.
   * 
   * @param harness  the test harness (<code>null</code> not permitted).
   */
    public void test(TestHarness harness) {
        DecimalFormat f1 = new DecimalFormat();
        f1.setPositiveSuffix("XYZ");
        harness.check(f1.getPositiveSuffix(), "XYZ");
    }
}
