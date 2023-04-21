package gnu.testlet.java2.text.SimpleDateFormat;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
* Some checks for the applyLocalizedPattern() method in the SimpleDateFormat
* class.  
*/
public class applyLocalizedPattern implements Testlet {

    /**
   * Runs the test using the specified harness.
   * 
   * @param harness  the test harness (<code>null</code> not allowed).
   */
    public void test(TestHarness harness) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy", Locale.CHINA);
        try {
            f.applyLocalizedPattern("j-nnn-aaaa");
        } catch (IllegalArgumentException iae) {
            harness.debug(iae);
            harness.check(false);
        }
        harness.check(f.toPattern(), "d-MMM-yyyy");
        try {
            f.applyLocalizedPattern("XYZ");
            harness.check(false);
        } catch (IllegalArgumentException e) {
            harness.check(true);
        }
        try {
            f.applyLocalizedPattern(null);
            harness.check(false);
        } catch (NullPointerException e) {
            harness.check(true);
        }
    }
}
