package gnu.testlet.java2.util.GregorianCalendar;

import gnu.testlet.TestHarness;
import gnu.testlet.Testlet;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Some checks for the getMinimum() method in the 
 * {@link GregorianCalendar} class.
 */
public class getMinimum implements Testlet {

    /**
   * Runs the test using the specified harness.
   * 
   * @param harness  the test harness (<code>null</code> not permitted).
   */
    public void test(TestHarness harness) {
        testX(harness);
    }

    /**
   * @param harness  the test harness (<code>null</code> not permitted).
   */
    private void testX(TestHarness harness) {
        Calendar c1 = new GregorianCalendar();
        harness.check(c1.getMinimum(Calendar.HOUR_OF_DAY), 0);
        harness.check(c1.getMinimum(Calendar.MINUTE), 0);
        harness.check(c1.getMinimum(Calendar.SECOND), 0);
        harness.check(c1.getMinimum(Calendar.MILLISECOND), 0);
    }
}
