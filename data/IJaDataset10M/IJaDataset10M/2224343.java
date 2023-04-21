package gnu.testlet.java2.text.DateFormatSymbols;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;
import java.text.DateFormatSymbols;
import java.util.Locale;

/**
 * Some checks for the setZoneStrings() method in the DateFormatSymbols
 * class.  
 */
public class setZoneStrings implements Testlet {

    /**
   * Runs the test using the specified harness.
   * 
   * @param harness  the test harness (<code>null</code> not allowed).
   */
    public void test(TestHarness harness) {
        DateFormatSymbols dfs = new DateFormatSymbols(Locale.UK);
        try {
            dfs.setZoneStrings(null);
            harness.check(false);
        } catch (NullPointerException e) {
            harness.check(true);
        }
    }
}
