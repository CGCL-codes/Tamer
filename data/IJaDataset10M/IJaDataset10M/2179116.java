package gnu.testlet.java2.text.DecimalFormat;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Some checks for the setDecimalFormatSymbols() method in the 
 * {@link DecimalFormat} class.
 */
public class setDecimalFormatSymbols implements Testlet {

    /**
   * Runs the test using the specified harness.
   * 
   * @param harness  the test harness (<code>null</code> not permitted).
   */
    public void test(TestHarness harness) {
        DecimalFormat f1 = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('x');
        f1.setDecimalFormatSymbols(symbols);
        harness.check(f1.getDecimalFormatSymbols().getDecimalSeparator(), 'x');
        symbols.setDecimalSeparator('y');
        harness.check(f1.getDecimalFormatSymbols().getDecimalSeparator(), 'x');
        boolean pass = false;
        try {
            f1.setDecimalFormatSymbols(null);
            pass = true;
        } catch (NullPointerException e) {
        }
        harness.check(pass);
    }
}
