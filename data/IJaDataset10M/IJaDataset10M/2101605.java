package gnu.testlet.java2.text.DecimalFormat;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;
import java.text.DecimalFormat;

/**
 * Some checks for the setDecimalSeparatorAlwaysShown() method in the 
 * {@link DecimalFormat} class.
 */
public class setDecimalSeparatorAlwaysShown implements Testlet {

    /**
   * Runs the test using the specified harness.
   * 
   * @param harness  the test harness (<code>null</code> not permitted).
   */
    public void test(TestHarness harness) {
        DecimalFormat f1 = new DecimalFormat();
        f1.setDecimalSeparatorAlwaysShown(true);
        harness.check(f1.isDecimalSeparatorAlwaysShown());
        f1.setDecimalSeparatorAlwaysShown(false);
        harness.check(!f1.isDecimalSeparatorAlwaysShown());
    }
}
